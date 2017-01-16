

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 收到客户端发来的socket，进行处理
 * Created by ply on 2017/1/9.
 */
public class ChatSocket extends Thread {
    private Socket socket;

    public ChatSocket(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        try {
        	//读取客户端发来的消息，并群发消息
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            String line=null;
            while ((line=bufferedReader.readLine())!=null){
            	  System.out.println("服务器收到消息： "+line);
                SocketManager.getInstance().publish(this,line);//群发消息
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
/**
 * 消息发送
 * @param out
 */
    public void out(String out) {
        try {
            socket.getOutputStream().write(out.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
