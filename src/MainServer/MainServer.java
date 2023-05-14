package MainServer;

public class MainServer {

    public static Object lockObject = new Object();
    public static volatile int number = 0;

    public static void main(String[] args) {

        // The take number machine
        new Thread(new TakeServer()).start();

        
        System.out.println("No hello world");
    }

}
