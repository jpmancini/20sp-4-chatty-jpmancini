import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChattyChatChatClient {
	static boolean done = false;
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[1]);
		String host = args[0];
		
		try {
			Socket clientSocket = new Socket( host, port );
			System.out.println( "Connected to server" );
			
			BufferedReader clientReader = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
			BufferedReader inputReader = new BufferedReader( new InputStreamReader( System.in ) );
			PrintWriter clientWriter = new PrintWriter( clientSocket.getOutputStream(), true );

			Thread clientReading = new Thread() {
				@Override
				public void run() {
					while( !done ) {
						try {
							String line = clientReader.readLine();
							System.out.println( line );
						}
						catch(IOException e) {
							System.out.println( "Error in clientReading" );
						} // end try/catch
					} // end while
				} // run
			}; // clientReading
		

			Thread clientWriting = new Thread() {
				@Override
				public void run() {
					try {
						while(true) {
								String userInput;
								userInput = inputReader.readLine();
								clientWriter.println( userInput );
								if( userInput.equals( "/quit") ) {
									break;
								}
						} // end while
						clientSocket.close();
						done = true;
					}
					catch(IOException e) {
						System.out.println( "Error in clientWriting" );
					} // end try/catch
				} // run
			}; // clientWriting
			
			clientReading.start();
			clientWriting.start();
		
		}
		catch(IOException e) {
			System.out.println( "Error connecting to server" );
		} // end try/catch
	} // main
} // ChattyChatChatClient


