/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ictrec.server;

/**
 *
 * @author POPOOLA
 */
public class ServerMain {
   
    private static int port;
    private Server server;
    static String URL ="jdbc:derby://localhost:1527/BadGuysDB",User ="Badguys",PassWord ="akinola";
    public ServerMain (int port)
    {
        
        ServerMain.port = port;
        server = new Server(8192,URL,User,PassWord);
        System.out.println(port);
    }
    public static void  main (String [] args)
    {
//       
//        if (args.length !=1)
//        {
//            System.out.println("Usage: Java -jar BadGuysChat.jar [port]");
//            return ;
//        }
//        port = Integer.parseInt(args[0]);
        new ServerMain(8192);
    }
}
