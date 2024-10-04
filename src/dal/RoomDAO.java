/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Room;

/**
 *
 * @author admin
 */
public class RoomDAO extends DBContext {

    // CREATE: Thêm một phòng mới vào cơ sở dữ liệu
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO Room (name, area, capacity, in_use_status, floor_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room.getName());
            statement.setDouble(2, room.getArea());
            statement.setInt(3, room.getCapacity());
            statement.setBoolean(4, room.isInUseStatus());
            statement.setInt(5, room.getFloorId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ: Lấy thông tin phòng theo ID
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM Room WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("area"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("in_use_status"),
                        resultSet.getInt("floor_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Room getRoomByNameandFloor(String name, int floor_id) {
        String sql = "SELECT * FROM Room WHERE name = ? and floor_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(1, floor_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("area"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("in_use_status"),
                        resultSet.getInt("floor_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Room> getRoomByName(String name) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE name like = \"%" + name + "%\"";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Room room = new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("area"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("in_use_status"),
                        resultSet.getInt("floor_id")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
    
     public Room getRoomByNameAndFloor(String roomName, String floorName) {
        String sql = "SELECT room.id, room.name, room.area, room.capacity, room.in_use_status, room.floor_id "
                + "FROM apartment.room "
                + "JOIN apartment.floor ON room.floor_id = floor.id "
                + "WHERE room.name = ? AND floor.name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roomName);
            statement.setString(2, floorName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("area"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("in_use_status"),
                        resultSet.getInt("floor_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
// "SELECT 	room.name AS room_name, floor.name AS floor_name, room.area, room.capacity FROM apartment.room JOIN apartment.floor ON (room.floor_id = floor.id) WHERE (floor.in_use_status = TRUE);"
    // READ: Lấy danh sách tất cả các phòng

    public String getAllRooms() {
        String res = "";
        String sql = "SELECT room.name AS room_name, floor.name AS floor_name, room.area, room.capacity FROM apartment.room JOIN apartment.floor ON (room.floor_id = floor.id) WHERE (floor.in_use_status = TRUE);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                res
                        += resultSet.getString(1) + "$"
                        + resultSet.getString(2) + "$"
                        + resultSet.getDouble(3) + "$"
                        + resultSet.getInt(4) + "$";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String getAllRooms(String name, String areamin, String areamax, String capacitymin, String capacitymax, String floorName) {
        String res = "";
        String sql = "SELECT room.name AS room_name, floor.name AS floor_name, room.area, room.capacity "
                + "FROM apartment.room "
                + "JOIN apartment.floor ON (room.floor_id = floor.id) "
                + "WHERE (floor.in_use_status = TRUE) ";

        // Thêm các điều kiện vào câu truy vấn dựa trên các tham số đầu vào
        if (!"none".equals(name)) {
            sql += "AND room.name LIKE \"%"+ name + "%\" ";
        }
        if (!"none".equals(areamin)) {
            sql += "AND room.area >= " + areamin + " ";
        }
        if (!"none".equals(areamax)) {
            sql += "AND room.area <= " + areamax + " ";
        }
        if (!"none".equals(capacitymin)) {
            sql += "AND room.capacity >= " + capacitymin +  " ";
        }
        if (!"none".equals(capacitymax)) {
            sql += "AND room.capacity <= " + capacitymax + " ";
        }
        if (!"none".equals(floorName)) {
            sql += "AND floor.name = \"" + floorName + "\" ";
        }
        
//        System.out.println(sql);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                res += resultSet.getString(1) + "$"
                        + resultSet.getString(2) + "$"
                        + resultSet.getDouble(3) + "$"
                        + resultSet.getInt(4) + "$";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    // UPDATE: Cập nhật thông tin phòng
    public boolean updateRoom(Room room) {
        String sql = "UPDATE Room SET name = ?, area = ?, capacity = ?, in_use_status = ?, floor_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room.getName());
            statement.setDouble(2, room.getArea());
            statement.setInt(3, room.getCapacity());
            statement.setBoolean(4, room.isInUseStatus());
            statement.setInt(5, room.getFloorId());
            statement.setInt(6, room.getRoomId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: Xóa phòng theo ID
    public boolean deleteRoom(String roomName, String floorName) {
        String sql = "DELETE room FROM Room JOIN Floor ON (room.floor_id = floor.id) WHERE room.name = \"" + roomName+ "\" and floor.name = \"" + floorName + "\"";
        System.out.println(sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
