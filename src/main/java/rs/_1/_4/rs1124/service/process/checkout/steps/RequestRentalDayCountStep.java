package rs._1._4.rs1124.service.process.checkout.steps;

import rs._1._4.rs1124.service.process.StepTemplate;
import rs._1._4.rs1124.service.validation.InputValidationException;

public class RequestRentalDayCountStep extends StepTemplate {

  public RequestRentalDayCountStep(Builder builder) {
    super(builder);
  }

  @Override
  public void validateInput(String input) throws InputValidationException {
    // Explicit Requirement: input is a number greater than zero.
    try {
      int n = Integer.parseInt(input);
      if (n < 1) {
        throw new InputValidationException(getValidationErrorMessage());
      }
    } catch (NumberFormatException e) {
      throw new InputValidationException(getValidationErrorMessage());
    }
  }

  @Override
  public void processInput(String input) {
    setInfoCollected(Integer.parseInt(input));
    setRequireInput(false);
  }

  @Override
  public Object clone() {
    return new RequestRentalDayCountStep(new StepTemplate.Builder(getLabel())
            .userPrompt(getUserPrompt())
            .validationErrorMessage(getValidationErrorMessage())
            .successMessage(getSuccessMessage())
            .subProcess(isSubProcess()));
  }
}
