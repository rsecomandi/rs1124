package rs._1._4.rs1124.service.process;

import rs._1._4.rs1124.service.validation.InputValidationException;

import java.util.Collection;

public interface Step extends Cloneable {

    // Define what is required to accomplish the step here.
    String doStep(String input) throws InputValidationException;

    // Implement a way to indicate whether this step is a sub-process.
    boolean isSubProcess();

    // Implement a way to get a sub-process from a step if there is one.
    ProcessFlow getStepSubProcess();

    // Implement a way to retrieve the user prompt associated with this step.
    String getUserPrompt();

    // Implement a way to indicate whether this step requires user input.
    boolean isRequireInput();
    void setRequireInput(boolean b);

    // Implement a way to indicate whether this step needs to access info collected by other steps.
    boolean needsAccessToUserCollectedInfoFromOtherSteps();

    // Implement a way to retrieve and process information from other steps.
    void processUserCollectedInfoFromOtherSteps(Collection<Step> steps);

    // Implement a way to retrieve information collected by the step.
    Object getInfoCollected();
    boolean hasInfoCollected();

    // Implement a way to clear information collected by the step.
    void clearSelection();

    // Implement a way to retrieve a label for the step.
    String getLabel();

    // Implement a way to ask the step if the process should be terminated.
    boolean isAbort();

    // Implement a way to clone this step following the Prototype pattern.
    Step getClone();
}
