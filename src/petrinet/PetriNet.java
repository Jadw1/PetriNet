package petrinet;

import java.util.*;

public class PetriNet<T> {
  private boolean fair;
  private Map<T, Integer> places;

  public PetriNet(Map<T, Integer> initial, boolean fair) {
    this.fair = fair;
    this.places = initial; //TODO: deep copy?
  }

  public Set<Map<T, Integer>> reachable(Collection<Transition<T>> transitions) {
    Set<Map<T, Integer>> set = new HashSet<>();

    Stack<Map<T, Integer>> stack = new Stack<>();
    stack.push(places);

    while(!stack.empty()) {
      Map<T, Integer> marking = stack.pop();
      if(set.contains(marking)) {
        continue;
      }

      set.add(marking);
      for(Transition<T> transition : transitions) {
        if(transition.isEnabled(marking)) {
          try {
            stack.push(transition.fire(marking));
          }
          catch(TransitionNotEnabledException e) {}
        }
      }
    }

    return set;
  }
}
