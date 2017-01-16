
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 监听客户端的链接
 * Created by ply on 2017/1/9.
 */

public class SocketListener extends Thread {
    int count=0;
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(13345);  //端口号，数值范围1-65535，一般数值越大越好
            
            //循环监听客户端发来的socket
            while (true) {
                Socket socket = serverSocket.accept();//accept会阻塞线程，故放在线程中监听
                count++;
                System.out.println("客户端连接"+count);
                
                ChatSocket chatSocket = new ChatSocket(socket);//将socket传递给新的线程处理
                chatSocket.start();
                
                SocketManager.getInstance().addChatSocket(chatSocket);   //将所有连接到服务器的socket放进SocketManager,便于管理和消息的群发
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
