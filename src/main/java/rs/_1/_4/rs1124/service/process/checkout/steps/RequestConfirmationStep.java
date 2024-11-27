package rs._1._4.rs1124.service.process.checkout.steps;

import static rs._1._4.rs1124.presentation.Reference.*;

import org.apache.commons.lang3.StringUtils;
import rs._1._4.rs1124.service.process.StepTemplate;
import rs._1._4.rs1124.service.validation.InputValidationException;

public class RequestConfirmationStep extends StepTemplate {

  public RequestConfirmationStep(Builder builder) {
    super(builder);
  }

  @Override
  public void validateInput(String input) throws InputValidationException {
    if (!StringUtils.equalsAnyIgnoreCase(input, VALID_CONFIRMATION_INPUT)) {
      throw new InputValidationException(getValidationErrorMessage());
    }
  }

  @Override
  public void processInput(String input) {
    if (YES_INPUT.equalsIgnoreCase(input)) {
      setRequireInput(false);
    } else {
      setAbort(true);
    }
  }

  @Override
  public String getSuccessMessage() {
    if (isAbort()) {
      return TRANSACTION_CANCELED_BLOCK;
    } else {
      return super.getSuccessMessage();
    }
  }

  @Override
  public Object clone() {
    return new RequestConfirmationStep(new StepTemplate.Builder(getLabel())
            .userPrompt(getUserPrompt())
            .validationErrorMessage(getValidationErrorMessage())
            .successMessage(getSuccessMessage())
            .subProcess(isSubProcess()));
  }
}
