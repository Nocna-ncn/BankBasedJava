package MainServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import ObjectTrans.LinkedListTrans;

public class DisplayServer implements Runnable {

    public static Object lockObject = new Object();
    private static LinkedList<ObjectOutputStream> outputStreams = new LinkedList<>();

    @Override
    public void run() {
        try {
            try (ServerSocket serverSocket = new ServerSocket(10810)) {
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("显示服务器与客户端连接已建立，启动新线程处理连接");
                    SocketHandler handler = new SocketHandler(socket);
                    Thread thread = new Thread(handler);
                    thread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public class SocketHandler implements Runnable {
        public static Object lockObject = new Object();

        private Socket socket;
        private ObjectOutputStream objectOutputStream;
        private LinkedListTrans linkedListTrans;

        public SocketHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.linkedListTrans = new LinkedListTrans(null, null);
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());

            synchronized (DisplayServer.lockObject) {
                DisplayServer.outputStreams.add(objectOutputStream);
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("Display Server start!");
                    synchronized (MainServer.lockObject) {
                        linkedListTrans.setPersonList(MainServer.personQueue);
                        linkedListTrans.setWindowList(MainServer.windowQueue);

                        // 遍历所有客户端的输出流，将数据发送给每个客户端
                        for (ObjectOutputStream outputStream : DisplayServer.outputStreams) {
                            outputStream.writeObject(linkedListTrans);
                            outputStream.flush();
                            outputStream.reset();
                        }
                    }

                    Thread.sleep(1500);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
