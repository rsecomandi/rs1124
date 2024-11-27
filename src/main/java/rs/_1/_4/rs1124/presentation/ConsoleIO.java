package rs._1._4.rs1124.presentation;

import static rs._1._4.rs1124.Rs1124Application.logger;
import static rs._1._4.rs1124.presentation.Reference.*;

import java.util.Scanner;

import rs._1._4.rs1124.service.process.BasicProcess;
import rs._1._4.rs1124.service.process.BasicProcessFactory;
import rs._1._4.rs1124.service.validation.InputValidationException;

public class ConsoleIO {

  public static void start() {
    MessageUtil.printMessage(TITLE_SCREEN_BLOCK);

    try (Scanner scanner = new Scanner(System.in)) {
      startProcess(BasicProcessFactory.getNewProcess(BasicProcessFactory.ProcessType.CONSOLE), scanner);
    } catch (Exception e) {
      MessageUtil.printError(ERROR_INPUT_GENERIC);
      throw e;
    }
  }

  public static void startProcess(BasicProcess basicProcess, Scanner scanner) {
    String userInput = null;
    while (!basicProcess.isComplete()) {
      // if this step is a sub-process, recursively call this method to treat it like a process.
      if (basicProcess.getCurrentStep().isSubProcess()) {
        startProcess((BasicProcess) basicProcess.getCurrentStep().getStepSubProcess(), scanner);
        basicProcess.advance();
      } else {
        try {
          MessageUtil.printMessage(basicProcess.processCurrentStep(userInput));
        } catch (InputValidationException e) {
          MessageUtil.printError(e.getMessage());
          MessageUtil.printMessage(basicProcess.getCurrentUserPrompt());
        }
        userInput = null;
        if (basicProcess.isRequireInput()) {
          userInput = scanner.nextLine();
        } else if (!basicProcess.isComplete()) {
          basicProcess.advance();
          logger.debug("Process flow not complete.");
        }
      }
    }
  }
}
