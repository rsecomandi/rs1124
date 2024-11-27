package rs._1._4.rs1124.service.process;

public abstract class BasicProcess implements ProcessFlow {
  private boolean complete = false;

  public abstract void initializeSteps();

  @Override
  public String processCurrentStep(String userInput) throws IllegalArgumentException {
    Step currentStep = getCurrentStep();
    if (currentStep.needsAccessToUserCollectedInfoFromOtherSteps()) {
      currentStep.processUserCollectedInfoFromOtherSteps(getInfoCollectedSteps());
    }
    String displayMessage = currentStep.doStep(userInput);

    if (currentStep.isAbort() ||
            (!isComplete() && !isRequireInput() && !hasNextStep())) {
      setComplete(true);
    }
    return displayMessage;
  }

  @Override
  public String getCurrentUserPrompt() {
    return getCurrentStep().getUserPrompt();
  }

  public abstract Step getCurrentStep();
  public abstract boolean hasNextStep();

  @Override
  public boolean isRequireInput() {
    return !complete && getCurrentStep().isRequireInput();
  }

  @Override
  public boolean isComplete() {
    return complete;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }
}
