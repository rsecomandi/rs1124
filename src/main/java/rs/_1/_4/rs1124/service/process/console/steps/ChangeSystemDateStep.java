package rs._1._4.rs1124.service.process.console.steps;

import rs._1._4.rs1124.service.process.StepTemplate;
import rs._1._4.rs1124.service.validation.InputValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static rs._1._4.rs1124.Main.*;
import static rs._1._4.rs1124.presentation.Reference.*;

public class ChangeSystemDateStep extends StepTemplate {

    public ChangeSystemDateStep(Builder builder) {
        super(builder);
    }

    @Override
    public void validateInput(String input) throws InputValidationException {
        try {
            LocalDate.parse(input, getDefaultDateFormatter());
        } catch (DateTimeParseException e) {
            throw new InputValidationException(getValidationErrorMessage());
        }
    }

    @Override
    public String getValidationErrorMessage() {
        String message = super.getValidationErrorMessage();
        return String.format(message, DEFAULT_DATE_FORMAT);
    }

    @Override
    public void processInput(String input) {
        setToday(LocalDate.parse(input, getDefaultDateFormatter()));
        setRequireInput(false);
    }

    @Override
    public String getSuccessMessage() {
        return String.format(super.getSuccessMessage(), getTodayString());
    }

    @Override
    public Object clone() {
        return new ChangeSystemDateStep(new StepTemplate.Builder(getLabel())
                .userPrompt(getUserPrompt())
                .validationErrorMessage(getValidationErrorMessage())
                // we want to use super's version to avoid setting values while cloning.
                .successMessage(super.getSuccessMessage())
                .subProcess(isSubProcess()));
    }
}
