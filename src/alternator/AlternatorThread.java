package alternator;

import petrinet.PetriNet;
import petrinet.Transition;

import java.util.ArrayList;
import java.util.Collection;

public class AlternatorThread implements Runnable {

  private final String name;
  private final PetriNet<String> petriNet;
  private final Collection<Transition<String>> initialTransitions;
  private final Collection<Transition<String>> endingTrasitions;

  public AlternatorThread(String name, PetriNet<String> petriNet, Transition<String> initialTransition, Transition<String> endingTransitin) {
    this.name = name;
    this.petriNet = petriNet;
    this.initialTransitions = new ArrayList<>();
    this.initialTransitions.add(initialTransition);
    this.endingTrasitions = new ArrayList<>();
    this.endingTrasitions.add(endingTransitin);
  }

  @Override
  public void run() {
    while(true) {
      try {
        initialProtocol();
        criticalSection();
        endingProtocol();
      }
      catch (InterruptedException e) {
        System.out.println("Thread " + name + " interrupted.");
        Thread.currentThread().interrupt();
        return;
      }
    }
  }

  private void initialProtocol() throws InterruptedException {
    petriNet.fire(initialTransitions);
  }

  private void endingProtocol() throws InterruptedException {
    petriNet.fire(endingTrasitions);
  }

  private void criticalSection() {
    System.out.print(name);
    System.out.print(".");
  }
}
