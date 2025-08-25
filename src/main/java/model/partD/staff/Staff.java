/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partD.staff;

/**
 *
 * @author Deneth
 */
public class Staff {
    private final String userId; // unique username or id
    private String role;         // "Doctor", "Nurse", "Pharmacist", "Admin"
    private String password;     // plain for demo; do NOT store plain text in real apps

    public Staff(String userId, String role, String password) {
        this.userId = userId;
        this.role = role;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
