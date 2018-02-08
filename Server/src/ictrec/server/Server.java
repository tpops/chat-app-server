/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * any message dat is sent from here using send all
 * automatically attatches an end 
 */
package ictrec.server;
import java.net.*;
import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.SocketException;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.*;
import java.io.*;
import com.Macropax.BadguysChat.Users;
/**
 *
 * @author POPOOLA
 */
public class Server {
    DataLayer Dl;
     private List<ServerClient> Chatclients =   new 
              ArrayList<ServerClient>();
     private List<ServerClient> clients =   new 
              ArrayList<ServerClient>();
     private List<Integer> GrpClientResp = 
             new ArrayList<Integer>();
     private List<Integer> UsrClientResp =
             new ArrayList<Integer>();
     private final int Max_Attempts=5;
     Stack pop;
    private int port;
    private DatagramPacket packet;
    private DatagramSocket socket;
    private Thread  run,Manage,Send,recieve,ManageUsers;
    private boolean Running = false;
    public Server(final int port,String URL,String User, String Password)
    {
        Dl = new DataLayer(URL,User,Password);
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        run = new Thread("Server"){
        public void run()
    {
        Running = true;
        System.out.println("Server started on Port-"+ port);
        ManageGrPClient();
        Recieve();
        Scanner sc;
    }
        };
        run.start();
        
    } 
    
