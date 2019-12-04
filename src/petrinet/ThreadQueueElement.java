package petrinet;

import java.util.Collection;
import java.util.concurrent.Semaphore;

public class ThreadQueueElement<T> {
  public final Semaphore semaphore;
  public final Collection<Transition<T>> transitions;
  public Transition<T> enabled;

  public ThreadQueueElement(Collection<Transition<T>> transitions) {
    this.transitions = transitions;
    this.semaphore = new Semaphore(0);
    this.enabled = null;
  }
}
