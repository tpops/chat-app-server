/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ictrec.chat.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author POPOOLA
 */
public class Client {
    private boolean running = false;
    public boolean GetRunning()
    {
        return running;
    }
    int port; 
    String Address;
    String name;
    private Socket streamSock;
     private DatagramSocket socket;
    private InetAddress iP;
    private Thread ThreadSend;
    private Thread recieve;
    private int Id=-1;
     /**
     * @param Address Ip address @argument string
     * @param name Name of the client string
     * @param port Port inputed by the client
     */
    public Client(String name,String Address,int port)
    {
        running  = true;
        this.port = port;
        this.Address= Address;
        this.name = name;
        
    }
    
    public void Disconnect()
    {
       new Thread()
       {
           public void run()
           {
        synchronized(socket)
        {
        socket.close();
        }
           }
       }.start();
    }
    public void setId (int id)
    {
        this.Id = id;
    }
    
   /**
     * <Summary> this recieves series of messages at run time
     * Attatch this to the constructor of ur clients window
     * @return proccessed  Recieved message from the server
     * </Summary>
     */
    public DatagramPacket Recieve()
    {
       
                
        byte[] data = new byte[1024];
        DatagramPacket pckt = new DatagramPacket(data,data.length);
        try {
            socket.receive(pckt);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return pckt;
        
           
    }
    
    public int GetId()
    {
        return Id;
    }
     public void SetName (String Name)
     {
         this.name = Name;
     }
     public String GetName ()
     {
         return name;
     }
     /**
      * <summary> do not send in string here
     * @param data the message in byte form
     */
    public void Send(final byte[]data)
    {
        ThreadSend = new Thread("Send")
        {
            public void run()
            { 
                DatagramPacket  pckt = new DatagramPacket(data,data.length,iP,port);
                try {
                    socket.send(pckt);
                } catch (IOException ex) {
                   ex.printStackTrace();
                }
            
            }
           
        };
        ThreadSend.start();
    }
   
/**
 * @return connection validity to true or 
 * false
 */
    public boolean OpenConnection() {
        
        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            ex.printStackTrace();
            return false;
        }
          try {
            this.iP=InetAddress.getByName(Address) ;
        } catch (UnknownHostException ex) {
           ex.printStackTrace();
           return false;
        }
        
        return true;
    }
    
}
