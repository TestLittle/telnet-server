package utb.fai;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class App {

    public static void main(String[] args) {
        // Implement input parameter processing
        int port = Integer.parseInt(args[0]);
        int max_threads = Integer.parseInt(args[1]);

        ExecutorService executorService = Executors.newFixedThreadPool(max_threads);
        
        // Implementation of the main server loop
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(!executorService.isShutdown()){
                Socket socket = serverSocket.accept();
                ClientThread thread = new ClientThread(socket);
                try{
                    executorService.execute(thread);
                }catch(RejectedExecutionException e){
                    socket.close();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
