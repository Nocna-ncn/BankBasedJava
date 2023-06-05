package MainServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class MainServer {

    public static Object lockObject = new Object();
    public static volatile int number = 0;
    public static volatile int windowNumber = 0;
    public static volatile LinkedList<Integer> windowQueue = new LinkedList<>();
    public static volatile LinkedList<Integer> personQueue = new LinkedList<>();

    public static void main(String[] args) throws IOException {

        // The take number machine
        new Thread(new TakeServer()).start();

        System.out.println("No hello world");

        // The windows server running.
        new Thread(new WindowServer()).start();
               
    }

}
