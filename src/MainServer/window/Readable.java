package MainServer.window;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import MainServer.MainServer;

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

        switch (command) {
            case "叫号" -> {

            }

            case "过号" -> {

            }
        }

        channel.write(ByteBuffer.wrap(
                new String(MainServer.windowNumber + "号窗口" + command + "：" + MainServer.number)
                        .getBytes()));
    }

    @Override
    public void run() {

        ByteBuffer buffer = ByteBuffer.allocate(128);
        try {

            int bytesRead = channel.read(buffer);

            if (bytesRead == -1) {
                // 客户端断开连接
                System.out.println("客户端断开连接：" + channel.getRemoteAddress());
                channel.close();
                return;
            }

            String clientInput = new String(buffer.array(), 0, buffer.remaining());

            buffer.flip();

            if (wNumIsEmpty(clientInput)) {
                synchronized (MainServer.lockObject) {
                    ++MainServer.windowNumber;
                    channel.write(ByteBuffer.wrap(
                            new String(MainServer.windowNumber + "号窗口").getBytes()));
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
