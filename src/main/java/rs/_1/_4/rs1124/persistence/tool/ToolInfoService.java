package rs._1._4.rs1124.persistence.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolInfoService {
  private final ToolInfoRepository repository;

  @Autowired
  public ToolInfoService(ToolInfoRepository repository) {
    this.repository = repository;
  }

  public ToolInfo getToolInfoByToolCode(String toolCode) {
    return repository.findOneByToolCode(toolCode);
  }
}
