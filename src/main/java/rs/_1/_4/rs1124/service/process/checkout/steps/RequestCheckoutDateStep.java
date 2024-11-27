package rs._1._4.rs1124.service.process.checkout.steps;

import static rs._1._4.rs1124.Main.*;
import static rs._1._4.rs1124.presentation.Reference.DEFAULT_DATE_FORMAT;
import static rs._1._4.rs1124.presentation.Reference.LOG_GENERIC_DATE_PARSE_ERROR;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import rs._1._4.rs1124.service.process.StepTemplate;
import rs._1._4.rs1124.service.validation.InputValidationException;

public class RequestCheckoutDateStep extends StepTemplate {

  public RequestCheckoutDateStep(Builder builder) {
    super(builder);
  }

  @Override
  public void validateInput(String input) throws InputValidationException {
    try {
      LocalDate targetDate = LocalDate.parse(input, getDefaultDateFormatter());
      if (targetDate.isBefore(getToday())) {
        throw new InputValidationException(getValidationErrorMessage());
      }
    } catch (DateTimeParseException e) {
      throw new InputValidationException(getValidationErrorMessage());
    }
  }

  @Override
  public String getValidationErrorMessage() {
    String message = super.getValidationErrorMessage();
    return String.format(message, DEFAULT_DATE_FORMAT, getTodayString());
  }

  @Override
  public void processInput(String input) {
    try {
      setInfoCollected(LocalDate.parse(input, getDefaultDateFormatter()));
      setRequireInput(false);
    } catch (DateTimeParseException e) {
      // i.e: "Failed to parse "2024-11-05" to a date using format "DD/MM/YYYY" in RequestCheckoutDateStep.processInput."
      logger.warn(String.format(LOG_GENERIC_DATE_PARSE_ERROR, input, DEFAULT_DATE_FORMAT, this.getClass().getName() + "." + new Object() {}.getClass().getEnclosingMethod().getName()));
    }
  }

  @Override
  public Object clone() {
    return new RequestCheckoutDateStep(new StepTemplate.Builder(getLabel())
            .userPrompt(getUserPrompt())
            // we want to use super's version to avoid setting values while cloning.
            .validationErrorMessage(super.getValidationErrorMessage())
            .successMessage(getSuccessMessage())
            .subProcess(isSubProcess()));
  }
}
