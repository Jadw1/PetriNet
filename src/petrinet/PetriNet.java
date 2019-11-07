package petrinet;

import java.util.Map;

public class PetriNet<T> {
  private boolean fair;
  private Map<T, Integer> places;

  public PetriNet(Map<T, Integer> initial, boolean fair) {
    this.fair = fair;
    this.places = initial; //TODO: deep copy?
  }
}
