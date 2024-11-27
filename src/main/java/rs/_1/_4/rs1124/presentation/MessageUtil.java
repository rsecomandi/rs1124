package rs._1._4.rs1124.presentation;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static rs._1._4.rs1124.presentation.Reference.DISPLAY_DATE_FORMAT;

public class MessageUtil {
  public enum Color {
    RESET("\u001B[0m"),
    BLACK ("\u001B[30m"),
    GREEN ("\u001B[32m"),
    OLIVE ("\033[38;2;213;237;78m"),
    YELLOW ("\u001B[33m"),
    HONEY("\033[38;2;252;235;149m"),
    CYAN ("\u001B[36m"),
    BLUE ("\u001B[34m"),
    INDIGO("\033[38;2;68;82;143m"),
    PURPLE ("\u001B[35m"),
    FUCHSIA("\033[38;2;138;17;85m"),
    RED("\u001B[31m"),
    WHITE ("\u001B[37m"),
    GREY ("\033[38;2;115;115;115m");
    private final String value;
    Color(String v) { value = v; }

    @Override
    public String toString() {
      return value;
    }
  }

  public static void printMessage(String message) {
    System.out.println(paint(message, Color.HONEY));
  }
  public static void printError(String message) {
    System.out.println(paint(message, Color.RED));
  }

  public static String formatDate(LocalDate checkoutDate) {
    // REQ: date must be formatted as follows: : mm/dd/yy
    return DateTimeFormatter.ofPattern(DISPLAY_DATE_FORMAT, Locale.US).format(checkoutDate);
  }

  public static String formatCurrency(BigDecimal value) {
    // REQ: currency must be formatted as follows: $9.999,99
    return NumberFormat.getCurrencyInstance(Locale.US).format(value);
  }

  public static String formatPercent(Integer value) {
    // REQ: percent must be formatted as follows: 99%
    return value.toString() + '%';
  }

  public static String paint(String message, Color color) {
    return color + message + Color.RESET;
  }

  public static String getTitleBlock(String title) {
    return System.lineSeparator() + System.lineSeparator() +
        StringUtils.repeat('/', 8) + StringUtils.repeat('*', 24) + StringUtils.repeat('\\', 8)
        + System.lineSeparator() + "|||" + StringUtils.center(title.toUpperCase(), 32 + 2, ' ') + "|||" + System.lineSeparator()
        + StringUtils.repeat('/', 8) + StringUtils.repeat('*', 24) + StringUtils.repeat('\\', 8)
        + System.lineSeparator();
  }
}
