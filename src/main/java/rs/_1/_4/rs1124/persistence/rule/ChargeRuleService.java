package rs._1._4.rs1124.persistence.rule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChargeRuleService {
    private final ChargeRuleRepository repository;

    @Autowired
    public ChargeRuleService(ChargeRuleRepository repository) {
        this.repository = repository;
    }

    public ChargeRule getChargeRuleByToolType(String toolType) {
        return repository.findOneByToolType(toolType);
    }
}
