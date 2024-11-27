package rs._1._4.rs1124;

import static rs._1._4.rs1124.presentation.Reference.DEFAULT_DATE_FORMAT;
import static rs._1._4.rs1124.presentation.Reference.LOG_GENERIC_DATE_PARSE_ERROR;
import static rs._1._4.rs1124.presentation.Reference.LOG_GENERIC_LOAD_RESOURCE_ERROR;
import static rs._1._4.rs1124.presentation.Reference.PROPERTIES;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import rs._1._4.rs1124.persistence.rule.ChargeRuleService;
import rs._1._4.rs1124.persistence.tool.ToolInfoService;
import rs._1._4.rs1124.presentation.ConsoleIO;

@SpringBootApplication
public class Rs1124Application {
	public static final Logger logger = LoggerFactory.getLogger(Rs1124Application.class);
	private static final DateTimeFormatter defaultDateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT, Locale.US);
	private static final Map<String, String> holidayRules = new HashMap<>();
	private static LocalDate today;
	private static ConfigurableApplicationContext applicationContext;


	public static void main(String[] args) {
		String todayOverride = null;
		try {
			Properties props = PropertiesLoaderUtils.loadAllProperties(PROPERTIES);

			todayOverride = props.getProperty("today.override");
			today = StringUtils.isNotEmpty(todayOverride) ? LocalDate.parse(todayOverride, defaultDateFormatter) : LocalDate.now();

			for (Map.Entry<Object, Object> entry : props.entrySet()) {
				String key = entry.getKey().toString();
				if (key.startsWith("holiday")) {
					holidayRules.put(StringUtils.removeStart(key, "holiday."), entry.getValue().toString());
				}
			}
		} catch (IOException e) {
			// i.e: "Failed to load application.properties in Rs1124Application.main."
			logger.warn(String.format(LOG_GENERIC_LOAD_RESOURCE_ERROR, PROPERTIES, "Rs1124Application", "main"));
		} catch (DateTimeParseException e) {
			// i.e: "Failed to parse "gibberish" to a date using format "MM/DD/YYYY" in Rs1124Application.main."
			logger.warn(String.format(LOG_GENERIC_DATE_PARSE_ERROR, todayOverride, DEFAULT_DATE_FORMAT, "main"));
			today = LocalDate.now();
		}

		applicationContext = SpringApplication.run(Rs1124Application.class, args);
		ConsoleIO.start();
	}

	public static DateTimeFormatter getDefaultDateFormatter() {
		return defaultDateFormatter;
	}
	public static LocalDate getToday() {
		return today;
	}
	public static void setToday(LocalDate date) {
		today = date;
	}
	public static String getTodayString() {
		return defaultDateFormatter.format(today);
	}

	public static String getHolidayRule(String key) {
		return holidayRules.get(key);
	}

	public static ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static ToolInfoService getToolInfoService() {
		return (ToolInfoService) getApplicationContext().getBean("toolInfoService");
	}

	public static ChargeRuleService getChargeRuleService() {
		return (ChargeRuleService) getApplicationContext().getBean("chargeRuleService");
	}
}
