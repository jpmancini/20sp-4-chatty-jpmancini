import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{

	OutputStream out;
	InputStream in;
	BufferedReader clientReader;
	PrintWriter clientWriter;
	Socket clientSocket;
	String username;
	ServerHandler serverHandler;
	
	public ClientHandler( ServerHandler serverHandler, String username, Socket clientSocket ) {
		this.clientSocket = clientSocket;
		this.username = username;
		this.serverHandler = serverHandler;
	} // constructor
	
	public void run() {
		try {
			clientChat();
		}
		catch(IOException e) {
			System.out.println( "Error in clientChat" );
		} // end try/catch
	} // run
	
	private void clientChat() throws IOException {
		this.in = clientSocket.getInputStream();
		this.out = clientSocket.getOutputStream();
		this.clientReader = new BufferedReader( new InputStreamReader(in) );
		this.clientWriter = new PrintWriter( out, true );
			
		messageAll( "<" + username + "> connected", serverHandler.getClientList() );
			
		String inputLine;
		String message = "";
		while((inputLine = clientReader.readLine()) != null) {
			String[] inputs = inputLine.split(" ");
				
			if( inputs.length > 0 && inputs != null) {
				String command = inputs[0];
				
				if(command.equals("/quit")) {
					messageAll( "<" + username + "> disconnected", serverHandler.getClientList() );
					break;
				}
				else if( command.equals("/dm") && inputs[1] != null) {
					String directMessage = "";
					for( int i = 1; i < inputs.length; i++) {
						directMessage += ( inputs[i] + " " );
					}
					messageOne( inputs[1], directMessage ); 
				}
				else if(command.equals("/nick") && inputs[1] != null) {
					String temp = username;
					setUsername(inputs[1]);
					clientWriter.println("Username changed from <" + temp + "> to <" + username + ">");
				}

				else {
					ArrayList<ClientHandler> clientList = serverHandler.getClientList();
					message = "<" + username + ">: " + inputLine;
					messageAll( message , clientList );
				}
			}
		} // end while
		clientSocket.close();
	} // clientChat

	public String getUsername() {
		return username;
	} // getUsername
	
	public void message( String message ) {
		clientWriter.println( message );
	} //message
	
	public void messageAll( String message, ArrayList<ClientHandler> clientList ) {
		for( ClientHandler clientHandler : clientList ) {
			clientHandler.message( message ); 
		}
	} //messageAll
	
	public void setUsername(String username) {
		this.username = username;
	} //setUsername
	
	public void messageOne(String target, String message) {
		boolean found = false;
		boolean online = false;
		
		for( ClientHandler clientHandler : serverHandler.getClientList() ) {
			if( clientHandler.getUsername().equals( target ) ) {
				online = clientHandler.isAlive();
				found = true;
				
				if( found && online ) { 
					String dm = "[DM]<" + username + ">: " + message;
					clientHandler.message( dm );
					//clientWriter.println("Message sent to " + target);
					return;
				}
			}
		} // for
		
		clientWriter.println( "User not found" ); 
	} // messageOne
	
} //clientHandler
