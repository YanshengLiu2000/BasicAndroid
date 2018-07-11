package yanshengliu.uniwidedetect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static java.lang.Thread.sleep;


public class Client {
    public static void main(String[] args) throws IOException{
        int check=0;
        Socket client= new Socket("129.94.210.64",20001);
//        Socket client = new Socket("127.0.0.1",20001);
        client.setSoTimeout(500);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = new PrintStream(client.getOutputStream());
        BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
        boolean flag=true;
        while(flag){
            String string ="1";
            out.println(string);
            if("bye".equals(string)){
                flag=false;
            }else{
                try{
                    String echo= buf.readLine();
                    System.out.println(echo);
                }catch(SocketTimeoutException e){
                    if(check==3){
                        System.out.println("Time out,No response");
                        System.out.println();
                    }else{
                        check++;
                    }

                }
            }
        }
        input.close();
        if(client!=null){
            client.close();
        }
    }
}
