import java.net.*;

public class FxServer{

	public static void main (String[] args) throws Exception{

		ServerSocket ss = new ServerSocket(80);
		while(true){
			System.out.println("Server waiting...");
			Socket connectionFromClient = ss.accept();
			new FxServerThread(connectionFromClient);
		}
	}
}