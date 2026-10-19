package utb.fai;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {

    private Socket clientSocket;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        // Implementation of processing incoming communication from the telnet client
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String line;
            while((line = reader.readLine()) != null){
                System.out.println("Klient (" + clientSocket.getInetAddress() + ") napsal: " + line);
                writer.println(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
                clientSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            System.out.println("Klient " + clientSocket.getInetAddress() + " se odpojil.");
        }
    }
}
