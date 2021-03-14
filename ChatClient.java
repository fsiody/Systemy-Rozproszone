import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public class ChatClient {
    String id;
    Socket socketTCP;
    int portNumber;
    String hostName;

    DatagramSocket socketUDP;

    ChatClient(String id){
        this.id=id;
        this.hostName = "localhost";
        this.portNumber = 12345;
        this.socketTCP = null;
        this.socketUDP = null;
    }

    public void run() {
        try {
            // create socket
            this.socketTCP = new Socket(this.hostName, this.portNumber);

            // in & out streams
            PrintWriter out = new PrintWriter(this.socketTCP.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socketTCP.getInputStream()));
            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

            // send msg, read response
            System.out.println("["+this.id + "] CHAT CLIENT CONNECTED");
            out.println("["+this.id+"] "+"Hello");
            String msg;
            Boolean connectionWorks = true;

            while (connectionWorks) {
                msg = in.readLine();
                if(msg!=null) {
                    System.out.println("["+this.id+"] received: \n  * * * * * \n " + msg + "\n * * * * * \n");
                    System.out.print("[" + this.id+"] *writing*");
                    String answer = consoleIn.readLine();
                    out.println("["+this.id+"] "+answer);
                    if(answer.contains("Bye")) connectionWorks=false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
    }

    private void close() {
        try {
            if (this.socketTCP != null) {
                this.socketTCP.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

