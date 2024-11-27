package rs._1._4.rs1124.presentation;

public abstract class Reference {
  // Resources
  public static String PROPERTIES = "application.properties";

  // Default Formats
  public static String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
  public static String DISPLAY_DATE_FORMAT = "MM/dd/yy";
  public static String LABEL_VALUE_SEPARATOR = ": ";
  public static String YES_INPUT = "Y";
  public static String NO_INPUT = "N";
  public static String[] VALID_CONFIRMATION_INPUT = {YES_INPUT, NO_INPUT};

  // Regex
  public static String REGEX_MONTH_RULE = "M\\d{2}";
  public static String REGEX_DAY_RULE = "D\\d{2}";
  public static String REGEX_WEEKDAY_RULE = "(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY):\\d+";


  // Generic Messages and Notifications
  public static String GENERIC_CONTINUE = System.lineSeparator();
  public static String TITLE_SCREEN_BLOCK = MessageUtil.getTitleBlock("RS1124 CHECKOUT SYSTEM");
  public static String TRANSACTION_CANCELED_BLOCK = MessageUtil.getTitleBlock("TRANSACTION CANCELED");
  public static String RENTAL_AGREEMENT_BLOCK = MessageUtil.getTitleBlock("RENTAL AGREEMENT");
  public static String GOODBYE = "Good Bye!!";
  public static String TODAY_IS = "Today is %s.";

  // Labels
  public static String LABEL_TOOL_CODE = "Tool code";
  public static String LABEL_TOOL_TYPE = "Tool type";
  public static String LABEL_TOOL_BRAND = "Tool brand";
  public static String LABEL_RENTAL_DAYS = "Rental days";
  public static String LABEL_CHECKOUT_DATE = "Check out date";
  public static String LABEL_DUE_DATE = "Due date";
  public static String LABEL_CHARGE_DAYS = "Charge days";
  public static String LABEL_DAILY_CHARGE = "Daily rental charge";
  public static String LABEL_PRE_DISCOUNT_TOTAL = "Pre-discount charge";
  public static String LABEL_DISCOUNT_PERCENT = "Discount percent";
  public static String LABEL_DISCOUNT_AMOUNT = "Discount amount";
  public static String LABEL_FINAL_CHARGE = "Final Charge";

  // Prompts
  public static String PROMPT_CONSOLE_SELECT_TASK = "Please select from the following options:"
          + System.lineSeparator() + "1. Begin Checkout"
          + System.lineSeparator() + "2. Change Today's Date"
          + System.lineSeparator() + "3. Exit";
  public static String PROMPT_CONSOLE_CHANGE_DATE = "Enter Today's Date:";
  public static String PROMPT_CHECKOUT_TOOL_CODE_REQ = "Enter Tool Code:";
  public static String PROMPT_CHECKOUT_RENTAL_DAY_REQ = "Enter Rental Day Count:";
  public static String PROMPT_CHECKOUT_DATE_REQ = "Enter Checkout Date:";
  public static String PROMPT_CHECKOUT_DISCOUNT_REQ = "Enter Discount Percent (0-100):";
  public static String PROMPT_CHECKOUT_CONFIRMATION = "Confirm transaction (Y/N):";


  // Error Messages
  public static String ERROR_GENERIC_UNRECOVERABLE = "The system encountered an error and could not recover.";
  public static String ERROR_CALCULATOR_RULE_GENERIC = "There was an error applying a rule.";
  public static String ERROR_PROCESS_CONFIG_GENERIC = "Misconfiguration encountered during attempt to execute current process.";
  public static String ERROR_FILE_LOAD_JSON = "Failed to load JSON data";
  public static String ERROR_INPUT_GENERIC = "There was an error with your input. Please try again.";
  public static String ERROR_INPUT_GENERIC_INVALID_OPTION = "The option you selected is invalid. Please select from the following options:";
  public static String ERROR_INPUT_GENERIC_INVALID_DATE_FORMAT = "The date you entered is invalid. Please enter a date in the following format \"%s\".";
  public static String ERROR_INPUT_INVALID_TOOL_CODE = "The code you provided is not a valid tool code.";
  public static String ERROR_INPUT_INVALID_RENTAL_DAY_COUNT = "Rental day count must be a valid number greater than zero.";
  public static String ERROR_INPUT_INVALID_CHECKOUT_DATE = "Checkout date must be in the format \"%s\" and after today's date (%s).";
  public static String ERROR_INPUT_INVALID_DISCOUNT_PERCENT = "Discount percent must be a number between 0 and 100.";
  public static String ERROR_INPUT_INVALID_CONFIRMATION = "Invalid confirmation response. Please use \"Y\" to confirm or \"N\" to cancel.";
  public static String ERROR_GENERATE_RENTAL_AGREEMENT = "Unable to generate rental agreement.";

  // Logger Messages
  public static String LOG_GENERIC_DATE_PARSE_ERROR = "Failed to parse \"%s\" to a date using format \"%s\" in %s.";
  public static String LOG_GENERIC_LOAD_RESOURCE_ERROR = "Failed to load %s in %s.%s.";
}
