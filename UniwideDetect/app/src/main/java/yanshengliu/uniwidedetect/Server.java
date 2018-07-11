package yanshengliu.uniwidedetect;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ylxh5 on 10/11/17.
 */

public class Server {
    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(20001);
        Socket client= null;
        boolean f= true;
        System.out.println("server is running!");
        while(f){
            client=server.accept();
            System.out.println("yes you connected!");
            new Thread(new ServerThread(client)).start();
            System.out.println("thread started!!!");
            String temp=client.toString();
            System.out.println(temp);
        }
        server.close();
    }
}
