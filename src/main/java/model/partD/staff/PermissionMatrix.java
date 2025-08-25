/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partD.staff;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import model.partD.Permission;

/**
 *
 * @author Deneth
 */
public class PermissionMatrix {
    // Role -> Allowed Permissions
    private static final Map<String, EnumSet<Permission>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        // Defaults. You can adjust to match your table
        ROLE_PERMISSIONS.put("Doctor", EnumSet.of(
                Permission.VIEW_RECORDS,
                Permission.EDIT_RECORDS,
                Permission.GENERATE_REPORTS
        ));
        ROLE_PERMISSIONS.put("Nurse", EnumSet.of(
                Permission.VIEW_RECORDS,
                Permission.GENERATE_REPORTS
        ));
        ROLE_PERMISSIONS.put("Pharmacist", EnumSet.of(
                Permission.VIEW_RECORDS
        ));
        ROLE_PERMISSIONS.put("Admin", EnumSet.allOf(Permission.class));
    }

    public static EnumSet<Permission> getPermissionsForRole(String role) {
        EnumSet<Permission> set = ROLE_PERMISSIONS.get(role);
        return (set == null) ? EnumSet.noneOf(Permission.class) : EnumSet.copyOf(set);
    }

    // Optional: allow Admin to adjust role permissions at runtime (in-memory)
    public static void setPermissionsForRole(String role, EnumSet<Permission> permissions) {
        ROLE_PERMISSIONS.put(role, EnumSet.copyOf(permissions));
    }

    public static Map<String, EnumSet<Permission>> snapshot() {
        Map<String, EnumSet<Permission>> copy = new HashMap<>();
        ROLE_PERMISSIONS.forEach((k,v) -> copy.put(k, EnumSet.copyOf(v)));
        return Collections.unmodifiableMap(copy);
    }
}
