package rs._1._4.rs1124.service.process.console.steps;

import rs._1._4.rs1124.service.process.BasicProcessFactory;
import rs._1._4.rs1124.service.process.BasicStep;
import rs._1._4.rs1124.service.process.ProcessFlow;
import rs._1._4.rs1124.service.process.StepTemplate;

public class CheckoutStep extends BasicStep {

    public CheckoutStep(Builder builder) {
        super(builder);
    }

    @Override
    public ProcessFlow getStepSubProcess() {
        return BasicProcessFactory.getNewProcess(BasicProcessFactory.ProcessType.CHECKOUT);
    }

    @Override
    public Object clone() {
        return new CheckoutStep(new StepTemplate.Builder(getLabel())
                .userPrompt(getUserPrompt())
                .validationErrorMessage(getValidationErrorMessage())
                .successMessage(getSuccessMessage())
                .subProcess(isSubProcess()));
    }
}
