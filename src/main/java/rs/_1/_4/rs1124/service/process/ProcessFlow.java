package rs._1._4.rs1124.service.process;

import java.util.Collection;
public interface ProcessFlow {

  // Define the process-related housekeeping required to
  // consider a step complete, returning a message to be displayed to the user.
  String processCurrentStep(String userInput);

  // Implement a way to retrieve the latest user prompt associated with this process.
  String getCurrentUserPrompt();

  // Define how to move to the next step in the process.
  void advance();

  // Define how to retrieve all steps that have collected info.
  Collection<Step> getInfoCollectedSteps();

  // Define how to determine if the process is complete.
  boolean isComplete();

  // Implement a way to determine if the process requires user input.
  boolean isRequireInput();
}
