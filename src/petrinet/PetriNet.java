package petrinet;

import java.util.*;
import java.util.concurrent.Semaphore;

public class PetriNet<T> {
  private Map<T, Integer> places;

  private final Semaphore mutex;
  private final Queue<ThreadQueueElement<T>> semaphoreQueue;

  public PetriNet(Map<T, Integer> initial, boolean fair) {
    this.places = initial;

    this.mutex = new Semaphore(1, fair);
    this.semaphoreQueue = new ArrayDeque<>();
  }

  public Set<Map<T, Integer>> reachable(Collection<Transition<T>> transitions) {
    Set<Map<T, Integer>> set = new HashSet<>();
    Stack<Map<T, Integer>> stack = new Stack<>();

    Map<T, Integer> clone;
    try {
      mutex.acquire();
      clone = new HashMap<>(places);
    } catch (InterruptedException e) {
      return null;
    }
    stack.push(clone);
    mutex.release();

    while (!stack.empty()) {
      Map<T, Integer> marking = stack.pop();
      if (marking == null) {
        continue;
      }
      if (set.contains(marking)) {
        continue;
      }

      set.add(marking);
      for (Transition<T> transition : transitions) {
        if (transition.isEnabled(marking)) {
          stack.push(transition.fire(marking));
        }
      }
    }

    return set;
  }

  public Transition<T> fire(Collection<Transition<T>> transitions) throws InterruptedException {
      mutex.acquire();

      Transition<T> enabled = findEnabled(transitions, places);
      if (enabled == null) {
        ThreadQueueElement<T> me = new ThreadQueueElement<>(transitions);
        semaphoreQueue.add(me);
        mutex.release();
        try {
          me.semaphore.acquire();
        }
        catch (InterruptedException e) {
          mutex.release();
          throw e;
        }

        enabled = me.enabled;
      }

      Map<T, Integer> newPlaces = enabled.fire(places);
      if (newPlaces == null) {
        return null;
      }

      places = newPlaces;
      checkQueue();
      return enabled;
  }

  public Map<T, Integer> getActualMarking() {
    return places;
  }

  private void checkQueue() {
    Iterator<ThreadQueueElement<T>> iterator = semaphoreQueue.iterator();

    while (iterator.hasNext()) {
      ThreadQueueElement<T> thread = iterator.next();

      Transition<T> enabled = findEnabled(thread.transitions, places);
      if (enabled != null) {
        iterator.remove();
        thread.enabled = enabled;
        thread.semaphore.release();
        return;
      }
    }

    mutex.release();
  }

  private Transition<T> findEnabled(Collection<Transition<T>> transitions, Map<T, Integer> marking) {
    for (Transition<T> transition : transitions) {
      if (transition.isEnabled(marking)) {
        return transition;
      }
    }

    return null;
  }
}
