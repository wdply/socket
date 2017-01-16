

import java.util.Vector;

/**
 * socket管理
 * Created by ply on 2017/1/9.
 */

public class SocketManager {
    private static final SocketManager socketManager = new SocketManager();
    private Vector<ChatSocket> vector = new Vector<>();//收集所有的客户端socket

    private SocketManager() {
    }

    public static SocketManager getInstance() {
        //创建单例模式的SocketManager对象
        return socketManager;
    }
    
	/**
	 * 添加socket
	 * @param chatSocket
	 */
    public void addChatSocket(ChatSocket chatSocket) {
        vector.add(chatSocket);
    }
    
	/**
	 * 消息群发
	 * @param cs socket对象
	 * @param msg 要发送的消息
	 */
    public  void publish(ChatSocket cs, String msg) {
        for (ChatSocket chatSocket : vector) {
            if (chatSocket != cs) {//除去当前用户，群发其他用户消息
                chatSocket.out(msg);
            }
        }
    }

}
