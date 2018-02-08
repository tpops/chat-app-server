/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ictrec.server;

import java.net.InetAddress;

/**
 *
 * @author POPOOLA
 */
public interface IServer {
     void Recieve();
   void Send (final byte[] data,final InetAddress Add,final int port);
}
