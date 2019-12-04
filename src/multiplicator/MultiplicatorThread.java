package multiplicator;

import petrinet.PetriNet;
import petrinet.Transition;

import java.util.Collection;

public class MultiplicatorThread implements Runnable {
  private final Collection<Transition<Integer>> transitions;
  private final PetriNet<Integer> petriNet;
  private int counter = 0;

  public MultiplicatorThread(Collection<Transition<Integer>> transitions, PetriNet<Integer> petriNet) {
    this.transitions = transitions;
    this.petriNet = petriNet;
  }

  @Override
  public void run() {
    while(true) {
      try {
        petriNet.fire(transitions);
        counter++;
      }
      catch (InterruptedException e) {
        System.out.println("Thread interrupted. Fired transitions: " + counter);
        Thread.currentThread().interrupt();
        return;
      }
    }
  }
}
