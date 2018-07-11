package yanshengliu.uniwidedetect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class ServerThread implements Runnable{

    private Socket client = null;
    public ServerThread(Socket client){
        this.client= client;
    }
    @Override
    public void run(){
        try{
            System.out.println("Enter the thread!");
            System.out.println(client.toString());
            PrintStream out = new PrintStream(client.getOutputStream());
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            boolean flag=true;
            while(flag){
                String string=buf.readLine();
                out.println("From Server: "+string);
            }
            out.close();
            client.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
