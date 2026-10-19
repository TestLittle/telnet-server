package utb.fai;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class App {
    private static ExecutorService executorService;
    public static void main(String[] args) {
        // Implement input parameter processing
        int port = Integer.parseInt(args[0]);
        int max_threads = Integer.parseInt(args[1]);

        executorService = Executors.newFixedThreadPool(max_threads);
        
        // Implementation of the main server loop
        try {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while(!executorService.isShutdown()){
                    Socket socket = serverSocket.accept();
                    ClientThread thread = new ClientThread(socket);
                    try{
                        executorService.execute(thread);
                    }catch(RejectedExecutionException e){
                        socket.close();
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            closeSockets();
        }
    }

    public static void closeSockets(){
        executorService.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
