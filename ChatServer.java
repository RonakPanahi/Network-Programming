/**
 * This is the Term Project for Network Programming with Java 
 * AUTHOR : RONAK PANAHI
 * Date : December 2015
 * This program simulates the operation of a Chat server. 
 * Each user (client) logs into the system (server); and typed 
 * messages by each user is relayed or broadcast to each of the
 * other users by the server. You need to set up a server on a 
 * vacant port. The service should allow multiple connections 
 * from users. 
*/

package chatter;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class ChatServer
{   
	private static LinkedList<Socket> client_List = new LinkedList<Socket>();
	static ListIterator<Socket> listIterator = client_List.listIterator();
	private static int Current_Client_id = 1000;

 public static void main(String[] args) throws IOException
 {   
	 //Connection on port 2525
	 int port = 2525;     
     if (args.length > 0) port = Integer.parseInt(args[0]);
     ServerSocket serversocket = new ServerSocket(port);
     System.out.println("The Chat Server is running on port " + port);

     // Listen for clients. Start a new handler for each.
     while(true)
     {  
    	 Socket client = serversocket.accept();
         new ChatHandler(client).start();
         System.out.println("New client no. " + Current_Client_id + " from "
                     + serversocket.getInetAddress() + " on client's port "
                     + client.getPort());
         client_List.add(client);
         Current_Client_id++;
     }
 }

 static synchronized void broadcast(String message, String name) throws IOException   
 {   
	 Socket s = null;
     PrintWriter p;
     for (int i = 0; i < client_List.size() ; i++)
     {   
    	 
    	 s = client_List.get(i);
         p = new PrintWriter(s.getOutputStream(), true);
         p.println(name + ": " + message);
         p.flush();
     }
     System.out.println(name + ": " + message); 
 }

 static synchronized void remove(Socket s)
 {  
	 Socket t;
     for (int i = 0; i < client_List.size() ; i++)
     {   t = client_List.get(i);
         if (t.equals(s)) break;
     }
     client_List.remove();
     Current_Client_id--;
 }
}

class ChatHandler extends Thread
{       
// One thread for each client
 private BufferedReader in;
 private PrintWriter out;
 private Socket toClient;
 private String name;

 ChatHandler(Socket s)
 {   toClient = s;
 }

 public void run()
 {   try
     {  
     	
	 in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
         out = new PrintWriter(toClient.getOutputStream(),true);
         out.println("*** Welcome to the ChatServer ***");
         out.println("Type BYE to end the session");
         out.print("What is your name? ");
         out.flush();
         name = in.readLine();
         ChatServer.broadcast(name + " has joined the discussion!","Server");

         while(true)
         {   
        	 String s = in.readLine();
             try {
				if (s.equalsIgnoreCase("bye"))
				 {   
					 ChatServer.broadcast(name + " has left the discussion.","Server");
				     System.out.println(name + " has disconnected.");  // Server terminal
				     break;
				 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.println(name+" left without saying Bye.");
				System.out.println("Closing the chat...");
				System.exit(0);
			}
             
             ChatServer.broadcast(s,name);
          }
          ChatServer.remove(toClient);
          toClient.close();
      }
      catch (IOException e)
      {  
    	  System.out.println("Chat system error: " + e);
      }
 }
}
