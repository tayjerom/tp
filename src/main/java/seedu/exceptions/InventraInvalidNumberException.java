package seedu.exceptions;

public class InventraInvalidNumberException extends RuntimeException {
  private final String invalidNumber;

  public InventraInvalidNumberException(String input) {
   invalidNumber = input;
  }
  @Override
  public String getMessage() {
    return String.format("The input %s could not be parsed as an integer.", invalidNumber);
  }
}
