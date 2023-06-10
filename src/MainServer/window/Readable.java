package MainServer.window;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import MainServer.DisplayServer;
import MainServer.MainServer;
import ObjectTrans.Window;

public class Readable implements Runnable {

    private final SocketChannel channel;

    private static Integer windowNumber;

    public Readable(SocketChannel channel) {
        this.channel = channel;
    }

    private static boolean wNumIsEmpty(String clientInput) {

        windowNumber = Integer.parseInt(clientInput.substring(0, 1));
        if (windowNumber == 0) {
            return true;
        }
        return false;

    }

    private static void whichCmd(SocketChannel channel, String clientInput) throws IOException {

        String command = clientInput.substring(4, 6);

        Integer commandInteger = null;

        switch (command) {
            case "叫号" -> {
                synchronized (MainServer.lockObject) {
                    // MainServer.windowQueue.set(windowNumber - 1, MainServer.personQueue.poll());
                    MainServer.windowQueue.get(windowNumber - 1).personNumber = MainServer.personQueue.poll();
                }
                synchronized (DisplayServer.SocketHandler.lockObject) {
                    DisplayServer.SocketHandler.lockObject.notify();
                }

                commandInteger = MainServer.windowQueue.get(windowNumber - 1).personNumber;

            }

            case "过号" -> {
                synchronized (MainServer.lockObject) {
                    commandInteger = MainServer.windowQueue.get(windowNumber - 1).personNumber;
                    MainServer.personQueue.offer(commandInteger);
                    MainServer.windowQueue.get(windowNumber - 1).personNumber = null;
                }
                synchronized (DisplayServer.SocketHandler.lockObject) {
                    DisplayServer.SocketHandler.lockObject.notify();
                }

            }
        }

        synchronized (MainServer.lockObject) {
            channel.write(ByteBuffer.wrap(
                    new String(
                            MainServer.windowNumber + "号窗口" + command + "："
                                    + commandInteger)
                            .getBytes()));
        }

    }

    @Override
    public void run() {

        ByteBuffer buffer = ByteBuffer.allocate(128);
        try {

            int bytesRead = channel.read(buffer);

            if (bytesRead == -1) {
                // 客户端断开连接
                System.out.println("窗口客户端断开连接：" + channel.getRemoteAddress());
                channel.close();
                return;
            }

            String clientInput = new String(buffer.array(), 0, buffer.remaining());

            buffer.flip();

            if (wNumIsEmpty(clientInput)) {
                synchronized (MainServer.lockObject) {
                    ++MainServer.windowNumber;
                    MainServer.windowQueue.offer(new Window());
                    channel.write(ByteBuffer.wrap(
                            new String(MainServer.windowNumber + "号窗口" + "由于初始无窗口号现服务器已为您分配：").getBytes()));
                    System.out.println("已为客户端分配窗口号：" + MainServer.windowNumber);
                }
            } else {
                whichCmd(channel, clientInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
