package rs._1._4.rs1124.service.process.console;

import rs._1._4.rs1124.service.process.BasicProcess;
import rs._1._4.rs1124.service.process.Step;
import rs._1._4.rs1124.service.process.StepTemplate;
import rs._1._4.rs1124.service.process.console.steps.ChangeSystemDateStep;
import rs._1._4.rs1124.service.process.console.steps.CheckoutStep;
import rs._1._4.rs1124.service.process.console.steps.ExitStep;
import rs._1._4.rs1124.service.process.console.steps.RequestTaskStep;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static rs._1._4.rs1124.presentation.Reference.*;

public class ConsoleProcess extends BasicProcess {

    private enum StepDefinition {
        REQUEST_TASK(new RequestTaskStep(new StepTemplate.Builder("REQUEST_TASK")
                .userPrompt(PROMPT_CONSOLE_SELECT_TASK)
                .validationErrorMessage(ERROR_INPUT_GENERIC_INVALID_OPTION))),
        CHECKOUT(new CheckoutStep(new StepTemplate.Builder("CHECKOUT")
                .subProcess(true))),
        CHANGE_SYSTEM_DATE(new ChangeSystemDateStep(new StepTemplate.Builder("CHANGE_SYSTEM_DATE")
                .userPrompt(PROMPT_CONSOLE_CHANGE_DATE)
                .validationErrorMessage(ERROR_INPUT_GENERIC_INVALID_DATE_FORMAT)
                .successMessage(TODAY_IS))),
        QUIT(new ExitStep(new StepTemplate.Builder("QUIT")
                .successMessage(GOODBYE)));
        private final Step step;

        StepDefinition(Step step) {
            this.step = step;
        }

        public Step getCopyOfTemplate() {
            return step.getClone();
        }

        public Step getTemplate() {
            return step;
        }
    }

    private Map<StepDefinition, Step> steps;
    private StepDefinition currentStepKey;

    @Override
    public void initializeSteps() {
        steps = new HashMap<>();
        Arrays.stream(ConsoleProcess.StepDefinition.values())
                .forEach(stepDefinition -> steps.put(stepDefinition, stepDefinition.getCopyOfTemplate()));
        currentStepKey = StepDefinition.REQUEST_TASK;
    }

    // This process relies on user input to determine what the next step should be.
    @Override
    public void advance() {
        StepDefinition nextStep = switch (getOption()) {
            case 1 -> StepDefinition.CHECKOUT;
            case 2 -> StepDefinition.CHANGE_SYSTEM_DATE;
            case 3 -> StepDefinition.QUIT;
            default -> StepDefinition.REQUEST_TASK;
        };
        clearOption();
        // reset the requireUserInput flag on the step we are moving from if needed
        if (currentStepKey.getTemplate().isRequireInput()) {
            getCurrentStep().setRequireInput(true);
        }

        currentStepKey = nextStep;
    }

    public int getOption() {
        Object option = steps.get(StepDefinition.REQUEST_TASK).getInfoCollected();
        if (option != null) {
           return (int) option;
        } else {
           return 0;
        }
    }

    public void clearOption() {
        steps.get(StepDefinition.REQUEST_TASK).clearSelection();
    }
    @Override
    public Collection<Step> getInfoCollectedSteps() {
        return steps.values().stream().filter(Step::needsAccessToUserCollectedInfoFromOtherSteps).collect(Collectors.toList());
    }

    @Override
    public Step getCurrentStep() {
        return steps.get(currentStepKey);
    }

    // This process always has a next step unless the user quits it
    // explicitly by selecting the appropriate option.
    @Override
    public boolean hasNextStep() {
        return !StepDefinition.QUIT.equals(currentStepKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsoleProcess that = (ConsoleProcess) o;
        return Objects.equals(steps, that.steps) && currentStepKey == that.currentStepKey;
    }

    @Override
    public int hashCode() {
        return Objects.hash(steps, currentStepKey);
    }
}
