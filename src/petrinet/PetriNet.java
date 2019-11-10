package petrinet;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class PetriNet<T> {
  private Map<T, Integer> places;

  private final ReentrantLock lock;

  public PetriNet(Map<T, Integer> initial, boolean fair) {
    this.places = initial; //TODO: deep copy?

    this.lock = new ReentrantLock(fair);
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

  public Transition<T> fire(Collection<Transition<T>> transitions) throws InterruptedException {
    Transition<T> result = null;
    while(result == null) {
      lock.lock();
      result = tryFire(transitions);
      lock.unlock();
    }

    return result;
  }

  private Transition<T> tryFire(Collection<Transition<T>> transitions) {
    for(Transition<T> transition : transitions) {
      if(transition.isEnabled(places)) {
        try {
          transition.fire(places);
          return transition;
        }
        catch (TransitionNotEnabledException e) {
          return null;
        }
      }
    }

    return null;
  }
}
