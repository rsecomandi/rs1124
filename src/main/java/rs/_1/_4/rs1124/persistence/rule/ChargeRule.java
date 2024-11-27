package rs._1._4.rs1124.persistence.rule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "CHARGE_RULE")
public class ChargeRule extends AbstractPersistable<UUID> {

    @Column(name = "TOOL_TYPE")
    @NotEmpty
    private String toolType;
    @Column(name = "DAILY_CHARGE")
    @NotEmpty
    private BigDecimal dailyCharge;
    @Column(name = "WEEKDAY_CHARGE")
    private Boolean weekdayCharge;
    @Column(name = "WEEKEND_CHARGE")
    private Boolean weekendCharge;
    @Column(name = "HOLIDAY_CHARGE")
    private Boolean holidayCharge;

    public ChargeRule() {}

    public ChargeRule(String toolType, BigDecimal dailyCharge, Boolean weekdayCharge, Boolean weekendCharge, Boolean holidayCharge) {
        this.toolType = toolType;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }

    public void setDailyCharge(BigDecimal dailyCharge) {
        this.dailyCharge = dailyCharge;
    }

    public Boolean getWeekdayCharge() {
        return weekdayCharge;
    }

    public void setWeekdayCharge(Boolean weekdayCharge) {
        this.weekdayCharge = weekdayCharge;
    }

    public boolean isFreeOnWeekdays() {
        return getWeekdayCharge() != null && !getWeekdayCharge();
    }

    public Boolean getWeekendCharge() {
        return weekendCharge;
    }

    public void setWeekendCharge(Boolean weekendCharge) {
        this.weekendCharge = weekendCharge;
    }

    public boolean isFreeOnWeekends() {
        return getWeekendCharge() != null && !getWeekendCharge();
    }

    public Boolean getHolidayCharge() {
        return holidayCharge;
    }

    public void setHolidayCharge(Boolean holidayCharge) {
        this.holidayCharge = holidayCharge;
    }

    public boolean isFreeOnHolidays() {
        return getHolidayCharge() != null && !getHolidayCharge();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ChargeRule that = (ChargeRule) o;
        return Objects.equals(toolType, that.toolType) && Objects.equals(dailyCharge, that.dailyCharge) && Objects.equals(weekdayCharge, that.weekdayCharge) && Objects.equals(weekendCharge, that.weekendCharge) && Objects.equals(holidayCharge, that.holidayCharge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), toolType, dailyCharge, weekdayCharge, weekendCharge, holidayCharge);
    }
}
