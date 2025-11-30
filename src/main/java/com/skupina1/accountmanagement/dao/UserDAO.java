package com.skupina1.accountmanagement.dao;

import com.skupina1.accountmanagement.model.Location;
import com.skupina1.accountmanagement.model.User;
import com.skupina1.accountmanagement.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean Create(User user){
        String sql = "INSERT INTO users (email, password, first_name, last_name, address, role) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getRole());

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){  // should return id (i think)
                    long id = rs.getLong("id");
                    user.setId(id);
                    return true;
                }
            }

            return false;
        }catch (SQLException e) {
            System.out.println("SQL Create Error: " + e);
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

                User u = new User();
                u.setId(rs.getLong("id"));
                u.setRole((rs.getString("role")));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setAddress(rs.getString("address"));

                return u;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("SQL Find Error: " + e);
            return null;
        }
    }

    public User findById(long id){
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setEmail(rs.getString("email"));
                u.setPassword(null);
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setAddress(rs.getString("address"));
                u.setRole(rs.getString("role"));
                return u;
            }
            return null;
        }catch (SQLException e){
            System.out.println("SQL FindID Error: " + e);
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

                User u = new User();
                u.setId(rs.getLong("id"));
                u.setRole((rs.getString("role")));
                u.setEmail(rs.getString("email"));
                u.setPassword(null);
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setAddress(rs.getString("address"));

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
            System.out.println("SQL Error: " + e);
            return false;
        }
    }

    public boolean updateUser(User user){
        String sql = "UPDATE users SET first_name = ?, last_name = ?, address = ?, role = ? " +
                "WHERE email = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getEmail());

            return ps.executeUpdate() >= 1;

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e);
            return false;
        }
    }

    public boolean updateUserById(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, address = ?, role = ? " +
                "WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getRole());
            ps.setLong(5, user.getId());

            return ps.executeUpdate() >= 1;

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e);
            return false;
        }
    }
}
