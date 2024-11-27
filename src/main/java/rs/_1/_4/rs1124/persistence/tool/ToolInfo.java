package rs._1._4.rs1124.persistence.tool;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "TOOL_INFO")
public class ToolInfo extends AbstractPersistable<UUID> {

  @Column(name = "TOOL_CODE")
  @NotEmpty
  private String toolCode;
  @Column(name = "TOOL_TYPE")
  @NotEmpty
  private String toolType;
  @NotEmpty
  private String brand;

  public ToolInfo() {}

  public ToolInfo(String toolCode, String toolType, String brand) {
    this.toolCode = toolCode;
    this.toolType = toolType;
    this.brand = brand;
  }

  public String getToolCode() {
    return toolCode;
  }

  public void setToolCode(String toolCode) {
    this.toolCode = toolCode;
  }

  public String getToolType() {
    return toolType;
  }

  public void setToolType(String toolType) {
    this.toolType = toolType;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ToolInfo toolInfo = (ToolInfo) o;
    return Objects.equals(toolCode, toolInfo.toolCode) && Objects.equals(toolType, toolInfo.toolType) && Objects.equals(brand, toolInfo.brand);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), toolCode, toolType, brand);
  }
}
