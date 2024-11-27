package rs._1._4.rs1124.service.process.console.steps;

import rs._1._4.rs1124.service.process.StepTemplate;
import rs._1._4.rs1124.service.validation.InputValidationException;

public class RequestTaskStep extends StepTemplate {

    public RequestTaskStep(Builder builder) {
        super(builder);
    }

    @Override
    public void validateInput(String input) throws InputValidationException {
        try {
            int n = Integer.parseInt(input);
            if (!(1 <= n && n <= 3)) {
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
        return new RequestTaskStep(new StepTemplate.Builder(getLabel())
                .userPrompt(getUserPrompt())
                .validationErrorMessage(getValidationErrorMessage())
                .successMessage(getSuccessMessage())
                .subProcess(isSubProcess()));
    }
}
