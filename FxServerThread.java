import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.StringTokenizer; 

public class FxServerThread extends Thread{
	private Socket connectionFromClient = null;

	public FxServerThread(Socket connectionFromClient){
		this.connectionFromClient = connectionFromClient;
		start();
	}

	public void run(){

		try {

			InputStream in = connectionFromClient.getInputStream();
			OutputStream out = connectionFromClient.getOutputStream();

			BufferedReader headerReader = new BufferedReader(new InputStreamReader(in));
			BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(out));

			DataInputStream DataIn = new DataInputStream(in);
			DataOutputStream DataOut = new DataOutputStream(out);

			String header = headerReader.readLine();
			StringTokenizer strk = new StringTokenizer(header, " ");

			String command = strk.nextToken();
			String fileName = strk.nextToken();
			
			String errorMessage = "NOT FOUND\n";

			if(command.equals("download")) {
				try {
					errorMessage = "NOT FOUND\n";

					FileInputStream fileIn = new FileInputStream("ServerShare/" + fileName);
					int fileSize = fileIn.available();
					header = "OK " + fileSize + "\n";
					
					headerWriter.write(header, 0, header.length());
					headerWriter.flush();

					byte[] bytes = new byte[fileSize];
					fileIn.read(bytes);
					fileIn.close();

					DataOut.write(bytes, 0, fileSize);
				} catch(Exception ex) {
					headerWriter.write(errorMessage, 0, errorMessage.length());
					headerWriter.flush();
				} finally {
					connectionFromClient.close();
				}


			} else if(command.equals("upload")) {
				try {

					int size = Integer.parseInt(strk.nextToken());

					byte[] space = new byte[size];
					DataIn.readFully(space);

					FileOutputStream fileOut = new FileOutputStream("ServerShare/" + fileName);
					fileOut.write(space, 0, size);
					fileOut.close();

					header = "OK" + "\n";
					headerWriter.write(header, 0, header.length());
					headerWriter.flush();
					

				} catch (FileNotFoundException ex){
				} finally {
					connectionFromClient.close();
				}

			} else if(command.equals("delete")) {

				try {
					Files.delete(Paths.get("ServerShare/" + fileName));
					header = "OK" + "\n";
					headerWriter.write(header, 0, header.length());
					headerWriter.flush();
					
				} catch(Exception ex) {
					errorMessage = "NOT FOUND\n";
					headerWriter.write(errorMessage, 0, errorMessage.length());
					headerWriter.flush();
				} finally {
					connectionFromClient.close();
				}

			} else {

			}


		} catch(Exception e) {

		}
	}
}