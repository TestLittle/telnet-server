package utb.fai;

import java.io.*;
import java.net.*;
import java.util.Set;

public class ClientThread implements Runnable {

    private Socket clientSocket;
    private Set<Socket> activeSockets;

    public ClientThread(Socket clientSocket, Set<Socket> activeSockets) {
        this.clientSocket = clientSocket;
        this.activeSockets = activeSockets;
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
                this.activeSockets.remove(clientSocket);
            }catch (IOException e){
                e.printStackTrace();
            }
            System.out.println("Klient " + clientSocket.getInetAddress() + " se odpojil.");
        }
    }
}
