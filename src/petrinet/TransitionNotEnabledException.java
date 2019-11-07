package petrinet;

public class TransitionNotEnabledException extends Exception {
  public TransitionNotEnabledException(String s) {
    super("Transition " + s + " not enabled exception");
  }
}
