import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class WindowClient {

    private static boolean running = true;
    private static int bytesRead;
    private static String ServerAddress;
    private static int serverPort = 10809;
    private static Scanner scanner;
    private static Integer windowNumber = 0;

    private static boolean wNumIsEmpty(String serverInput) {
        if (windowNumber == 0) {
            windowNumber = Integer.parseInt(serverInput.substring(0, 1));
            return true;
        }
        return false;
    }

    private static void send(SocketChannel channel, String command) throws IOException {
        String comment = windowNumber.toString() + "号窗口" + command;
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        channel.write(ByteBuffer.wrap(comment.getBytes()));

        bytesRead = channel.read(buffer);

        String serverInput = new String(buffer.array(), 0, buffer.remaining());

        if (bytesRead != -1) {

            if (!wNumIsEmpty(serverInput)) {
                System.out.println(serverInput + " <<<");
            } else {
                System.out.println(serverInput.substring(4, serverInput.length()) + serverInput.substring(0, 4));
            }
        }
        buffer.flip();

        return;
    }

    public static void main(String[] args) {

        scanner = new Scanner(System.in);
        System.out.print("输入服务器IP地址：");
        ServerAddress = scanner.nextLine();

        try (SocketChannel channel = SocketChannel.open(new InetSocketAddress(ServerAddress, serverPort))) {

            System.out.println("已连接服务器！");
            String input = null;
            while (running) {
                if (windowNumber != 0) {
                    System.out.println("<" + windowNumber + "号窗口>");
                }
                System.out.println("======");
                System.out.println("c) 叫号");
                System.out.println("s) 过号");
                System.out.println("q) 退出");
                System.out.println("======");
                System.out.print("：");

                input = scanner.nextLine();

                switch (input) {
                    case "c" -> send(channel, "叫号");

                    case "s" -> send(channel, "过号");

                    case "q" -> {
                        System.out.println("退出");
                        running = false;
                        channel.close();
                        return;
                    }
                    default -> {
                        System.out.println("输入错误，请重新输入。");
                        continue;
                    }
                }

                if (bytesRead == -1) {
                    System.err.println("连接中断，请尝试叫号重新连接......");
                    channel.close();
                }
            }

        } catch (Exception e) {
            System.err.print("连接错误，请重新");
            main(args);
        }

    }
}
