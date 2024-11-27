package rs._1._4.rs1124.service.process.checkout;

import static rs._1._4.rs1124.presentation.Reference.ERROR_INPUT_INVALID_CHECKOUT_DATE;
import static rs._1._4.rs1124.presentation.Reference.ERROR_INPUT_INVALID_CONFIRMATION;
import static rs._1._4.rs1124.presentation.Reference.ERROR_INPUT_INVALID_DISCOUNT_PERCENT;
import static rs._1._4.rs1124.presentation.Reference.ERROR_INPUT_INVALID_RENTAL_DAY_COUNT;
import static rs._1._4.rs1124.presentation.Reference.ERROR_INPUT_INVALID_TOOL_CODE;
import static rs._1._4.rs1124.presentation.Reference.PROMPT_CHECKOUT_CONFIRMATION;
import static rs._1._4.rs1124.presentation.Reference.PROMPT_CHECKOUT_DATE_REQ;
import static rs._1._4.rs1124.presentation.Reference.PROMPT_CHECKOUT_DISCOUNT_REQ;
import static rs._1._4.rs1124.presentation.Reference.PROMPT_CHECKOUT_RENTAL_DAY_REQ;
import static rs._1._4.rs1124.presentation.Reference.PROMPT_CHECKOUT_TOOL_CODE_REQ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import rs._1._4.rs1124.service.process.BasicProcess;
import rs._1._4.rs1124.service.process.Step;
import rs._1._4.rs1124.service.process.StepTemplate;
import rs._1._4.rs1124.service.process.checkout.steps.ProduceRentalAgreementStep;
import rs._1._4.rs1124.service.process.checkout.steps.RequestCheckoutDateStep;
import rs._1._4.rs1124.service.process.checkout.steps.RequestConfirmationStep;
import rs._1._4.rs1124.service.process.checkout.steps.RequestDiscountPercentStep;
import rs._1._4.rs1124.service.process.checkout.steps.RequestItemCodeStep;
import rs._1._4.rs1124.service.process.checkout.steps.RequestRentalDayCountStep;

public class CheckoutProcess extends BasicProcess {
  public enum StepDefinition {
    REQUEST_TOOL_CODE(new RequestItemCodeStep(new StepTemplate.Builder("REQUEST_TOOL_CODE")
            .userPrompt(PROMPT_CHECKOUT_TOOL_CODE_REQ)
            .validationErrorMessage(ERROR_INPUT_INVALID_TOOL_CODE))),
    REQUEST_RENTAL_DAY_COUNT(new RequestRentalDayCountStep(new StepTemplate.Builder("REQUEST_RENTAL_DAY_COUNT")
            .userPrompt(PROMPT_CHECKOUT_RENTAL_DAY_REQ)
            // REQ: instructive, user-friendly message if rental day count is not 1 or greater.
            .validationErrorMessage(ERROR_INPUT_INVALID_RENTAL_DAY_COUNT))),
    REQUEST_DISCOUNT_PERCENT(new RequestDiscountPercentStep(new StepTemplate.Builder("REQUEST_DISCOUNT_PERCENT")
            .userPrompt(PROMPT_CHECKOUT_DISCOUNT_REQ)
            // REQ: instructive, user-friendly message if discount percent is not in the range 1-100.
            .validationErrorMessage(ERROR_INPUT_INVALID_DISCOUNT_PERCENT))),
    REQUEST_CHECKOUT_DATE(new RequestCheckoutDateStep(new StepTemplate.Builder("REQUEST_CHECKOUT_DATE")
            .userPrompt(PROMPT_CHECKOUT_DATE_REQ)
            .validationErrorMessage(ERROR_INPUT_INVALID_CHECKOUT_DATE))),
    REQUEST_CONFIRMATION(new RequestConfirmationStep(new StepTemplate.Builder("REQUEST_CONFIRMATION")
            .userPrompt(PROMPT_CHECKOUT_CONFIRMATION)
            .validationErrorMessage(ERROR_INPUT_INVALID_CONFIRMATION))),
    PRINT_RENTAL_AGREEMENT(new ProduceRentalAgreementStep(new StepTemplate.Builder("PRINT_RENTAL_AGREEMENT")));
    private final Step step;

    StepDefinition(Step step) {
      this.step = step;
    }

    public Step getCopyOfTemplate() {
      return step.getClone();
    }
  }

  private List<Step> steps;
  private int currentStepIndex;

  @Override
  public void initializeSteps() {
    steps = new ArrayList<>();
    Arrays.stream(StepDefinition.values())
            .sequential()
            .forEach(stepDefinition -> steps.add(stepDefinition.getCopyOfTemplate()));
    currentStepIndex = 0;
  }

  @Override
  public Step getCurrentStep() {
    return steps.get(currentStepIndex);
  }

  @Override
  public boolean hasNextStep() {
    return currentStepIndex < steps.size() - 1;
  }

  // This process performs its steps sequentially.
  // So the next step is simply the next on the list.
  @Override
  public void advance() {
    currentStepIndex++;
  }

  @Override
  public Collection<Step> getInfoCollectedSteps() {
    return steps.stream().filter(Step::hasInfoCollected).collect(Collectors.toList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CheckoutProcess that = (CheckoutProcess) o;
    return currentStepIndex == that.currentStepIndex && Objects.equals(steps, that.steps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steps, currentStepIndex);
  }
}

