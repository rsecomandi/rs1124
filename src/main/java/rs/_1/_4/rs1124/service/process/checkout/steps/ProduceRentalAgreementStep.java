package rs._1._4.rs1124.service.process.checkout.steps;

import rs._1._4.rs1124.persistence.rule.ChargeRule;
import rs._1._4.rs1124.persistence.tool.ToolInfo;
import rs._1._4.rs1124.presentation.MessageUtil;
import rs._1._4.rs1124.service.calculators.HolidayCalculator;
import rs._1._4.rs1124.service.process.AggregationStep;
import rs._1._4.rs1124.service.process.StepTemplate;
import rs._1._4.rs1124.service.process.checkout.CheckoutProcess;
import rs._1._4.rs1124.service.validation.UnexpectedInputException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Set;

import static rs._1._4.rs1124.Rs1124Application.getChargeRuleService;
import static rs._1._4.rs1124.presentation.Reference.*;

public class ProduceRentalAgreementStep extends AggregationStep {

  private StringBuilder content = new StringBuilder();
  private ToolInfo toolInfo = null;
  private Integer rentalDays = null;
  private LocalDate checkoutDate = null;
  private Integer discountPercent = null;
  public ProduceRentalAgreementStep(Builder builder) {
    super(builder);
  }

  @Override
  public void doWork() {
        resetContent();
        processInfoCollected();
        assembleContent();
  }

  private void processInfoCollected() {
    getUserCollectedInfoSteps().forEach(step -> {
      switch (CheckoutProcess.StepDefinition.valueOf(step.getLabel())) {
        case REQUEST_TOOL_CODE -> setToolInfo(((RequestItemCodeStep) step).getToolInfo());
        case REQUEST_RENTAL_DAY_COUNT -> setRentalDays((int) step.getInfoCollected());
        case REQUEST_DISCOUNT_PERCENT -> setDiscountPercent((int) step.getInfoCollected());
        case REQUEST_CHECKOUT_DATE -> setCheckoutDate((LocalDate) step.getInfoCollected());
        case REQUEST_CONFIRMATION -> {}
        case PRINT_RENTAL_AGREEMENT -> throw new UnexpectedInputException(ERROR_PROCESS_CONFIG_GENERIC);
      }
    });
  }

  private void assembleContent() {
    if (getToolInfo() == null || getRentalDays() == null || getCheckoutDate() == null || getDiscountPercent() == null) {
      throw new UnexpectedInputException(ERROR_GENERATE_RENTAL_AGREEMENT);
    }

    ChargeRule chargeRule = getChargeRuleService().getChargeRuleByToolType(getToolInfo().getToolType());
    Integer chargeDays = calculateChargeDays(getCheckoutDate(), chargeRule);

    BigDecimal dailyRentalCharge = chargeRule.getDailyCharge();
    BigDecimal preDiscountTotal = dailyRentalCharge.multiply(new BigDecimal(chargeDays)).setScale(2, RoundingMode.HALF_UP);
    BigDecimal discountMultiplier = new BigDecimal(getDiscountPercent()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    BigDecimal discountAmount = preDiscountTotal.multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);

    content
            // REQ: Include tool code - specified at checkout.
            .append(formatLineItem(LABEL_TOOL_CODE, getToolInfo().getToolCode()))
            // REQ: Include tool type - from tool info.
            .append(formatLineItem(LABEL_TOOL_TYPE, getToolInfo().getToolType()))
            // REQ: Include tool brand - from tool info.
            .append(formatLineItem(LABEL_TOOL_BRAND, getToolInfo().getBrand()))
            // REQ: Include rental days - specified at checkout
            .append(formatLineItem(LABEL_RENTAL_DAYS, getRentalDays().toString()))
            // REQ: Include checkout date - specified at checkout.
            .append(formatLineItem(LABEL_CHECKOUT_DATE, MessageUtil.formatDate(checkoutDate)))
            // REQ: Include due date - calculated from checkout date and rental days.
            .append(formatLineItem(LABEL_DUE_DATE, MessageUtil.formatDate(checkoutDate.plusDays(getRentalDays()))))
            // REQ: Include daily rental charge as the amount per day, specified by tool type.
            .append(formatLineItem(LABEL_DAILY_CHARGE, MessageUtil.formatCurrency(dailyRentalCharge)))
            // REQ: Include charge days - count of chargeable days, from day after checkout through
            // and including due date, excluding "no charge" days as specified by the tool type.
            .append(formatLineItem(LABEL_CHARGE_DAYS, chargeDays.toString()))
            // REQ: Include pre-discount total - calculated as charge days X daily charge. Resulting total rounded half up to cents.
            .append(formatLineItem(LABEL_PRE_DISCOUNT_TOTAL, MessageUtil.formatCurrency(preDiscountTotal)))
            // REQ: Include discount percent - specified at checkout.
            .append(formatLineItem(LABEL_DISCOUNT_PERCENT, MessageUtil.formatPercent(discountPercent)))
            // REQ: Include discount amount - calculated from discount % and pre-discount charge. Resulting amount rounded half up to cents.
            .append(formatLineItem(LABEL_DISCOUNT_AMOUNT, MessageUtil.formatCurrency(discountAmount)))
            // REQ: Include final charge - calculated as pre-discount charge minus discount amount.
            .append(formatLineItem(LABEL_FINAL_CHARGE, MessageUtil.formatCurrency(preDiscountTotal.subtract(discountAmount))));
  }