   public void ManageUsers ()
   {
       ManageUsers = new Thread ("ManageUsers")
       {
           public void run (){
               while (Running)
               {
                   try {
                       Thread.sleep(2000);
                   } catch (InterruptedException ex) {
                       Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   SendToAll("/i/Stat",-1,true);
                   for (int i = 0; i< Chatclients.size();i++)
                   {
                       if (!UsrClientResp.contains(new Integer(Chatclients.get(i).Id)))
                       {
                           if (Chatclients.get(i).attempt>Max_Attempts){
                               disconnect (Chatclients.get(i).Id,false,true);
                           }
                       }
                       else
                       {
                           Chatclients.get(i).attempt =0;
                       }
                   }
               }
           }
       };ManageUsers.start();
   }
   
           public void ManageGrPClient ()
    {
        Manage = new Thread("Manage"){
         public void run()
         {
            
             while (Running)
                {
                    //managing clients                
                      SendToAll("/i/server",-1,false);
                 try {
                     Thread.sleep(2000);
                 } catch (InterruptedException ex) {
                     Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                 }
                      for(int i=0; i< clients.size();i++)
                      {
                          ServerClient c = clients.get(i);
                          if (!GrpClientResp.contains(c.Id))
                          { 
                              
                              if (c.attempt>Max_Attempts)
                              {
                                  c.attempt=0;
                              disconnect(c.Id,false,false);
                              
                              }
                              else
                              {
                               c.attempt++;
                              }
                          } else 
                          {
                              // if it contains it then remove for the next while loop
                              GrpClientResp.remove(new Integer(c.Id));
                              c.attempt = 0;
                          }
                      }
                   
                }
         }
        }; Manage.start();
    }
           // for sending personal messages
    public void SendPM (int fromId,int ToId,String Message)
    {
        boolean Offline= true;
        ServerClient sc;
        for (int i =0;i< Chatclients.size();i++)
        {
            sc = Chatclients.get(i);
            if (sc.Id == ToId)
            {
                Send(("/pm/"+fromId+"¬FId¬"+Message),sc.ip,sc.port);
                Offline = false;
                break;
            }
        }
        if (Offline)
        {
           // store the messages offline
        }
    }
    
    public void Recieve ()
    {
        recieve = new Thread("Recieve")
        {
            public void run()
            {
               
                do
                {
                    
                   
                        //managing recieving
                       byte [] data = new byte[1024]; 
                       DatagramPacket pcket = new DatagramPacket(data,data.length);
                       
                       try {
                       socket.receive(pcket);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        
                    }
                       
                    process(pcket);
                    
                    
                    
                }while (Running);
            }
        }; recieve.start();
        
    }
    private void SendToAll(String message, int id, boolean Users)
    {
        if (!Users){
        for(int i = 0 ; i <clients.size();i++)
        {
           
            ServerClient  clienti = clients.get(i);
                if (clienti.Id==id)
                {                
                }
                else{ 
                    Send(message,clienti.ip,clienti.port);
                
                }
            
        }
        } else
        {
            for (int i = 0 ; i<Chatclients.size();i++)
            {
                ServerClient sc = Chatclients.get(i);
                if (sc.Id==id)
                {
                    
                }else {
                    Send(message,sc.ip, sc.port);
                }
            }
        }
    }
    private void Send(String Message,final InetAddress Add,final int port)
    {
        // create an end indicatior
        Message+="/e/";
        Send(Message.getBytes(),Add,port);
    }

    private void Send (final byte[] data,final InetAddress Add,final int port)
    {
        Send = new Thread (){
            public void run()
            {
                DatagramPacket pckt = new DatagramPacket(data,data.length,Add,port);
                try {
                    socket.send(pckt);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
               
            }
        };
        Send.start();
    }
    private void process (DatagramPacket packet)
    {
        int id = 0;
        int RecieveId = 0;
        String msg = new String(packet.getData());
        if(msg.startsWith("/c/"))
        {
           // UUID id = UUID.randomUUID();
            id = UniqueIdentifier.getIdentifier();
            System.out.println("New Group Chat Client Joined with Id:"+id);
            String idC = "/c/"+id;
            System.out.println(idC);
            Send(idC,packet.getAddress(),
                    packet.getPort());
           
                    
            clients.add(new ServerClient(msg.split("/c/|/e/")[1]
                    ,packet.getAddress(),id ,packet.getPort()));
        }else if (msg.startsWith("/R/"))
        {
            List <String>RegDetails = new ArrayList <String>();
           
         RegDetails.add(msg.substring(3).split("¬id¬|¬Pa¬|¬C¬|¬Pn¬|/e/")[0]);
         RegDetails.add(msg.substring(3).split("¬id¬|¬Pa¬|¬C¬|¬Pn¬|/e/")[1]);
         RegDetails.add(msg.substring(3).split("¬id¬|¬Pa¬|¬C¬|¬Pn¬|/e/")[2]);
         RegDetails.add(msg.substring(3).split("¬id¬|¬Pa¬|¬C¬|¬Pn¬|/e/")[3]);
         RegDetails.add(msg.substring(3).split("¬id¬|¬Pa¬|¬C¬|¬Pn¬|/e/")[4]);
         System.out.println(RegDetails.add(msg.substring(3).split("¬id¬|¬Pa¬|¬C¬|¬Pn¬|/e/")[1]));
         Dl.RegisterationDet(RegDetails);
         Send("/R/A/e/".getBytes(),packet.getAddress(),packet.getPort());
        }
        else if (msg.startsWith("/A/"))
        {
            String UserName = msg.split("/A/|/U/|/e/")[1];
            String PassWord = msg.split("/A/|/U/|/e/")[2];
            if (Dl.Authenticate(UserName, PassWord))
            {
             System.out.println("User:"+UserName+" Authenticated");
                Send(("/A/"+Dl.GetId()+"/e/").getBytes(),
                        packet.getAddress(),packet.getPort());
            Chatclients.add(new ServerClient(UserName,packet.getAddress(),
                    Dl.GetId(),packet.getPort()));
            } else 
            {
                 Send(("/A/"+-1+"/e/").getBytes(),
                        packet.getAddress(),packet.getPort());
            }
        } else if  (msg.startsWith("/pm/"))
        {
           
                    int FromId = Integer.parseInt(msg.substring(4).split("¬Fid¬|¬Tid¬|/e/")[0]);
                    int ToId = Integer.parseInt(msg.substring(4).split("¬Fid¬|¬Tid¬|/e/")[1]);
                    String Message =msg.substring(4).split("¬Fid¬|¬Tid¬|/e/")[2];
                    System.out.println(FromId+ToId+Message);
                    SendPM(FromId,ToId,Message);
        }
        else if (msg.startsWith("/p/"))
        {
            if ("Stati".equals(msg.split("/p/|/inf/|/id/|/e/")[1]))
            {
                String Status;
                Status = Dl.GetStatus(Integer.parseInt(msg.split("/p/|/inf/|/id/|/e/")[2] ));
                Send(("/p/Stat/id/"+Status),packet.getAddress(),packet.getPort());
            }
            else if ("Statr".equals(msg.split("/p/|/inf/|/id/|/e/")[1]))
            {
                Dl.SetStatus(Integer.parseInt(msg.split("/p/|/inf/|/id/|/e/")[2] ),
                        msg.split("/p/|/inf/|/id/|/e/")[3] );
            }
            else if ("FL".equals(msg.split("/p/|/inf/|/id/|/e/")[1]))
            {
//                List <Users> U = new ArrayList<Users>();
                
               List <Users> sc =Dl.GetFriends(Integer.parseInt(msg.split("/p/|/inf/|/id/|/e/")[2]));
              
               
            
              
               for (int i=0;i<sc.size();i++)
               {
                   boolean online= false;
                   Users s = sc.get(i);
                   for (int j =0; j < Chatclients.size();j++)
                   {
                       ServerClient c = Chatclients.get(j);
                       if (c.Id == s.Id)
                       {
                           online = true;
                           s.online = true;
                           break;
                       }
                    
                   }
//                    U.add( new Users (s.Name,s.Id,online)); 
               }
                Stream(sc, packet);
               
            }
            
            else if ("On".equals(msg.split("/p/|/inf/|/id/|/e/")[1]))
            {
                UsrClientResp.add(Integer.parseInt(msg.split("/p/|/inf/|/id/|/e/")[2]));
            }
             else if ("di".equals(msg.split("/p/|/inf/|/id/|/e/")[1]))
             {
                 System.out.println(Integer.parseInt(msg.split("/p/|/inf/|/id/|/e/")[2]));
                 disconnect(Integer.parseInt(msg.split("/p/|/inf/|/id/|/e/")[2]),true,true);
             }
        }
        else if (msg.startsWith("/m/"))
        {
            String name = "";
            String message = msg.substring(3).split("/id/|/e/")[1];
            RecieveId= Integer.parseInt(msg.substring(3).split("/id/|/e/")[0]);
            System.out.println("Recieved data from:"+RecieveId);
            System.out.println(msg);
           
                    
            SendToAll("/m/"+message,RecieveId,false);
        }
        else if (msg.startsWith("/d/"))
        {
            // check out the Id of the Client
            // that disconnected
            
           disconnect(Integer.parseInt(msg.split("/d/|/e/")[1]),true,false);       
           
        }
        else if (msg.startsWith("/i/"))
        {
            GrpClientResp.add(Integer.parseInt(msg.split("/i/|/e/")[1]));     
        }
        
    }
    public void disconnect(int RecieveId,boolean status,boolean User)
    {
        // checking if user has been connected 
        //before or not
        boolean A_User = false;
        ServerClient sc=null;
        String Message;
        
            
       
      if (!User)
      {
           UniqueIdentifier.ReturnIdentifier(RecieveId);
        for (int i=0;i<clients.size();i++)
        {
            if (clients.get(i).Id==RecieveId)
            {
                sc = clients.get(i);
                clients.remove(i);
                A_User = true;
                break;
            }
            
            
        }
            LogOutLog(A_User, status, sc, RecieveId);
        
            
      }
      else
      {
           for (int i=0;i<Chatclients.size();i++)
        {
            if (Chatclients.get(i).Id==RecieveId)
            {
                sc = Chatclients.get(i);
                Chatclients.remove(i);
                A_User = true;
                break;
            }
                
        }
           LogOutLog(A_User, status, sc, RecieveId);
      }
//        if (A_User )
//        {
//           String Message; 
//           if (User)
//           {
//        if (status )
//        {
//            
//        
//        }else Message="Client:"+sc.name+" Id:"+RecieveId+" @"+sc.ip.toString()+":"+sc.port+" Timmed out ";
//            
//            
//        System.out.println(Message);      
//           } else
//           {
//               if (status )
//        {
//            Message="Client:"+sc.name+"Id:"+RecieveId+" @"+sc.ip.toString()+":"+sc.port+" Logged out from Groupchat";
//        
//        }else Message="Client:"+sc.name+" Id:"+RecieveId+" @"+sc.ip.toString()+":"+sc.port+" Timmed out from Groupchat";
//           }
//        } else 
//            System.out.println("not connected so cant be logged out");
//            
   }

    private void Stream(List<Users> sc, DatagramPacket packet) {
        try 
        {
            
          
           
           ByteArrayOutputStream Os = new ByteArrayOutputStream();
           ObjectOutputStream  OOs = new ObjectOutputStream(Os);
           OOs.writeObject(sc);
           OOs.close();
          byte [] buf = Os.toByteArray();
          
          Send (buf,packet.getAddress(),packet.getPort());
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void LogOutLog(boolean A_User, boolean status, ServerClient sc, int RecieveId) {
        String Message;
        if (A_User){  if (status)
          Message="Client:"+sc.name+"Id:"+RecieveId+" @"+sc.ip.toString()+":"+sc.port+" Logged out ";
  else Message="Client:"+sc.name+"Id:"+RecieveId+" @"+sc.ip.toString()+":"+sc.port+" Timed out ";System.err.println(Message);}
  else System.out.println("not connected so cant be logged out");
    }
    
    /**
     * 
     * <param> Message
     * This is to send message to all
     * Client
     * </param>
     * <Summary> Takes in the message
     * @param message
     */
   
}
