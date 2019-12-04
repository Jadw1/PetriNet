package multiplicator;

import petrinet.PetriNet;
import petrinet.Transition;

import java.util.*;

public class Main {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    System.out.print("Enter first number. m = ");
    int m = in.nextInt();
    System.out.print("Enter second number. n = ");
    int n = in.nextInt();

    Map<Integer, Integer> initialMarking = new HashMap<>();
    initialMarking.put(4, 1);
    initialMarking.put(1, m);
    initialMarking.put(2, n);

    PetriNet<Integer> petriNet = new PetriNet<>(initialMarking, true);
    ArrayList<Transition<Integer>> transitions = new ArrayList<>();

    Map<Integer, Integer> t1Input = new HashMap<>();
    t1Input.put(3, 1);
    t1Input.put(4, 1);
    Map<Integer, Integer> t1Output = new HashMap<>();
    t1Output.put(2, 1);
    t1Output.put(4, 1);
    transitions.add(new Transition<>(t1Input, new ArrayList<>(), new ArrayList<>(), t1Output));

    Map<Integer, Integer> t2Input = new HashMap<>();
    t2Input.put(2, 1);
    t2Input.put(5, 1);
    Map<Integer, Integer> t2Output = new HashMap<>();
    t2Output.put(3, 1);
    t2Output.put(5, 1);
    t2Output.put(0, 1);
    transitions.add(new Transition<>(t2Input, new ArrayList<>(), new ArrayList<>(), t2Output));

    Map<Integer, Integer> t3Input = new HashMap<>();
    t3Input.put(5, 1);
    Map<Integer, Integer> t3Output = new HashMap<>();
    t3Output.put(4, 1);
    Collection<Integer> t3Inhibitors = new ArrayList<>();
    t3Inhibitors.add(2);
    transitions.add(new Transition<>(t3Input, new ArrayList<>(), t3Inhibitors, t3Output));

    Map<Integer, Integer> t4Input = new HashMap<>();
    t4Input.put(4, 1);
    t4Input.put(1, 1);
    Map<Integer, Integer> t4Output = new HashMap<>();
    t4Output.put(5, 1);
    Collection<Integer> t4Inhibitors = new ArrayList<>();
    t4Inhibitors.add(3);
    transitions.add(new Transition<>(t4Input, new ArrayList<>(), t4Inhibitors, t4Output));

    Collection<Integer> inhibitors = new ArrayList<>();
    inhibitors.add(1);
    inhibitors.add(5);
    Collection<Transition<Integer>> finalTransitions = new ArrayList<>();
    finalTransitions.add(new Transition<>(new HashMap<>(), new ArrayList<>(), inhibitors, new HashMap<>()));

    ArrayList<Thread> threads = new ArrayList<>();
    for(Transition<Integer> t: transitions) {
      Collection<Transition<Integer>> collection = new ArrayList<>();
      collection.add(t);
      Thread thread = new Thread(new MultiplicatorThread(collection, petriNet));
      threads.add(thread);
      thread.start();
    }

    try {
      petriNet.fire(finalTransitions);
    }
    catch (InterruptedException e) {
      System.out.println("Main thread interrupted.");
      Thread.currentThread().interrupt();
      return;
    }

    for(Thread thread: threads) {
      thread.interrupt();
    }

    Map<Integer, Integer> marking = petriNet.getActualMarking();
    Integer result = marking.get(0);
    if(result != null) {
      System.out.println("Result " + m + " x " + n + " = " + result);
    }
    else {
      System.out.println("Error. Result not defined.");
    }

  }
}
