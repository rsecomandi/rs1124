package rs._1._4.rs1124.service.process;

import org.apache.commons.lang3.StringUtils;
import rs._1._4.rs1124.service.validation.InputValidationException;

import java.util.Collection;
import java.util.Objects;

import static rs._1._4.rs1124.presentation.Reference.*;

public abstract class StepTemplate implements Step {
  private final String label;

  // All fields above are required, and all fields below are optional.
  private final String userPrompt;
  private final String validationErrorMessage;
  private final String successMessage;
  private boolean requireInput;
  private final boolean subProcess;
  private boolean abort;
  private Object infoCollected;

  public static class Builder {
    private final String label;
    private String userPrompt;
    private String validationErrorMessage;
    private String successMessage = GENERIC_CONTINUE;
    private boolean subProcess = false;

    public Builder(String label) {
      this.label = label;
    }

    public Builder userPrompt(String value) {
      userPrompt = value;
      return this;
    }
    public Builder validationErrorMessage(String value) {
      validationErrorMessage = value;
      return this;
    }
    public Builder successMessage(String value) {
      successMessage = value;
      return this;
    }
    public Builder subProcess(boolean value) {
      subProcess = value;
      return this;
    }
  }

  protected StepTemplate(Builder builder) {
    this.label = builder.label;
    if (StringUtils.isEmpty(label)) {
      throw new IllegalStateException(ERROR_PROCESS_CONFIG_GENERIC);
    }
    this.userPrompt = builder.userPrompt;
    this.validationErrorMessage = builder.validationErrorMessage;
    this.successMessage = builder.successMessage;
    this.requireInput = StringUtils.isNotEmpty(builder.userPrompt);
    this.subProcess = builder.subProcess;
    this.abort = false;
    this.infoCollected = null;
  }

  @Override
  public String doStep(String input) throws InputValidationException {
    if (isRequireInput()) {
      if (StringUtils.isEmpty(input)) {
        return getUserPrompt();
      } else {
        input = sanitizeInput(input);
        validateInput(input);
        processInput(input);
        return produceOutput();
      }
    } else {
      doWork();
      return getSuccessMessage();
    }
  }

  @Override
  public ProcessFlow getStepSubProcess() {
    return null;
  }
  public String sanitizeInput(String input) {
    return StringUtils.trim(input);
  }
  public abstract void validateInput(String input) throws InputValidationException;
  public abstract void processInput(String input);
  public String produceOutput() {
    return getSuccessMessage();
  }
  public void doWork() {
    // nothing by default.
  }

  @Override
  public Step getClone() {
    try {
      return (Step) this.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(ERROR_GENERIC_UNRECOVERABLE);
    }
  }

  @Override
  public String getUserPrompt() {
    return userPrompt;
  }

  public String getValidationErrorMessage() {
    return validationErrorMessage;
  }

  public String getSuccessMessage() {
    return successMessage;
  }

  @Override
  public boolean isRequireInput() {
    return requireInput;
  }

  @Override
  public void setRequireInput(boolean requireInput) {
    this.requireInput = requireInput;
  }

  @Override
  public boolean isSubProcess() {
    return subProcess;
  }

  @Override
  public Object getInfoCollected() {
    return infoCollected;
  }

  @Override
  public boolean hasInfoCollected() {
    return infoCollected != null;
  }

  @Override
  public boolean needsAccessToUserCollectedInfoFromOtherSteps() {
    return false;
  }

  @Override
  public void processUserCollectedInfoFromOtherSteps(Collection<Step> steps) {
    // nothing by default.
  }

  public void setInfoCollected(Object infoCollected) {
    this.infoCollected = infoCollected;
  }

  @Override
  public void clearSelection() {
    this.infoCollected = null;
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public boolean isAbort() {
    return abort;
  }

  public void setAbort(boolean abort) {
    this.abort = abort;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StepTemplate that = (StepTemplate) o;
    return requireInput == that.requireInput && subProcess == that.subProcess && abort == that.abort && Objects.equals(label, that.label) && Objects.equals(userPrompt, that.userPrompt) && Objects.equals(validationErrorMessage, that.validationErrorMessage) && Objects.equals(successMessage, that.successMessage) && Objects.equals(infoCollected, that.infoCollected);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, userPrompt, validationErrorMessage, successMessage, requireInput, subProcess, abort, infoCollected);
  }
}
