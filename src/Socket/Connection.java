package Socket;
import java.io.BufferedOutputStream;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	public Connection (Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out = new DataOutputStream( clientSocket.getOutputStream());
			this.start();
		} catch(IOException e)  {
			System.out.println("Connection:"+e.getMessage());}
	}
	@SuppressWarnings({ "resource" })
	public String mainFile(String args) throws IOException {				
		String vetor[]=args.split(" ");
		if(vetor[0].equals("list")) {
			File Diretorio = new File(System.getProperty("user.home"));
			File arquivos[] = Diretorio.listFiles();
			String lista = " ";
			for(File i:arquivos) {
				lista +=i.getName() + " ";
			}return "Arquivos remotos: " + lista;
		}else if(vetor[0].equals("rm")) {
			File Diretorio = new File(System.getProperty("user.home"));
			File arquivos[] = Diretorio.listFiles();
			for(File i:arquivos) {
				if(i.getName().equals(vetor[1])) {
					i.delete();
					return "Removendo arquivo "+vetor[1]+" do servidor";
				}
			}
		}
		return "Ocorreu um Erro!";
	}			
	@SuppressWarnings("resource")
	public void run(){
	    try {
	    	String dado = in.readUTF();
	    	String[] myArgs = dado.split(" ");
	    	if(myArgs[0].equals("list") || myArgs[0].equals("rm")) {
	    		String resultado = mainFile(dado);
			    out.writeUTF(resultado);
	    	}else if(myArgs[0].equals("put")){
	    		int filesize = 605489;
		        int bytesRead;
		        int current = 0;
				byte[] buffer = new byte[filesize];
			    FileOutputStream fos = new FileOutputStream(System.getProperty("user.home")+"/"+myArgs[2]);
			    BufferedOutputStream bos = new BufferedOutputStream(fos);
			    bytesRead = in.read(buffer,0,buffer.length);
			    current = bytesRead;
				try {
					do {
					       bytesRead =
					          in.read(buffer, current, (buffer.length-current));
					       if(bytesRead >= 0) current += bytesRead;
					    } while(bytesRead > -1);
					bos.write(buffer, 0 , current);
				    bos.close();
				} catch (Exception e) {
					System.out.println("Erro: "+e);
				}finally {
					clientSocket.close();
				}
	    	}
		}catch(EOFException e) {System.out.println("EOF:"+e.getMessage());
	    }catch(IOException e) {System.out.println("IO:"+e.getMessage());
	    }finally{ try {clientSocket.close();
	    }catch (IOException e){/*close failed*/}}
	    	}
	    }