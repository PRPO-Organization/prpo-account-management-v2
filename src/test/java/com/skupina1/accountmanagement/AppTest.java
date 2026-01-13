package com.skupina1.accountmanagement;

import com.skupina1.accountmanagement.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void userService_canBeCreated() {
        UserService newService = new UserService();
        assertNotNull(newService);
    }

}