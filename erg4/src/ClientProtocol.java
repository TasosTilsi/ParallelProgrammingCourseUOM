import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class ClientProtocol {

    private static final int SOCKET_TIMEOUT = 60;

    BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

    public String prepareRequest() throws IOException {
        System.out.print("Enter message to send to server:");
        String theOutput = user.readLine();
        return theOutput;
    }

    public void processReply(String theInput) throws IOException, InterruptedException {

        System.out.println("Message received from server: " + theInput);

    }

}
