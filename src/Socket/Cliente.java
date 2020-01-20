package Socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		String infor = "";
		Socket s = null;
	    	try{
		    	int serverPort =Integer.parseInt(args[1]);
			   	s = new Socket(args[0], serverPort);
			   	DataInputStream in = new DataInputStream( s.getInputStream());
				DataOutputStream out = new DataOutputStream( s.getOutputStream());
				if(args.length == 5) {
					 infor = args[2]+" "+args[3]+" "+args[4];
				}else if(args.length == 4){
					infor = args[2]+" "+args[3];
				}else {
					infor = args[2];
				}
				String esc[] = infor.split(" ");
				if(esc[0].equals("put")){
					out.writeUTF(infor);        	
					String result = in.readUTF();
					try {
						File f = new File(System.getProperty("user.home")+"/"+esc[1]);
						FileInputStream ins = new FileInputStream(f);
						OutputStreamWriter osw = new OutputStreamWriter(out);
						BufferedWriter writer = new BufferedWriter(osw);
						writer.write(f.getName()+"\n");
						writer.flush();
						int c;
						while ((c = in.read()) != -1) {
						System.out.println(c);
						out.write(c);
						 }
						
					} catch (Exception e) {
						System.out.println("Erro: "+e);
					}finally {
						out.close();
					}
				} else { 
					out.writeUTF(infor);        	
					String result = in.readUTF();
					System.out.println(result);
				}
		    } catch (UnknownHostException e){
		    	System.out.println("Sock:"+e.getMessage()); 
		    } catch (EOFException e){
		    	System.out.println("EOF:"+e.getMessage());
		    } catch (IOException e){
		    	System.out.println("IO:"+e.getMessage());
			}finally {
				s.close();
			}
	}
}