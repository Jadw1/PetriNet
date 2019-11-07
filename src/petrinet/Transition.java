package petrinet;

import java.util.Collection;
import java.util.Map;

public class Transition<T> {
  private Map<T, Integer> inputs;
  private Collection<T> resets;
  private Collection<T> inhibitor;
  private Map<T, Integer> outputs;

  public Transition(Map<T, Integer> inputs, Collection<T> resets, Collection<T> inhibitors, Map<T, Integer> outputs) {
    this.inputs = inputs;
    this.resets = resets;
    this.inhibitor = inhibitors;
    this.outputs = outputs;
  }

  public boolean isEnabled(Map<T, Integer> places) throws InvalidInputException {
    for(Map.Entry<T, Integer> arc : inputs.entrySet()) {
      Integer tokens = places.get(arc.getKey());

      if(tokens == null) {
        throw new InvalidInputException("no place " + arc.getKey().toString());
      }
      if(tokens >= arc.getValue()) {
        return false;
      }
    }

    for(T place : inhibitor) {
      Integer tokens = places.get(place);

      if(tokens == null) {
        throw new InvalidInputException("no place " + place.toString());
      }
      if(tokens != 0) {
        return  false;
      }
    }

    return true;
  }
}
