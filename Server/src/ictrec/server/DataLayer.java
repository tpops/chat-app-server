/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ictrec.server;
import java.net.InetAddress;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import com.Macropax.BadguysChat.Users;
import java.util.logging.Logger;
/**
 *
 * @author POPOOLA
 */
public class DataLayer {
    private int Id =-1;
    String AuthMessage;
    ResultSet rs;
    Statement st;
    Connection con ;
    String Url;
    String Name;
    String Password;
    public String getAuthMessage ()
    {
        return AuthMessage;
    }
    public DataLayer(String Url,String Name, String Password)
    {
        this.Url = Url;
        this.Name = Name;
        this.Password = Password;
        Connect();
    }
    public void SetStatus (int Id,String sTAT)
    {
        try {
            st.executeUpdate("Update PERSDETAILS "
                    + "SET STATUS ='"+sTAT+"'"
                            + "WHERE USERID ="+Id);
        } catch (SQLException ex) {
            Logger.getLogger(DataLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Users> GetFriends(int Id)
    {
         
        ResultSet rsG;
        List<Users> FriendList = new ArrayList<Users>();
        try {
            rsG = st.executeQuery("select F.FRIENDID , UF.USERNAME \n" +
"                    FROM FRIENDLIST as F                    \n" +
"                     JOIN USERS UF ON UF.USERID =F.FRIENDID \n" +
"                    WHERE F.USERID = " +Id);
              while(rsG.next())
        {           
           
            FriendList.add(new Users(rsG.getString(2),rsG.getInt(1),false));
        }
        } catch (SQLException ex) {
            Logger.getLogger(DataLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
      return FriendList;
    }
    public String GetStatus (int Id)
    {
        String Status="";
        ResultSet rsl;
        try {
            rsl = st.executeQuery("select STATUS FROM PERSDETAILS "
                    + "WHERE USERID = "+Id);
           while( rsl.next())
               Status=rsl.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DataLayer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Status Unavailable");
            return Status;
        }
        return Status;
    }
    
    public int GetId ()
    {
        return Id;
    }
    
    public boolean Authenticate (String UserName, String PassWord)
    {
        Id = -1;
        
        ResultSet rs;
        try {
            rs = st.executeQuery(" select USERID FROM USERS WHERE PASSWORD = '"+PassWord+"' AND\n" +
"USERNAME ='"+UserName+"'");
    
            while(rs.next())
            {
                Id= rs.getInt(1);
                System.out.println(rs.getInt("USERID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataLayer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt Authenticate");
            
        }
        
        if (Id == -1)
        {
            AuthMessage ="Invalid Username or Password";
            
            return false;
        }
       else
        {
            return true;
        }
    }
    private void Connect ()
    {
        try {
            con = DriverManager.getConnection(Url, Name, Password);
            st = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DataLayer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt connect to the database");
        }
        
    }
    /**
     * <summary>
     * Please Arrange Registeration details in this order
     * Username,Password,Country,Phonnumber,SEX
     * </summary>
     * @param RegDet 
     * @return String
     */
    public void RegisterationDet(List<String> RegDet)
    {
     
           ResultSet rsR;
        try {
            st.executeUpdate("insert into USERS (USERNAME,PASSWORD,COUNTRY,PHONENUM,SEX)"
                    + "VALUES("+"'"+RegDet.get(0)+"'"+","+"'"+RegDet.get(1) +"'"+","
                  +"'"+RegDet.get(2) +"'"+","+"'"+RegDet.get(3)+"'"+","+"'"+RegDet.get(4)+"'"+")"
                  );
            st.executeUpdate( "INSERT INTO PERSDETAILS (USERID,STATUS)"
                    + "VALUES ("+0+",'Hi am new to BadGuys Chat')");
           
        } catch (SQLException ex) {
            Logger.getLogger(DataLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
