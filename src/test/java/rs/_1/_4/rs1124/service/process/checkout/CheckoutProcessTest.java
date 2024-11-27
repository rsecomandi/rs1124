package rs._1._4.rs1124.service.process.checkout;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import rs._1._4.rs1124.Rs1124Application;
import rs._1._4.rs1124.persistence.rule.ChargeRule;
import rs._1._4.rs1124.persistence.rule.ChargeRuleService;
import rs._1._4.rs1124.persistence.tool.ToolInfo;
import rs._1._4.rs1124.persistence.tool.ToolInfoService;
import rs._1._4.rs1124.service.process.AggregationStep;
import rs._1._4.rs1124.service.process.BasicProcessFactory;
import rs._1._4.rs1124.service.process.Step;
import rs._1._4.rs1124.service.validation.InputValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static rs._1._4.rs1124.presentation.Reference.DEFAULT_DATE_FORMAT;
import static rs._1._4.rs1124.presentation.Reference.YES_INPUT;

public class CheckoutProcessTest {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT, Locale.US);
    private static Map<String, ToolInfo> toolInfoMap;
    private static Map<String, ChargeRule> chargeRuleMap;

    @Mock
    ToolInfoService toolInfoService;
    @Mock
    ChargeRuleService chargeRuleService;
    
    @BeforeAll
    static void populateMaps() {
        toolInfoMap = new HashMap<>();
        toolInfoMap.put("CHNS", new ToolInfo("CHNS", "Chainsaw", "Stihl"));
        toolInfoMap.put("LADW", new ToolInfo("LADW", "Ladder", "Weber"));
        toolInfoMap.put("JAKD", new ToolInfo("JAKD", "Jackhammer", "DeWalt"));
        toolInfoMap.put("JAKR", new ToolInfo("JAKR", "Jackhammer", "Ridgid"));
        
        chargeRuleMap = new HashMap<>();
        chargeRuleMap.put("Ladder", new ChargeRule("Ladder", new BigDecimal("1.99"), true, true, false));
        chargeRuleMap.put("Chainsaw", new ChargeRule("Chainsaw", new BigDecimal("1.49"), true, false, true));
        chargeRuleMap.put("Jackhammer", new ChargeRule("Jackhammer", new BigDecimal("2.99"), true, false, false));
    }

    @BeforeEach
    void setUp() throws Exception {
        try (AutoCloseable openMocks = MockitoAnnotations.openMocks(this)) {}
    }

    @ParameterizedTest
    @ArgumentsSource(CheckoutTestCaseArgumentsProvider.class)
    void testScenario(CheckoutTestCase testCase) {
        try (MockedStatic<Rs1124Application> mockedStatic = mockStatic(Rs1124Application.class)) {
            when(Rs1124Application.getToolInfoService()).thenReturn(toolInfoService);
            when(Rs1124Application.getChargeRuleService()).thenReturn(chargeRuleService);
            when(Rs1124Application.getDefaultDateFormatter()).thenReturn(dateFormatter);
            when(Rs1124Application.getHolidayRule("julyfourth")).thenReturn("M07.D04.?WE:near");
            when(Rs1124Application.getHolidayRule("laborday")).thenReturn("M09.MONDAY:1");
            when(Rs1124Application.getToday()).thenReturn(LocalDate.parse(testCase.getCheckoutDate(), dateFormatter));

            // Start a checkout process
            CheckoutProcess checkoutProcess = (CheckoutProcess) BasicProcessFactory.getNewProcess(BasicProcessFactory.ProcessType.CHECKOUT);

            boolean handledExpectedAbort = false;
            String output = null;
            while (!checkoutProcess.isComplete()) {
                // Add the values from the test file as user input to the appropriate process step
                Step currentStep = checkoutProcess.getCurrentStep();
                output = switch (currentStep.getLabel()) {
                    case "REQUEST_TOOL_CODE" -> {
                        when(toolInfoService.getToolInfoByToolCode(testCase.getToolCode())).thenReturn(toolInfoMap.get(testCase.getToolCode()));
                        String returnString = currentStep.doStep(testCase.getToolCode());
                        mockedStatic.verify(Rs1124Application::getToolInfoService);
                        yield returnString;
                    }
                    case "REQUEST_RENTAL_DAY_COUNT" -> currentStep.doStep(testCase.getRentalDayCount());
                    case "REQUEST_DISCOUNT_PERCENT" -> {
                        String returnString = null;
                        int percent = Integer.parseInt(testCase.getDiscountPercent());
                        if (percent < 0 || percent > 100) {
                            assertThrows(InputValidationException.class, () -> currentStep.doStep(testCase.getDiscountPercent()));
                            handledExpectedAbort = true;
                        } else {
                            returnString = currentStep.doStep(testCase.getDiscountPercent());
                        }
                        yield returnString;
                    }
                    case "REQUEST_CHECKOUT_DATE" -> currentStep.doStep(testCase.getCheckoutDate());
                    case "REQUEST_CONFIRMATION" -> currentStep.doStep(YES_INPUT);
                    case "PRINT_RENTAL_AGREEMENT" -> {
                        String toolType = toolInfoMap.get(testCase.getToolCode()).getToolType();
                        when(chargeRuleService.getChargeRuleByToolType(toolType)).thenReturn(chargeRuleMap.get(toolType));
                        ((AggregationStep) currentStep).setUserCollectedInfoSteps(checkoutProcess.getInfoCollectedSteps());
                        String returnString = currentStep.doStep(null);
                        mockedStatic.verify(Rs1124Application::getChargeRuleService);
                        yield returnString;
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + currentStep.getLabel());
                };

                if (handledExpectedAbort || !checkoutProcess.hasNextStep()) {
                    checkoutProcess.setComplete(true);
                } else {
                    checkoutProcess.advance();
                }
            }
            if (!handledExpectedAbort) {
                assertNotNull(output);
                for (String expectation : testCase.getExpectations()) {
                    if (!output.contains(expectation)) {
                        System.out.println(output);
                        System.out.println("Failed Expectation -> " + expectation);
                        fail();
                    }

                }
            }
        }
    }
}