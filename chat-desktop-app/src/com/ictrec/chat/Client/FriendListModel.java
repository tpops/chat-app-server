/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ictrec.chat.Client;

import com.Macropax.BadguysChat.Users;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 *
 * @author POPOOLA
 */
public class FriendListModel {
    private DefaultListModel data = new DefaultListModel();
    boolean Search;
    public FriendListModel(boolean Search)
    {
        this.Search = Search;
    }
    private List <Integer> Id = new ArrayList<Integer>();
    public void AddValues (List <Users> user)
    {
        for (int i =0;i<user.size();i++)
        {
            if (Search)
            {
               this.data.add(i,user.get(i).Name); 
            }else  this.data.add(i,user.get(i).Display());
            
            
            Id.add(i,user.get(i).Id );
        }
    }
    public ListModel ReturnModel (){
        return data;
    }
    public List<Integer> ID ()
    {
        return Id;
    }
}