  public Integer calculateChargeDays(LocalDate checkoutDate, ChargeRule chargeRule) {
    Set<LocalDate> noChargeDates = new HolidayCalculator().getAllUpcomingHolidays();

    // REQ: Some tools are free of charge on weekends and holidays
    LocalDate currentDate = checkoutDate;
    Integer chargeDays = 0;
    for (int daysCounted = 0; daysCounted < getRentalDays(); daysCounted++) {
      currentDate = currentDate.plusDays(1);
      if (HolidayCalculator.isWeekend(currentDate)) {
        if (!chargeRule.isFreeOnWeekends()) {
          if (!chargeRule.isFreeOnHolidays() || !noChargeDates.contains(currentDate)) {
            chargeDays++;
          }
        }
      } else {
        if (!chargeRule.isFreeOnWeekdays()) {
          if (!chargeRule.isFreeOnHolidays() || !noChargeDates.contains(currentDate)) {
            chargeDays++;
          }
        }
      }
    }
    return chargeDays;
  }

  public static String formatLineItem(String label, String value) {
    return label + LABEL_VALUE_SEPARATOR + value + System.lineSeparator();
  }

  public void resetContent() {
    content = new StringBuilder();
    rentalDays = null;
    checkoutDate = null;
    discountPercent = null;
    content.append(RENTAL_AGREEMENT_BLOCK)
            .append(System.lineSeparator())
            .append(System.lineSeparator());
  }

  private String getContentString() {
    return content.toString();
  }

  public ToolInfo getToolInfo() {
    return toolInfo;
  }

  public void setToolInfo(ToolInfo toolInfo) {
    this.toolInfo = toolInfo;
  }

  public Integer getRentalDays() {
    return rentalDays;
  }

  public void setRentalDays(Integer rentalDays) {
    this.rentalDays = rentalDays;
  }

  public LocalDate getCheckoutDate() {
    return checkoutDate;
  }

  public void setCheckoutDate(LocalDate checkoutDate) {
    this.checkoutDate = checkoutDate;
  }

  public Integer getDiscountPercent() {
    return discountPercent;
  }

  public void setDiscountPercent(Integer discountPercent) {
    this.discountPercent = discountPercent;
  }

  @Override
  public String getSuccessMessage() {
    return getContentString();
  }

  @Override
  public Object clone() {
    return new ProduceRentalAgreementStep(new StepTemplate.Builder(getLabel())
            .userPrompt(getUserPrompt())
            .validationErrorMessage(getValidationErrorMessage())
            .successMessage(getSuccessMessage())
            .subProcess(isSubProcess()));
  }
}
