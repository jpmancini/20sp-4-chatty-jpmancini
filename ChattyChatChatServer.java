public class ChattyChatChatServer {

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		ServerHandler server = new ServerHandler(port);
		System.out.println("Now accepting client connections...");
		server.start();
	} // main
} // ChattyChatChatServer

