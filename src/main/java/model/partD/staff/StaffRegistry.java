/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partD.staff;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Deneth
 */
public class StaffRegistry {
    // userId -> Staff
    private final Map<String, Staff> users = new HashMap<>();

    // Seed an initial admin for first login
    public StaffRegistry() {
        // default admin user: admin / admin
        users.put("admin", new Staff("admin", "Admin", "admin"));
    }

    public boolean exists(String userId) {
        return users.containsKey(userId);
    }

    public Staff get(String userId) {
        return users.get(userId);
    }

    public void register(Staff staff) {
        users.put(staff.getUserId(), staff);
    }

    public Map<String, Staff> snapshot() {
        return Collections.unmodifiableMap(users);
    }
}
