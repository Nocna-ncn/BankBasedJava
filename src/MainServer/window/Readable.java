package MainServer.window;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Readable implements Runnable {

    private final SocketChannel channel;

    public Readable(SocketChannel channel) {
        this.channel = channel;
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

            buffer.flip();

            String input = new String(buffer.array(), 0, buffer.remaining());

            if (!input.trim().isEmpty()) {
                System.out.println("接收到数据：" + input);                
                System.out.println("已" + new String(buffer.array(), 0, buffer.remaining()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
