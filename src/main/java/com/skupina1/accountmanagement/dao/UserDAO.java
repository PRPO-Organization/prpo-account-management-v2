package com.skupina1.accountmanagement.dao;

import com.skupina1.accountmanagement.model.Location;
import com.skupina1.accountmanagement.model.User;
import com.skupina1.accountmanagement.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean Create(User user){
        String sql = "INSERT INTO users (email, password, first_name, last_name, latitude, longitude, address, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            Location loc = user.getLocation();
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            if(loc != null) {
                ps.setLong(5, user.getLocation().getLatitude());
                ps.setLong(6, user.getLocation().getLongitude());
            }else{
                ps.setLong(5, 0);
                ps.setLong(6, 0);

            }
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getRole());

            ps.executeUpdate();
            return true;
        }catch (SQLException e) {
            return false;
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Location loc = new Location(
                        rs.getLong("latitude"),
                        rs.getLong("longitude")
                );

                User u = new User();
                u.setRole((rs.getString("role")));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setAddress(rs.getString("address"));
                u.setLocation(loc);

                return u;
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<User> findByRole(String role){
        role = role.toUpperCase();
        if(!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("DRIVER")
                && !role.equalsIgnoreCase("CUSTOMER"))
            return new ArrayList<User>();

        String sql = "SELECT * FROM users WHERE role = ?";

        try(Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();

            List<User> users = new ArrayList<User>();

            while(rs.next()){
                Location loc = new Location(
                        rs.getLong("latitude"),
                        rs.getLong("longitude")
                );

                User u = new User();
                u.setRole((rs.getString("role")));
                u.setEmail(rs.getString("email"));
                u.setPassword(null);
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setAddress(rs.getString("address"));
                u.setLocation(loc);

                users.add(u);
            }

            return users;

        }catch (SQLException e){
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    public boolean delete(String email) {
        String sql = "DELETE FROM users WHERE email = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateUser(User user){
        String sql = "UPDATE users SET first_name = ?, last_name = ?, address = ?, " +
                "latitude = ?, longitude = ? WHERE email = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getAddress());
            ps.setLong(4, user.getLocation().getLatitude());
            ps.setLong(5, user.getLocation().getLongitude());
            ps.setString(6, user.getEmail());

            return ps.executeUpdate() >= 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
