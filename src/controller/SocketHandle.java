/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dal.RoomDAO;
import dal.UserDAO;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import model.User;

/**
 *
 * @author admin
 */
public class SocketHandle implements Runnable {

    private final DatagramSocket socket;

    public SocketHandle(DatagramSocket server) {
        this.socket = server;
    }


    @Override
    public void run() {
        try {
            UserDAO udb = new UserDAO();
            RoomDAO rdb = new RoomDAO();
            
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                // Sửa lại cách xử lý dữ liệu nhận được
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
                System.out.println("client: " + receivedMessage);
                String message = null;
                
                //xu ly
                String[] msg = receivedMessage.trim().split(" ");
                String req = msg[0];
                // xu ly dang nhap
                if (req.equals("login-request")) {
                    User u = udb.findByName(msg[1]);
                    if (u == null) {
                        message = "login-fail";
                    } else if (!u.getPassword().equals(msg[2])) {
                        message = "login-fail";
                    } else {
                        String name = u.getName();
                        message = "login-success " + name;
                    }
                }
                
                // xu ly dang ky
                if (req.equals("register-request")) {
                    String name = msg[1];
                    String pass = msg[2];
                    User u = udb.findByName(name);
                    if (u==null) {
                        udb.add(name, pass);
                        message = "register-success";
                    } else {
                        message = "register-fail";
                    }
                }
                //xử lý delete account
                else if (req.equals("delete-account-request")) {
                    String name = msg[1];
                    String pass = msg[2];
                    if (udb.delete(name, pass) == true) {
                        message = "delete-account-success";
                    } else {
                        message = "delete-account-fail";
                    }
                }
                
                else if (req.equals("change-password-request")) {
                    String name = msg[1];
                    String oldPassword = msg[2];
                    String newPassword = msg[3];
                    if (udb.updatePassword(name, oldPassword, newPassword)) {
                        message = "change-password-success " + name;
                    } else {
                        message = "change-password-fail";
                    }
                }
                
                else if (req.equals("open-search-room-frm")) {
                    String res = rdb.getAllRooms();
                    message = "open-search-room-frm-success$" + res;
                }
                
                else if (receivedMessage.startsWith("search-room-request")) {
                    msg = receivedMessage.split("\\$");
                    String name = msg[1];
                    String areamin = msg[2];
                    String areamax = msg[3];
                    String capacitymin = msg[4];
                    String capacitymax = msg[5];
                    String floorName = msg[6];
                    
                    String result = rdb.getAllRooms(name, areamin, areamax, capacitymin, capacitymax, floorName);
//                    System.out.println("result: " + result);
                    
                    message = "return-room-search$" + result;
                }
               // in ra console
                System.out.println("Server: " + message);
                
                // gui message di den Client
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket);
            }
        } catch (Exception e) {
        }

    }

}
