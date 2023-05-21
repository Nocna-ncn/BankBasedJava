package MainServer;

import java.io.IOException;

public class MainServer {

    public static Object lockObject = new Object();
    public static volatile int number = 0;
    public static volatile int windowNumber = 0;

    public static void main(String[] args) throws IOException {

        // The take number machine
        new Thread(new TakeServer()).start();
        new Thread(new WindowServer()).start();

        
        System.out.println("No hello world");
    }

}
