import java.net.*;
import java.io.*;
import java.util.stream.Stream;

public class EchoServerTCP {
    private static final int PORT = 1234;
    private static final String EXIT = "CLOSE";

    public static void main(String args[]) throws IOException {

        ServerSocket connectionSocket = new ServerSocket(PORT);
        System.out.println("Server is listening to port: " + PORT);

        Socket dataSocket = connectionSocket.accept();
        System.out.println("Received request from " + dataSocket.getInetAddress());

        InputStream is = dataSocket.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        OutputStream os = dataSocket.getOutputStream();
        PrintWriter out = new PrintWriter(os, true);

        String inmsg, outmsg;
        while(is.available() != 0); //block until there is data
        inmsg = in.readLine();
        ServerProtocol app = new ServerProtocol();
        outmsg = app.processRequest(inmsg);
        while (!outmsg.equals(EXIT)) {
            out.println(outmsg);
            inmsg = in.readLine();
            outmsg = app.processRequest(inmsg);
        }

        dataSocket.close();
        System.out.println("Data socket closed");


    }
}
