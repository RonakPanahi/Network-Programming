/**
 * This is a Lab project for Network Programming
 * AUTHOR: RONAK PANAHI
 * Date: October 2015
 * Problem Description: nslookup is a Unix utility that converts hostnames to IP  
 * addresses and IP addresses to hostnames. It has two modes: interactive
 * and command line. If you enter a hostname on the command line, nslookup
 * prints the IP address of that host. If you enter an IP address on 
 * the command line, nslookup prints the hostname. If no hostname or 
 * IP address is entered  on the command line, nslookup enters interactive
 * mode, in which it reads hostnames and IP addresses from input and echoes
 * back the corresponding IP addresses or hostnames until you type “exit”.
 */



package ip_hostname;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Find_IP_Host {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
	
	
		 if (args.length > 0) 
		 { 
		      for (int i = 0; i < args.length; i++) 
		      {
		    	if(ItIsHost(args[i]))
		    		Gethost(args[i]);		    	
		    	else
		        GetIP(args[i]);
		      }
		    } 
		 else 
		 {
		      DataInputStream dis = new DataInputStream(System.in);
		      System.out.println("Enter Host names or IP addresses. Enter \"exit\" to quit the command line.");
		      while (true) {
		        String s;
		        try {
		          s = dis.readLine();
		        } catch (IOException e) {
		          break;
		        }
		        if (s.equals("exit"))
		          break;
		        GetIP(s);
		      }
		 }
	}
	
	private static void Gethost(String input) {
		
		byte[] ip;
	
		
		try {
			
			ip = InetAddress.getByName(input).getAddress();
			for (int i = 0; i < ip.length; i++) {
		        int unsignedByte = ip[i] < 0 ? ip[i] + 256 : ip[i];
		        System.out.print(unsignedByte + ".");
		      }	

			System.out.println("The Network Class for this IP is: "+ NetworkClass(input));	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Cant find the Host name for "+ input);
			e.printStackTrace();
		}
		
		
	}

	private static String NetworkClass(String input) {
		// TODO Auto-generated method stub
		try {
			input = InetAddress.getByName(input).getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] record = input.split("\\.");
		int firstOctet = Integer.parseInt(record[0]);
		if(firstOctet >= 1 && firstOctet <= 127)
			return "Class A";
		else if(firstOctet >= 128 && firstOctet <= 191)
			return "Class B";
		else if(firstOctet >= 192 && firstOctet <= 223)
			return "Class C";
		else if(firstOctet >= 224 && firstOctet <= 239)
			return "Class D";
		
		return "Class Not found";
	}

	private static boolean ItIsHost(String string) {
		
		char[] c = string .toCharArray();
		
		for(int i = 0 ; i<c.length ; i++)
		{
			if(Character.isDigit(c[i])== false)
				if(c[i] != '.')
					return true;
				
		}
		return false;
	}

	private static void GetIP(String s) {
		InetAddress ip_address = null;
	    try {
	      ip_address = InetAddress.getByName(s);
	      System.out.println(ip_address.getHostName() + " = "  + ip_address.getHostAddress());
		    System.out.println("The Network Class for this IP is: "+ NetworkClass(s));
	    	} 
	    catch (UnknownHostException e) {
	    	System.out.println("Can not find the IP address : "+ s );
	    	}
	   
	}
	
	
}
