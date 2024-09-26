/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package apartmentserver;

import controller.SocketHandle;
import java.net.DatagramSocket;

/**
 *
 * @author vankh
 */
public class ApartmentServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            DatagramSocket serverSocket = new DatagramSocket(9999);
            SocketHandle server = new SocketHandle(serverSocket);
            server.run();
        } catch (Exception e) {
        }
    }
    
}
