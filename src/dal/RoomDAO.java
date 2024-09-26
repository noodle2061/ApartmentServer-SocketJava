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
public class RoomDAO extends DBContext{
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

    // READ: Lấy danh sách tất cả các phòng
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room";
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
    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM Room WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
