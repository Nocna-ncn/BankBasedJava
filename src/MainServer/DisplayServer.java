package MainServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import ObjectTrans.LinkedListTrans;

public class DisplayServer implements Runnable {

    @Override
    public void run() {

        try {
            try (ServerSocket serverSocket = new ServerSocket(10810)) {
                while (true) {
                    System.out.println("显示服务器等待新的连接...");
                    Socket socket = serverSocket.accept();
                    System.out.println("显示服务器与客户端连接已建立，启动新线程处理连接");
                    SocketHandler handler = new SocketHandler(socket);
                    Thread thread = new Thread(handler);
                    thread.start(); // 启动新线程处理连接
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
        }

        @Override
        public void run() {
            try {

                while (true) {
                    synchronized (lockObject) {
                        lockObject.wait();
                    }
                    synchronized (MainServer.lockObject) {
                        System.out.println("Main p queue: " + MainServer.personQueue);
                        System.out.println("Main w queue: " + MainServer.windowQueue);

                        linkedListTrans.setPersonList(MainServer.personQueue);
                        linkedListTrans.setWindowList(MainServer.windowQueue);

                        objectOutputStream.writeObject(linkedListTrans);
                        objectOutputStream.flush();
                        objectOutputStream.reset();

                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

