package rs._1._4.rs1124.service.process.console.steps;

import rs._1._4.rs1124.service.process.BasicStep;
import rs._1._4.rs1124.service.process.StepTemplate;

public class ExitStep extends BasicStep {

    public ExitStep(Builder builder) {
        super(builder);
    }

    @Override
    public Object clone() {
        return new ExitStep(new StepTemplate.Builder(getLabel())
                .userPrompt(getUserPrompt())
                .validationErrorMessage(getValidationErrorMessage())
                .successMessage(getSuccessMessage())
                .subProcess(isSubProcess()));
    }
}
