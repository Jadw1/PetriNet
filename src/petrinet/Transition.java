package petrinet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Transition<T> {
  private Map<T, Integer> inputs;
  private Collection<T> resets;
  private Collection<T> inhibitor;
  private Map<T, Integer> outputs;

  public Transition(Map<T, Integer> inputs, Collection<T> resets, Collection<T> inhibitors, Map<T, Integer> outputs) {
    this.inputs = inputs; //TODO: validate
    this.resets = resets; //TODO: can inputs and resets have intersection?
    this.inhibitor = inhibitors;
    this.outputs = outputs;
  }

  public boolean isEnabled(Map<T, Integer> marking) {
    for(Map.Entry<T, Integer> arc : inputs.entrySet()) {
      Integer tokens = marking.get(arc.getKey());

      if(tokens == null) {
        return false;
      }
      else if(tokens < arc.getValue()) {
        return false;
      }
    }

    return validateInhibitors(marking);
  }

  public Map<T, Integer> fire(Map<T, Integer> marking) throws TransitionNotEnabledException {
    if(!validateInhibitors(marking)) {
      throw new TransitionNotEnabledException(this.toString());
    }

    Map<T, Integer> newMarking = new HashMap<>(marking);
    for(Map.Entry<T, Integer> arc : inputs.entrySet()) {
      Integer tokens = newMarking.get(arc.getKey());

      if(tokens == null) {
        throw new TransitionNotEnabledException(this.toString());
      }

      int newTokens = tokens - arc.getValue();
      if(newTokens < 0) {
        throw new TransitionNotEnabledException(this.toString());
      }

      if(newTokens == 0) {
        newMarking.remove(arc.getKey());
      }
      else {
        newMarking.put(arc.getKey(), newTokens);
      }
    }

    for(T place : resets) {
      newMarking.remove(place);
    }

    for(Map.Entry<T, Integer> arc : outputs.entrySet()) {
      int tokens = newMarking.getOrDefault(arc.getKey(), 0);
      tokens += arc.getValue();
      newMarking.put(arc.getKey(), tokens);
    }

    return newMarking;
  }

  private boolean validateInhibitors(Map<T, Integer> marking) {
    for(T place : inhibitor) {
      if(marking.containsKey(place)) {
        return false;
      }
    }

    return true;
  }
}
