import java.net.*;
import java.io.*;
import java.util.StringTokenizer;

public class FxClient{
	public static void main(String[] args) throws Exception{
		String command = args[0];
		String fileName = args[1];

		String header = null;
		StringTokenizer strk = null;

		Socket connectionToServer = new Socket("localhost", 80);

		InputStream in = connectionToServer.getInputStream();
		OutputStream out = connectionToServer.getOutputStream();

		BufferedReader headerReader = new BufferedReader(new InputStreamReader(in));
		BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(out));

		DataInputStream dataIn = new DataInputStream(in);
		DataOutputStream dataOut = new DataOutputStream(out);

		if(command.equals("d")){
			header = "download " + fileName + "\n";
	
			headerWriter.write(header, 0, header.length());
			headerWriter.flush();

			header = headerReader.readLine();

			if(header.equals("NOT FOUND")){

				System.out.println("We're extremely sorry, the file you specified is not available!");

			}else{
				strk = new StringTokenizer(header, " ");

				String status = strk.nextToken();

				if(status.equals("OK")){

					int size = Integer.parseInt(strk.nextToken());

					byte[] space = new byte[size];

					dataIn.readFully(space);

					FileOutputStream fileOut = new FileOutputStream("ClientShare/" + fileName);
					fileOut.write(space, 0, size);
					fileOut.close();

				}else{
					System.out.println("You're not connected to the right Server!");
				}

			}

		}else if(command.equals("u")){
			try {
				FileInputStream fileIn = new FileInputStream("ClientShare/"+ fileName);
				int fileSize = fileIn.available();

				header = "upload " + fileName + " " + fileSize +"\n";
				headerWriter.write(header, 0, header.length());
				headerWriter.flush();
				
				byte[] bytes = new byte[fileSize];
				fileIn.read(bytes);
				fileIn.close();
				dataOut.write(bytes, 0, fileSize);
				
				header = headerReader.readLine();

				if(header.equals("OK")) {
					System.out.println("Upload Completed!");
					
				} else {
					System.out.println("File Already Exists!");
				}

			} catch(Exception e) {
				System.out.println("File does not exist in local system!");
			}


		} else if(command.equals("del")) {

			header = "delete " + fileName + "\n";
			headerWriter.write(header, 0, header.length());
			headerWriter.flush();

			header = headerReader.readLine();
			
			if(header.equals("NOT FOUND")) {
				System.out.println("No such file exists!");
			} else {
				System.out.println("Deletion Complete!");
			}



		} else{
			//To do
		}
		connectionToServer.close();
		
	}
	

}