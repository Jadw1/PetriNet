import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, Integer> m1 = new HashMap<>();
        m1.put("test", 4);

        int a = m1.get("test");
        //int b = m1.get("rrr");
        Integer c = m1.get("qwe");

        System.out.println("Hello World!");
    }
}
