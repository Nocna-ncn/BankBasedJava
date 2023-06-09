import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TakeNumber {
    private static String serverAddress;
    private static int serverPort = 10808;

    public static void main(String[] args) {

        try (Scanner input = new Scanner(System.in)) {
            System.out.print("请输入服务器地址：");
            serverAddress = input.nextLine();
            while (true) {
                System.out.println("请回车取号。");
                input.nextLine();

                try (Socket socket = new Socket(serverAddress, serverPort)) {
                    OutputStreamWriter send = new OutputStreamWriter(socket.getOutputStream());
                    send.write("取号机\n");
                    send.flush();

                    // System.out.println("已连接!");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println("服务器返回：" + reader.readLine());

                    reader.close();
                    send.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println("连接失败!");
                    e.printStackTrace();
                }

            }
        }
    }

}
