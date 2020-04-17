import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHandler extends Thread {
	private int port;
	static int clientNumber = 0;
	ArrayList <ClientHandler> clientList = new ArrayList<>();
	
	public ServerHandler( int port ) {
		this.port = port;
	} // constructor
	
	public ArrayList<ClientHandler> getClientList(){ 
		return clientList;
	} //getClientList

	@Override
	public void run() {
		boolean runServer = true;
		ServerSocket listener = null;
		
		try {
			listener = new ServerSocket(port);
		}
		catch(IOException e){
			System.out.println( "Error creating listener" );
		} // end try/catch
		
		try {
			while( runServer ) {
				Socket clientSocket = listener.accept();
				System.out.println( "<" + clientSocket + "> has connected" );
				
				String username = "Client" + clientNumber;
				
				ClientHandler clientHandler = new ClientHandler( this , username, clientSocket );
				clientList.add( clientHandler );
				clientNumber++;
				
				clientHandler.start();
			} // end while
		}
		catch(IOException e) {
			System.out.println( "Error running Server" );
			runServer = false;
		} //end try/catch
	}
}
