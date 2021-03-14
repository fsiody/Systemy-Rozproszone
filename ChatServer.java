import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ChatServer {

    static PrintWriter[] outTable = new PrintWriter[10];
    static BufferedReader[] inTable = new BufferedReader[10];
    static int tableCount= 0;
    static int portNumber = 12345;


    static InetAddress[] udpTable = new InetAddress[10];
    static int udpCount=0;

    public static void main(String[] args) throws IOException {

        System.out.println("JAVA TCP SERVER");
        ServerSocket serverSocket = null;

        try {
            // create socket
            serverSocket = new ServerSocket(portNumber);

            while(true){

                // accept client
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected");

                Thread conection = new Thread( () -> {
                    try {
                        // in & out streams
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outTable[tableCount] = out;
                        inTable[tableCount++] = in;
                        String msg;
                        Boolean connectionWorks = true;

                        while (connectionWorks) {
                            msg = in.readLine();
                            if(msg!=null) {
                                System.out.println("received msg: " + msg);
                                sendToAllClients(msg, out);
                                if (msg.contains("bye")) connectionWorks=false;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                conection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (serverSocket != null){
                serverSocket.close();
            }
        }
    }

    private static void sendToAllClients(String msg, PrintWriter out){
        for(int i = 0; i < tableCount; i++){
            if(!outTable[i].equals(out)) outTable[i].println(msg);
        }
    }
}
