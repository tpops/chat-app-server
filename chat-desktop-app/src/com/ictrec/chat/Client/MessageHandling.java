/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Macropax.BadguysChat.Client;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author POPOOLA
 */
public class MessageHandling {
    List <String> Messages ;
    File file ;
    String Username;
    FileWriter FW;
     BufferedWriter BW;
    public MessageHandling(String Username)
    {
        this.Username = Username;
        FileReader fr;
        Messages = new ArrayList<String>();
        System.out.println(Username);
        
        try {
             file = new File ("MessageHistory/"+Username+".dat");
             if (!file.exists())
             {
                file.createNewFile();
             }
             else
             {
                 FileReader  Fr = new FileReader (file.getAbsoluteFile());
             BufferedReader Br = new BufferedReader (Fr);
             String line = null;
             while((line = Br.readLine())!= null)
                     {
                         Messages.add(line);
                     }
             Br.close();
             }
             
        } catch (IOException ex) {
           
            Logger.getLogger(MessageHandling.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
       
    }
    
   public  List <String> MessageHistory (String Id)
   {
       List <String> FriendHistory = new ArrayList<String>();
       for (int i =0;i<Messages.size();i++)
       {
           if (Messages.get(i).startsWith(Id))
           {
               String msg = Messages.get(i);
               FriendHistory.add(msg.substring(Id.length(), msg.length()));
           }
       }
       return FriendHistory;
   }
  /**
   * <Summary> this clears the chat history
   * both on the text file and on the temporary 
   * screen please use wisely
   * </Summary>
   */
    public void ClearHistory() {
        Messages.clear();
        try{
        FW = new FileWriter(file.getAbsoluteFile()); 
         BW = new BufferedWriter(FW); 
         BW.write("");
       
        BW.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
       
    }
    /**
   * <Summary> Pushes Messages into the temporary
   * display
   * @param  Id Id of the message that is been pushed in
   * @param Message  message been pushed in
   * </Summary>
   */
    public void PushMessages (int Id , String Message)
    {
        Messages.add(Id+Message);
       
    }


    public void CloseStream() throws IOException
    {
         try {
           
            Write();
            
        } catch (IOException ex) {
            Logger.getLogger(MessageHandling.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }

    private void Write() throws IOException {
        FW = new FileWriter(file.getAbsoluteFile()); 
         BW = new BufferedWriter(FW); 
         for (int i = 0 ; i <Messages.size();i++)
         {
        BW.append(Messages.get(i)+"\n");
         }
       
        BW.close();
    }
}
