package rs._1._4.rs1124.service.process;

import rs._1._4.rs1124.service.validation.InputValidationException;

public class BasicStep extends StepTemplate {

  public BasicStep(Builder builder) {
    super(builder);
  }

  @Override
  public void validateInput(String input) throws InputValidationException {}
  @Override
  public void processInput(String input) {}
}
