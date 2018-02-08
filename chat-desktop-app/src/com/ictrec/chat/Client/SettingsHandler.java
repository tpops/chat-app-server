/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Macropax.BadguysChat.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author POPOOLA
 */
public class SettingsHandler {
    File fl ;
    SettingsC  s;
    String Username;
    public SettingsHandler (String Username){
        this.Username = Username;
        s = new SettingsC();
      fl = new File("Settings/"+Username+".ini");
      if (!fl.exists())
      {
          try
          {
          fl.createNewFile();
          } catch( IOException e)
          {
              e.printStackTrace();
          }
      }
      else
      {
          try
          {
          FileInputStream fis = new FileInputStream (fl.getAbsoluteFile());
          ObjectInputStream  OS = new ObjectInputStream (fis);
          s= (SettingsC) OS.readObject();
          OS.close();
          } catch (Exception e)
          {
              e.printStackTrace();
      }
      }
      
          
    }
    public SettingsC GetSetting ()
    {
        return s;
    }
    
    public void StoreSettings(SettingsC s)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream (fl.getAbsoluteFile());
            ObjectOutputStream OOS = new ObjectOutputStream(fos);
            OOS.writeObject(s);
            OOS.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
