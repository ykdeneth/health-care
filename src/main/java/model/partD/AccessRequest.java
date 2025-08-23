/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partD;

/**
 *
 * @author Deneth
 */
public class AccessRequest {
    private final String userId;
    private final String role; // e.g., "Doctor", "Nurse", "Pharmacist", "Admin"
    private final Permission permission;

    public AccessRequest(String userId, String role, Permission permission) {
        this.userId = userId;
        this.role = role;
        this.permission = permission;
    }

    public String getUserId() { return userId; }
    public String getRole() { return role; }
    public Permission getPermission() { return permission; }
}
