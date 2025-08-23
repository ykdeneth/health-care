/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partD;

import java.util.List;

/**
 *
 * @author Deneth
 */
public class AccessControlChainBuilder {
     // Build a chain from a list of roles ordered by escalation priority.
    // Example: ["Nurse", "Doctor", "Admin"] means try Nurse first, then Doctor, then Admin.
    public static RoleHandler buildChain(List<String> rolesInOrder) {
        RoleHandler head = null;
        RoleHandler current = null;

        for (String role : rolesInOrder) {
            RoleHandler handler = createHandler(role);
            if (handler == null) continue;

            if (head == null) {
                head = handler;
                current = handler;
            } else {
                current = current.setNext(handler);
            }
        }
        return head;
    }

    private static RoleHandler createHandler(String role) {
        switch (role.toLowerCase()) {
            case "doctor": return new DoctorHandler();
            case "nurse": return new NurseHandler();
            case "pharmacist": return new PharmacistHandler();
            case "admin": return new AdminHandler();
            default: return null;
        }
    }
}
