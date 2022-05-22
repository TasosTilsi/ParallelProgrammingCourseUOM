import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class EchoClientTCP {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    private static final String EXIT = "CLOSE";
    private static final int SOCKET_TIMEOUT = 60;

    public static void main(String args[]) throws IOException, InterruptedException {

        Socket dataSocket = new Socket(HOST, PORT);

        InputStream is = dataSocket.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        OutputStream os = dataSocket.getOutputStream();
        PrintWriter out = new PrintWriter(os, true);

        System.out.println("Connection to " + HOST + " established");

        String inmsg, outmsg;
        ClientProtocol app = new ClientProtocol();

        outmsg = app.prepareRequest();
        while (!outmsg.equals(EXIT)) {
            out.println(outmsg);
            inmsg = in.readLine();
            app.processReply(inmsg);
            outmsg = app.prepareRequest();
        }
        out.println(outmsg);

        dataSocket.close();
        System.out.println("Data Socket closed");

    }
}
