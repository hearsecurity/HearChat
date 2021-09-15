import java.net.*;
import java.io.*;
import java.util.*;

public class HearServer {

      ArrayList<PrintWriter> clientOutputStreams;

      public static void main(String[] args) {

            new HearServer().startserver();
      }

      public void startserver() {

          clientOutputStreams = new ArrayList<PrintWriter>();
          try {
               ServerSocket socket = new ServerSocket(5000);
               System.out.println("Server listening on port 5000");
               while(true) {

                  Socket clientSocket = socket.accept();
                  PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                  clientOutputStreams.add(writer);

                  Thread t = new Thread(new ClientHandler(clientSocket));
                  t.start();
               }

          }catch(Exception ex) {
               ex.printStackTrace();
          }
      }

      class ClientHandler implements Runnable {

             BufferedReader reader;
             Socket sock;

             public ClientHandler(Socket clientSocket) {

                 try{
                      sock = clientSocket;
                      InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                      reader = new BufferedReader(isReader);

                 }catch(IOException ex) {
                     ex.printStackTrace();
                 }
             }

             public void run() {

                 String message;

                 try {

                       while((message = reader.readLine()) != null) {
                           sendtolog(message);
                           tellEveryone(message);
                       }

                 }catch(IOException ex) {
                    ex.printStackTrace();
                 }
             }

             public void tellEveryone(String message) {

                 Iterator it = clientOutputStreams.iterator();

                 while(it.hasNext()) {

                    try {
                        PrintWriter writer = (PrintWriter)it.next();
                        writer.println(message);
                        writer.flush();
                    }catch(Exception ex) {
                        ex.printStackTrace();
                    }

                 }

             }

             public void sendtolog(String message) {

                 try(FileWriter fw = new FileWriter("log.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw))
             {
                 out.println(message);
             } catch (IOException e) {
                 System.out.println("File not found.");
             }

             }
      }

}
