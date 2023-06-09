package MainServer.window;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptable implements Runnable {

    private ServerSocketChannel serverChannel;
    private Selector selector;
    private SocketChannel channel;

    public Acceptable(ServerSocketChannel serverChannel, Selector selector) {
        this.serverChannel = serverChannel;
        this.selector = selector;
    }

    @Override
    public void run() {

        try {
            channel = serverChannel.accept();
            System.out.println("窗口客户端已连接，ip地址为：" + channel.getRemoteAddress());

            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ, new Readable(channel));

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}
