import petrinet.PetriNet;
import petrinet.Transition;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static ReentrantLock lock = new ReentrantLock();

    private static class Test implements Runnable {
        String s;
        public Test(String s) {
            this.s = s;
        }

        @Override
        public void run() {
            System.out.println("before lock: " + s);
            lock.lock();
            if(s == "1") {
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {

                }

            }
            System.out.println("after lock: " + s);
            lock.unlock();
            System.out.println("after unlock: " + s);
        }
    }



    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Test("1"));
        Thread t2 = new Thread(new Test("2"));

        lock.lock();
        t1.start();
        t2.start();
        Thread.sleep(2000);
        lock.unlock();



        /*Map<String, Integer> places = new HashMap<>();
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

        Set<Map<String, Integer>> res = net.reachable(transitions);*/
    }
}
