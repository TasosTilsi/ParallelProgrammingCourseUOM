import java.net.*;
import java.io.*;

public class ServerProtocol {

    private static final String SEPARATOR = "===================================================";

    public String processRequest(String theInput) {
        System.out.println(SEPARATOR);
        System.out.println("Received message from client: " + theInput);
        System.out.println(SEPARATOR);
        String theOutput ="";
        try {
            theOutput = new ComputePI().compute(theInput);
            System.out.println("\nComputing...\n");
        }catch (Exception e){
            theOutput = "Your Message was : '" + theInput +"' Please Insert a parsable Long Number";
        }
        System.out.println(SEPARATOR);
        System.out.println("Send message to client: \n" + theOutput);
        System.out.println(SEPARATOR);
        return theOutput;
    }


}
