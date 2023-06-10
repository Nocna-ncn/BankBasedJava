import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;
import ObjectTrans.LinkedListTrans;
import ObjectTrans.Window;

public class DisplayClient {

    private static String serverAddress;
    private static LinkedListTrans receivedObject;
    private static int serverPort = 10810;

    public static void main(String[] args) throws UnknownHostException, IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("请输入服务器地址：");
            serverAddress = scanner.nextLine();
        }
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("与服务器连接成功");

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                while (true) {

                receivedObject = (LinkedListTrans) objectInputStream.readObject();

                System.out.println("Received object personlist: " + receivedObject.getPersonList());

                LinkedList<Window> list = (LinkedList<Window>) receivedObject.getWindowList();
                LinkedList<Integer> windowList = new LinkedList<>();

                for (int i = 0; i < list.size(); i++) {
                    windowList.offer(list.get(i).personNumber);
                }

                System.out.println("Received object windowlist: " + windowList);

            }
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}