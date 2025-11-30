package com.skupina1.accountmanagement.service;

import com.skupina1.accountmanagement.dao.UserDAO;
import com.skupina1.accountmanagement.model.Location;
import com.skupina1.accountmanagement.model.UpdateUserRequest;
import com.skupina1.accountmanagement.model.User;
import com.skupina1.accountmanagement.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserService {
    private final UserDAO dao = new UserDAO();

    public boolean register(User user) {
        if (dao.findByEmail(user.getEmail()) != null)
            return false;

        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);

        if (user.getRole() == null)
            user.setRole("CUSTOMER");

        return dao.Create(user);
    }

    public String login(String email, String password) {
        User u = dao.findByEmail(email);
        if (u == null) return null;

        if (!BCrypt.checkpw(password, u.getPassword()))
            return null;

        return JwtUtil.genToken(u.getEmail(), u.getRole());
    }

    public User findUserByEmail(String email) { return dao.findByEmail(email); }

    public User findUserById(long id) { return dao.findById(id); }

    public boolean updateUser(String email, UpdateUserRequest request){
        User existing = dao.findByEmail(email);
        if(existing == null)
            return false;

        if(existing.getRole().equals("ADMIN"))
            return false;

        if(request.firstName != null) { existing.setFirstName(request.firstName); }
        if(request.lastName != null) { existing.setLastName(request.lastName); }
        if(request.address != null) { existing.setAddress(request.address); }

        return dao.updateUser(existing);
    }

    public boolean delete(String email) {
        return dao.delete(email);
    }

    public List<User> getUsersByRole(String role) { return dao.findByRole(role); }
}
