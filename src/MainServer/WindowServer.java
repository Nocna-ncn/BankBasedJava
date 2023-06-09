package MainServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import MainServer.window.*;

public class WindowServer implements Runnable, Closeable {

    private final ServerSocketChannel serverChannel;
    private final Selector selector;

    public WindowServer() throws IOException {
        this.serverChannel = ServerSocketChannel.open();
        this.selector = Selector.open();
    }

    @Override
    public void run() {

        try {
            serverChannel.bind(new InetSocketAddress(10809));

            serverChannel.configureBlocking(false);

            serverChannel.register(selector, SelectionKey.OP_ACCEPT, new Acceptable(serverChannel, selector));

            while (true) {
                int count = selector.select();
                System.out.println("窗口服务器监听到" + count + "个事件。");

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    this.dispatch(key);
                    iterator.remove();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void dispatch(SelectionKey key) throws IOException {
        Object temp = key.attachment();

        if (temp instanceof Runnable) {
            // System.out.println("事件run()");
            ((Runnable) temp).run();
        }

    }

    @Override
    public void close() throws IOException {
        serverChannel.close();
        selector.close();
    }

}
