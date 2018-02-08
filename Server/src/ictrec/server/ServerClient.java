/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ictrec.server;
import java.net.*;
/**
 *
 * @author POPOOLA
 */
public class ServerClient {
    public String name;
    public InetAddress ip;
    public int port;
    public int Id;
    public int attempt = 0;
    // creating constructor for serverclient
    /**
     * Server Client is to Hold unique information of
     * each client that logs in
     */
    public ServerClient(String name, InetAddress ip,final int Id,int port)
    {
        this.name = name;
        this.port = port;
        this.Id = Id;
        this.ip = ip;
    }
       public int GetId()
               {
                   return Id;
               }
                
}
