import java.io.*;
import java.net.*;

public class TCPServer {
	public static void main(String[] args) throws Exception{
		int port = Integer.parseInt(args[0]);
		ServerSocket server= new ServerSocket(port);
		while(true){
			Socket client=null;
			//System.out.println("Server is ready.");
			client = server.accept();
			//System.out.println("Connection succeed!");
			InputStream input=client.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			//***test***

			//String test="";
			/*
			while((test= reader.readLine())!=null){
				System.out.println(test);
				break;
			}
			*/

			//***end_test***
			String line=reader.readLine();
			String url = line.substring(5, line.indexOf("HTTP") -1);
			//output
			try
			{
				input=new FileInputStream(url);
				OutputStream output =client.getOutputStream();
				byte[] buffer = new byte[1024];
				int len=0;
				while((len=input.read(buffer))!=-1){
					output.write(buffer,0,len);
				}
				output.flush();
				client.close();

			}
			catch(Exception e)
			{
				PrintWriter output = new PrintWriter(client.getOutputStream(),true);
				output.println("404 Not Found!");
				client.close();
				continue;
			}


			reader.close();
			//server.close();

		}


	}

}
