package MainServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TakeServer implements Runnable {

    private Integer number;

    private String command;

    String tempString;

    @Override
    public void run() {

        try (ServerSocket server = new ServerSocket(10808)) {

            while (true) {
                Socket socket = server.accept();
                System.out.println("取号客户端连接：" + socket.getInetAddress().getHostName());
                OutputStreamWriter send = new OutputStreamWriter(socket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // System.out.println("接受到客户端数据：" + reader.readLine() + "取号。");
                tempString = null;

                while (tempString == null) {
                    tempString = reader.readLine();
                }
                command = tempString.substring(0, 1);

                System.out.println("read: " + command);
                switch (command) {
                    case "g" -> {
                        synchronized (MainServer.lockObject) {
                            ++MainServer.numberCount;
                            MainServer.personQueue.offer(MainServer.numberCount);

                            send.write("您取的号码是：" + MainServer.numberCount + '\n');
                            send.flush();
                        }
                    }
                    case "s" -> {
                        int numResult = -1;
                        number = Integer.parseInt(tempString.substring(1, tempString.length()));

                        synchronized (MainServer.lockObject) {
                            for (int i = 0; i < MainServer.personQueue.size(); i++) {
                                if (MainServer.personQueue.get(i).equals(number)) {
                                    numResult = i + 1;
                                    send.write("此号码目前排在第" + numResult + "位。" + '\n');
                                    send.flush();
                                }
                            }
                            if (numResult == -1) {
                                for (int i = 0; i < MainServer.windowQueue.size(); i++) {
                                    if (MainServer.windowQueue.get(i).equals(number)) {
                                        numResult = i + 1;
                                        send.write("此号码不在等待队列，目前在" + numResult + "号窗口。" + '\n');
                                        send.flush();
                                    }
                                }
                            }
                            if (numResult == -1) {
                                send.write("此号码不存在!\n");
                                send.flush();
                            }
                        }

                    }
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
