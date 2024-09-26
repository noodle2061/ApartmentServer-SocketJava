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
import model.User;

/**
 *
 * @author admin
 */
public class UserDAO extends DBContext{
    
    public boolean add(String name, String password) {
        String query = "INSERT INTO User (name, password) VALUES (?, ?)";
        
        User u = findByName(name);
        if (u!=null) return false; // ton tai ten roi
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, password);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false; // Trả về false nếu không thêm được
    }
    
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM User";
        
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                users.add(new User(id, name, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    public User findByName(String name) {
        String query = "SELECT * FROM User WHERE name = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String password = rs.getString("password");
                    return new User(id, name, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null; // Trả về null nếu không tìm thấy
    }
    
    public boolean updatePassword(String name, String oldPassword, String newPassword) {
    String query = "UPDATE User SET password = ? WHERE name = ? AND password = ?";
    
    try (PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, newPassword);  // New password
        ps.setString(2, name);         // Username
        ps.setString(3, oldPassword);  // Old password
        
        int rowsAffected = ps.executeUpdate();
        return (rowsAffected > 0);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    
    public boolean delete(String name, String password) {
        String query = "DELETE FROM User WHERE name = ? and password = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, password);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false; // Trả về false nếu không xóa được
    }
}
