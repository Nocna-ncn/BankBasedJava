import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class WindowClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入服务器IP地址：");

        String ServerAddress = scanner.nextLine();

        try (SocketChannel channel = SocketChannel.open(new InetSocketAddress(ServerAddress, 10809))) {
            System.out.println("已连接服务器！");
            boolean running = true;
            while (running) {
                String input = "";
                System.out.println("======");
                System.out.println("1) 叫号");
                System.out.println("2) 过号");
                System.out.println("Not 1,2) 退出");
                System.out.println("======");

                input = scanner.nextLine();

                switch (input) {
                    case "1" -> {
                        channel.write(ByteBuffer.wrap("叫号".getBytes()));
                        System.out.println("你已叫号，号码为：");
                    }
                    case "2" -> {
                        channel.write(ByteBuffer.wrap("过号".getBytes()));
                        System.out.println("过号");
                    }
                    default -> {
                        System.out.println("退出");
                        running = false;
                        channel.close();
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
