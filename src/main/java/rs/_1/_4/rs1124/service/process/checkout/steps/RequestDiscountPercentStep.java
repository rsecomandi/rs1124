package rs._1._4.rs1124.service.process.checkout.steps;

import rs._1._4.rs1124.service.process.StepTemplate;
import rs._1._4.rs1124.service.validation.InputValidationException;

public class RequestDiscountPercentStep extends StepTemplate {

  public RequestDiscountPercentStep(Builder builder) {
    super(builder);
  }

  @Override
  public void validateInput(String input) throws InputValidationException {
    try {
      int n = Short.parseShort(input);
      if (!(0 <= n && n <= 100)) {
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
    return new RequestDiscountPercentStep(new StepTemplate.Builder(getLabel())
            .userPrompt(getUserPrompt())
            .validationErrorMessage(getValidationErrorMessage())
            .successMessage(getSuccessMessage())
            .subProcess(isSubProcess()));
  }
}
