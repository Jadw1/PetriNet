import petrinet.PetriNet;
import petrinet.Transition;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Map<String, Integer> places = new HashMap<>();
        places.put("a", 1);
        PetriNet<String> net = new PetriNet<>(places, false);


        ArrayList<Transition<String>> transitions = new ArrayList<>();

        Map<String, Integer> input1 = new HashMap<>();
        Map<String, Integer> output1 = new HashMap<>();
        Collection<String> reset1 = new ArrayList<>();
        Collection<String> inhibitors1 = new ArrayList<>();
        input1.put("a", 1);
        output1.put("b", 1);
        transitions.add(new Transition<>(input1, reset1, inhibitors1, output1));

        Map<String, Integer> input2 = new HashMap<>();
        Map<String, Integer> output2 = new HashMap<>();
        Collection<String> reset2 = new ArrayList<>();
        Collection<String> inhibitors2 = new ArrayList<>();
        input2.put("b", 1);
        output2.put("a", 1);
        transitions.add(new Transition<>(input2, reset2, inhibitors2, output2));

        Set<Map<String, Integer>> res = net.reachable(transitions);
    }
}
