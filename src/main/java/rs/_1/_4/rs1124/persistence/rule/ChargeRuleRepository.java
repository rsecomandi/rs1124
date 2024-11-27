package rs._1._4.rs1124.persistence.rule;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRuleRepository extends JpaRepository<ChargeRule, String> {
    ChargeRule findOneByToolType(String toolType);
}
