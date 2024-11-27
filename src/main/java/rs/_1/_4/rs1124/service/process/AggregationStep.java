package rs._1._4.rs1124.service.process;

import java.util.Collection;

public abstract class AggregationStep extends BasicStep {
    private Collection<Step> userCollectedInfoSteps = null;

    public AggregationStep(Builder builder) {
        super(builder);
    }

    public Collection<Step> getUserCollectedInfoSteps() {
        return userCollectedInfoSteps;
    }

    public void setUserCollectedInfoSteps(Collection<Step> userCollectedInfoSteps) {
        this.userCollectedInfoSteps = userCollectedInfoSteps;
    }

    @Override
    public boolean needsAccessToUserCollectedInfoFromOtherSteps() {
        return true;
    }

    @Override
    public void processUserCollectedInfoFromOtherSteps(Collection<Step> steps) {
        setUserCollectedInfoSteps(steps);
    }
}
