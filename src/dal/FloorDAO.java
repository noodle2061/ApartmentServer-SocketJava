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
import model.Floor;

/**
 *
 * @author admin
 */
public class FloorDAO extends DBContext {

    // CREATE: Thêm một tầng mới vào cơ sở dữ liệu
    public boolean addFloor(Floor floor) {
        String sql = "INSERT INTO Floor (name, description, in_use_status) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, floor.getName());
            statement.setString(2, floor.getDescription());
            statement.setBoolean(3, floor.isInUseStatus());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ: Lấy thông tin tầng theo ID
    public Floor getFloorById(int floorId) {
        String sql = "SELECT * FROM Floor WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, floorId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Floor(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getBoolean("in_use_status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Floor findFloorByName(String name) {
        String sql = "SELECT * FROM Floor WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Floor(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getBoolean("in_use_status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFloorByName(String name) {
        String sql = "SELECT * FROM Floor WHERE name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "\"%" + name + "%\"");
            ResultSet resultSet = statement.executeQuery();
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                String floorDetails = resultSet.getInt("id") + "$"
                        + resultSet.getString("name") + "$"
                        + resultSet.getString("description") + "$"
                        + resultSet.getBoolean("in_use_status");
                if (result.length() > 0) {
                    result.append("$");
                }
                result.append(floorDetails);
            }
            return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    // READ: Lấy danh sách tất cả các tầng
    public String getAllFloors() {
        String floors = "";
        String sql = "SELECT * FROM Floor where in_use_status = true";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                floors += ""
                        + resultSet.getString("name") + "$"
                        + resultSet.getString("description") + "$";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return floors;
    }

//    public List<Floor> getAllFloors() {
//        List<Floor> floors = new ArrayList<>();
//        String sql = "SELECT * FROM Floor";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Floor floor = new Floor(
//                    resultSet.getInt("id"),
//                    resultSet.getString("name"),
//                    resultSet.getString("description"),
//                    resultSet.getBoolean("in_use_status")
//                );
//                floors.add(floor);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return floors;
//    }
    // UPDATE: Cập nhật thông tin tầng
    public boolean updateFloor(Floor floor) {
        String sql = "UPDATE Floor SET name = ?, description = ?, in_use_status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, floor.getName());
            statement.setString(2, floor.getDescription());
            statement.setBoolean(3, floor.isInUseStatus());
            statement.setInt(4, floor.getFloorId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: Xóa tầng theo ID
    public boolean deleteFloor(String name) {
        String sql = "UPDATE Floor SET in_use_status = false WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
