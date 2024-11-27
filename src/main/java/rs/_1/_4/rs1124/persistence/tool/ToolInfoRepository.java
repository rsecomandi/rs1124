package rs._1._4.rs1124.persistence.tool;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolInfoRepository extends JpaRepository<ToolInfo, String> {
  ToolInfo findOneByToolCode(String toolCode);
}
