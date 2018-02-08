/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Macropax.BadguysChat.Client;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author POPOOLA
 */
public class SettingsC implements Serializable {
    public boolean sound ;
    public boolean EOflineMessages;
    public boolean EN;
    public List <Integer> BlockedContacts;
    public SettingsC()
    {
        sound = true;
        EOflineMessages= false;
        EN = true;
        BlockedContacts = new ArrayList<Integer>();
    }
    
}
