import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TakeNumber {
    private static String serverAddress;
    private static int serverPort = 10808;
    private static String command = null;
    private static boolean running = true;

    public static void main(String[] args) {

        try (Scanner input = new Scanner(System.in)) {
            serverAddress = null;
            System.out.print("请输入服务器地址：");
            serverAddress = input.nextLine();
            while (running) {
                System.out.println("======");
                System.out.println("g) 取号");
                System.out.println("s) 查号");
                System.out.println("q) 退出");
                System.out.println("======");
                System.out.print("：");

                command = input.nextLine();
                if (command.equals("q")) {
                    System.out.println("退出！");
                    running = false;
                    break;
                }
                if (!command.equals("g") && !command.equals("s")) {
                    System.out.println("输入错误，请重新输入。");

                    continue;
                }

                try (Socket socket = new Socket(serverAddress, serverPort)) {
                    OutputStreamWriter send = new OutputStreamWriter(socket.getOutputStream());

                    if (command.equals("s")) {
                        String number = null;
                        boolean tempRun = true;
                        while (tempRun) {
                            System.out.println("======");
                            System.out.println("请输入需要查询的号码");
                            System.out.println("输入q将退出");
                            System.out.print("：");
                            number = input.nextLine();
                            boolean isNumeric = number.matches("[+-]?\\d*(\\.\\d+)?");
                            if (isNumeric) {
                                command += number;
                                break;
                            } else if (number.equals("q")) {
                                break;
                            }
                        }

                        if (number.equals("q")) {

                            send.close();
                            socket.close();
                            continue;
                        }

                    }

                    send.write(command + "\n");
                    send.flush();

                    // System.out.println("已连接!");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println(">>> " + reader.readLine());

                    reader.close();
                    send.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println("连接失败!");
                    e.printStackTrace();
                }

            }
        }
    }

}
