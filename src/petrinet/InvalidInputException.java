package petrinet;

import java.io.InvalidClassException;

public class InvalidInputException extends Exception {

  public InvalidInputException(String s) {
    super("Invalid input: " + s);
  }
}
