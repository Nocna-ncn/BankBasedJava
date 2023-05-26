package MainServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TakeServer implements Runnable {
    @Override
    public void run() {

        try (ServerSocket server = new ServerSocket(10808)) {
            while (true) {
                System.out.println("等待客户端连接......");
                Socket socket = server.accept();
                System.out.println("客户端连接：" + socket.getInetAddress().getHostName());
                OutputStreamWriter send = new OutputStreamWriter(socket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("接受到客户端数据：" + reader.readLine() + "取号。");
                synchronized (MainServer.lockObject) {
                    ++MainServer.number;
                    MainServer.personQueue.offer(MainServer.number);
                    
                    send.write("您的号码是：" + MainServer.number + '\n');
                    send.flush();
                }
                send.close();
                reader.close();
                socket.close();
                System.out.println("关闭连接！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
