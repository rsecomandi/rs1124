package rs._1._4.rs1124.service.calculators;

import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static rs._1._4.rs1124.Main.*;
import static rs._1._4.rs1124.presentation.Reference.*;
import static rs._1._4.rs1124.service.calculators.HolidayCalculator.RuleSegment.*;

public class HolidayCalculator {

    enum Holiday {
        FOURTH_OF_JULY("julyfourth"),
        LABOR_DAY("laborday");

        private final String propertyName;
        Holiday(String value) {
            propertyName = value;
        }

        public String getPropertyName() {
            return propertyName;
        }
    }

    public Set<LocalDate> getAllUpcomingHolidays() {
        Set<LocalDate> allUpcomingHolidays = new HashSet<>();
        Arrays.stream(Holiday.values()).forEach(holiday -> allUpcomingHolidays.add(getAdjustedHoliday(holiday, false)));
        return allUpcomingHolidays;
    }

    public LocalDate getAdjustedHoliday(Holiday holiday, boolean isTryingNextYear) {
        String rule = getHolidayRule(holiday.getPropertyName());
        List<RuleSegment> ruleSegments = parseRule(rule);

        LocalDate date;
        if (isTryingNextYear) {
            date = getToday().plusYears(1);
        } else {
            date = getToday();
        }

        for (RuleSegment ruleSegment : ruleSegments) {
            try {
                String ruleValue = ruleSegment.fullInstruction.getValue();
                date = switch (ruleSegment.fullInstruction.getKey()) {
                    // MXX (go to month)
                    case GO_TO_MONTH -> date.withMonth(Integer.parseInt(ruleValue));
                    // DXX (go to day)
                    case GO_TO_DAY -> date.withDayOfMonth(Integer.parseInt(ruleValue));
                    // MONDAY:N (go to nth occurrence of weekday)
                    case GO_TO_DAY_OF_WEEK -> {
                        String[] ruleValueParts = ruleValue.split(CONDITIONAL_DELIMITER);
                        LocalDate adjustedDate = date.with(DayOfWeek.valueOf(ruleValueParts[0]));
                        if (adjustedDate.isBefore(date)) {
                            adjustedDate = adjustedDate.plusWeeks(1);
                        }

                        yield adjustedDate.plusWeeks(Integer.parseInt(ruleValueParts[1]) - 1);
                    }
                    // ? X : Y (if condition then strategy)
                    case CONDITIONAL -> executeConditionalSegment(ruleValue, date);
                };
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(ERROR_CALCULATOR_RULE_GENERIC);
            }
        }

        if (date.isEqual(getToday()) || date.isBefore(getToday())) {
            if (isTryingNextYear) {
                throw new IllegalStateException(ERROR_CALCULATOR_RULE_GENERIC);
            }
            return getAdjustedHoliday(holiday, true);
        } else {
            return date;
        }
    }

    private LocalDate executeConditionalSegment(String segmentValue, LocalDate date) {
        String condition = StringUtils.substringBetween(segmentValue, CONDITIONAL_START, CONDITIONAL_DELIMITER);
        String strategy = StringUtils.substringAfter(segmentValue, CONDITIONAL_DELIMITER);
        switch (condition) {
          case WEEKEND_ACRONYM -> {
              if (isWeekend(date)) {
                date = circumventWeekendStrategy(date, strategy);
              }
          } // support for additional rules goes here
          default -> throw new UnsupportedOperationException(ERROR_CALCULATOR_RULE_GENERIC);
        }
        return date;
    }

    public LocalDate circumventWeekendStrategy(LocalDate date, String strategy) {
        switch (strategy) {
            case NEAR -> {
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    date = date.minusDays(1);
                } else {
                    date = date.plusDays(1);
                }
            }
            case NEXT -> {
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    date = date.plusDays(2);
                } else {
                    date = date.plusDays(1);
                }
            }
            case PREVIOUS -> {
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    date = date.minusDays(1);
                } else {
                    date = date.minusDays(2);
                }
            }
        }
        return date;
    }

    public static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private List<RuleSegment> parseRule(String rule) {
        List<RuleSegment> segments = new ArrayList<>();
        Arrays.stream(rule.split(RULE_SPLITTER)).forEach(s -> {
            if (Pattern.matches(REGEX_MONTH_RULE, s)) {
                segments.add(new RuleSegment(RuleSegment.Instruction.GO_TO_MONTH, StringUtils.remove(s, MONTH_PREFIX)));
            } else if (Pattern.matches(REGEX_DAY_RULE, s)) {
                segments.add(new RuleSegment(RuleSegment.Instruction.GO_TO_DAY, StringUtils.remove(s, DAY_PREFIX)));
            } else if (Pattern.matches(REGEX_WEEKDAY_RULE, s)) {
                segments.add(new RuleSegment(RuleSegment.Instruction.GO_TO_DAY_OF_WEEK, s));
            } else if (StringUtils.startsWith(s, CONDITIONAL_START)) {
                segments.add(new RuleSegment(RuleSegment.Instruction.CONDITIONAL, s));
            } else {
                throw new UnsupportedOperationException(ERROR_CALCULATOR_RULE_GENERIC);
            }
        });
        return segments;
    }

    public static class RuleSegment {
        final static String RULE_SPLITTER = "\\.";
        final static String CONDITIONAL_START = "?";
        final static String CONDITIONAL_DELIMITER = ":";
        final static String MONTH_PREFIX = "M";
        final static String DAY_PREFIX = "D";
        final static String WEEKEND_ACRONYM = "WE";
        final static String NEAR = "near";
        final static String NEXT = "next";
        final static String PREVIOUS = "prev";

        enum Instruction {
            GO_TO_MONTH,
            GO_TO_DAY,
            GO_TO_DAY_OF_WEEK,
            CONDITIONAL
        }

        private final AbstractMap.SimpleEntry<Instruction, String> fullInstruction;

        RuleSegment(Instruction instruction, String value) {
            fullInstruction = new AbstractMap.SimpleEntry<>(instruction, value);
        }
    }
}
