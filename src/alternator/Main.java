package alternator;

import petrinet.PetriNet;
import petrinet.Transition;

import java.util.*;

/*
 * Każdy wątek ma swoją nazwe name.
 * Miejsce name0 reprezentuje wątek poza sekcją krytyczną.
 * Miejsce name1 reprezentuje wątek w sekcji krytycznej.
 * Miejsce nameGUARD reprezentuje strażnika by wątek nie był 2 razy pod rząd w sekcji krytycznej.
 *
 * Miejsce Section reprezentuje sekcje krytyczną.
 */
public class Main {

  private static final Map<String, Collection<String>> resetMap = new HashMap<>();
  private static Collection<Transition<String>> allTransitions = new ArrayList<>();

  private static Thread createThread(String name, Map<String, Integer> initialMarking, PetriNet<String> petriNet) {
    initialMarking.put(name + "0", 1);

    Map<String, Integer> t1Input = new HashMap<>();
    t1Input.put("Section", 1);
    t1Input.put(name + "0", 1);

    Map<String, Integer> t1Output = new HashMap<>();
    t1Output.put(name + "1", 1);

    Collection<String> t1Inhibitors = new ArrayList<>();
    t1Inhibitors.add(name + "GUARD");

    Transition<String> t1 = new Transition<>(t1Input, new ArrayList<>(), t1Inhibitors, t1Output);


    Map<String, Integer> t2Input = new HashMap<>();
    t2Input.put(name + "1", 1);

    Map<String, Integer> t2Output = new HashMap<>();
    t2Output.put(name + "0", 1);
    t2Output.put("Section", 1);
    t2Output.put(name + "GUARD", 1);

    Collection<String> t2Resets = new ArrayList<>();
    for(Map.Entry<String, Collection<String>> entry : resetMap.entrySet()) {
      t2Resets.add(entry.getKey() + "GUARD");
      entry.getValue().add(name + "GUARD");
    }
    resetMap.put(name, t2Resets);

    Transition<String> t2 = new Transition<>(t2Input, t2Resets, new ArrayList<>(), t2Output);


    allTransitions.add(t1);
    allTransitions.add(t2);
    return new Thread(new AlternatorThread(name, petriNet, t1, t2));
  }

  public static void checkMarkings(Set<Map<String, Integer>> markings) {
    for(Map<String, Integer> marking: markings) {
      int inCriticalSection = 0;

      for(Map.Entry<String, Integer> place: marking.entrySet()) {
        if(place.getKey().endsWith("1") && place.getValue() > 0) {
          inCriticalSection++;
        }
      }

      if(inCriticalSection > 1) {
        System.err.println("Invalid marking: " + marking.toString());
      }
    }
  }

  public static void main(String[] args) {
    Map<String, Integer> initialMarking = new HashMap<>();
    initialMarking.put("Section", 1);
    PetriNet<String> petriNet = new PetriNet<>(initialMarking, true);

    Thread A = createThread("A", initialMarking, petriNet);
    Thread B = createThread("B", initialMarking, petriNet);
    Thread C = createThread("C", initialMarking, petriNet);

    Set<Map<String, Integer>> allMarkings = petriNet.reachable(allTransitions);
    System.out.println(allMarkings.size() + " markings reachable.");
    checkMarkings(allMarkings);

    A.start();
    B.start();
    C.start();

    try {
      Thread.sleep(30000);
    }
    catch (InterruptedException e) {
      System.out.println("Main thread interrupted.");
      Thread.currentThread().interrupt();
    }
    finally {
      A.interrupt();
      B.interrupt();
      C.interrupt();
    }
  }
}
