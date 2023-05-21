import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TakeNumber {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("请回车取号。");
            input.nextLine();

            try (Socket socket = new Socket("localhost", 10808)) {
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
