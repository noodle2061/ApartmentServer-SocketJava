/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dal.FloorDAO;
import dal.RoomDAO;
import dal.UserDAO;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import model.Floor;
import model.Room;
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
            FloorDAO fdb = new FloorDAO();

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
                    if (u == null) {
                        udb.add(name, pass);
                        message = "register-success";
                    } else {
                        message = "register-fail";
                    }
                } //xử lý delete account
                else if (req.equals("delete-account-request")) {
                    String name = msg[1];
                    String pass = msg[2];
                    if (udb.delete(name, pass) == true) {
                        message = "delete-account-success";
                    } else {
                        message = "delete-account-fail";
                    }
                } else if (req.equals("change-password-request")) {
                    String name = msg[1];
                    String oldPassword = msg[2];
                    String newPassword = msg[3];
                    if (udb.updatePassword(name, oldPassword, newPassword)) {
                        message = "change-password-success " + name;
                    } else {
                        message = "change-password-fail";
                    }
                } else if (req.equals("open-search-room-frm")) {
                    String res = rdb.getAllRooms();
                    message = "open-search-room-frm-success$" + res;
                } else if (receivedMessage.startsWith("search-room-request")) {
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
                } else if (receivedMessage.startsWith("delete-room-request")) {
                    msg = receivedMessage.split("\\$");
                    String roomName = msg[1];
                    String floorName = msg[2];
                    boolean result = rdb.deleteRoom(roomName, floorName);
                    String Rooms = rdb.getAllRooms();
                    message = "delete-room-response-success$" + Rooms;
                }
                
                else if (receivedMessage.startsWith("change-room-request")) {
                    msg = receivedMessage.split("\\$");
                    String roomName = msg[1];
                    String floorName = msg[2];
                    String name = msg[3];
                    String area =msg[4];
                    String capacity = msg[5];
                    
                    Room r = rdb.getRoomByNameAndFloor(roomName, floorName);
                    if (!name.equals("none")) {
                        r.setName(name);
                    }
                    if (!area.equals("none")) {
                        r.setArea(Double.parseDouble(area));
                    }
                    if (!capacity.equals("none")) {
                        r.setCapacity(Integer.parseInt(capacity));
                    }
                    rdb.updateRoom(r);
                    String Rooms = rdb.getAllRooms();
                    message = "modify-room-response-success$" + Rooms;
                }
                
                else if (receivedMessage.startsWith("get-all-floor-request")) {
                    String getAll = fdb.getAllFloors();
                    message = "get-all-floor-success$" + getAll;
                }
                
                else if (receivedMessage.startsWith("find-floor-request")){
                    msg = receivedMessage.split("\\$");
                    String res = fdb.getFloorByName(msg[1]);
                    message = "find-floor-success$" + res;
                }
                else if (receivedMessage.startsWith("delete-floor-request")){
                    msg = receivedMessage.split("\\$");
                    String name = msg[1];
                    String passVerify = msg[2];
                    String floorName = msg[3];
                    User u = udb.findByName(msg[1]);
                    if (!u.getPassword().equals(passVerify)) {
                        message = "delete-floor-request-fail";
                    } else if (fdb.deleteFloor(floorName)) {
                        String res = fdb.getAllFloors();
                        message = "delete-floor-request-success$" + res;
                    }else {
                        message = "delete-floor-request-fail";
                    }
                }
                
                else if (receivedMessage.startsWith("get-floor-close-request")) {
                    String res = fdb.getAllUnuseFloors();
                    message = "get-floor-close-success$" + res;
                }
                
                else if (receivedMessage.startsWith("add-floor-request")) {
                    msg = receivedMessage.split("\\$");
                    String floorName = msg[1];
                    fdb.addFloor(floorName);
                    String res = fdb.getAllFloors();
                    message = "add-floor-success$" + res;
                }
                
                else if (receivedMessage.startsWith("add-room-request")) {
                    msg = receivedMessage.split("\\$");
                    String floorName = msg[1];
                    String name = msg[2];
                    String area = msg[3];
                    String capacity = msg[4];
                    
                    Floor f = fdb.findFloorByName(floorName);
                    Room room = rdb.getRoomByNameandFloor(name, f.getFloorId());
                    if (room != null) {
                        message = "add-room-fail";
                    } else {
                        room = new Room(0, name, Double.parseDouble(area), Integer.parseInt(capacity), true, f.getFloorId());
                        rdb.addRoom(room);
                        message = "add-room-success";
                    }
                }
                
                else if (receivedMessage.startsWith("change-floor-describe-request")) {
                    msg = receivedMessage.split("\\$");
                    String floorName = msg[1];
                    String newDescription = msg[2];
                    
                    Floor f = fdb.findFloorByName(floorName);
                    f.setDescription(newDescription);
                    fdb.updateFloor(f);
                    
                    String res = fdb.getAllFloors();
                    message = "change-floor-describe-success$" + res;
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
