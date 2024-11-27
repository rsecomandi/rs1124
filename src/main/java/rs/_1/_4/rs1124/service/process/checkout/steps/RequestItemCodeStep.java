package rs._1._4.rs1124.service.process.checkout.steps;

import static rs._1._4.rs1124.Main.getToolInfoService;

import rs._1._4.rs1124.persistence.tool.ToolInfo;
import rs._1._4.rs1124.service.process.StepTemplate;
import rs._1._4.rs1124.service.validation.InputValidationException;

public class RequestItemCodeStep extends StepTemplate {

  private ToolInfo toolInfo;
  public RequestItemCodeStep(Builder builder) {
    super(builder);
  }

  @Override
  public void validateInput(String input) throws InputValidationException {
    ToolInfo matchedToolInfo = getToolInfoService().getToolInfoByToolCode(input);
    if (matchedToolInfo == null) {
      throw new InputValidationException(getValidationErrorMessage());
    } else {
      setToolInfo(matchedToolInfo);
      setRequireInput(false);
    }
  }

  @Override
  public void processInput(String input) {
    setInfoCollected(getToolInfo());
  }

  public ToolInfo getToolInfo() {
    return toolInfo;
  }

  public void setToolInfo(ToolInfo toolInfo) {
    this.toolInfo = toolInfo;
  }

  @Override
  public Object clone() {
    return new RequestItemCodeStep(new StepTemplate.Builder(getLabel())
            .userPrompt(getUserPrompt())
            .validationErrorMessage(getValidationErrorMessage())
            .successMessage(getSuccessMessage())
            .subProcess(isSubProcess()));
  }
}
