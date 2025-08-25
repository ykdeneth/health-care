/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partD.staff;

/**
 *
 * @author Deneth
 */
public class SessionManager {
    private Staff currentUser;

    public Staff getCurrentUser() {
        return currentUser;
    }

    public void login(Staff staff) {
        this.currentUser = staff;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String currentRole() {
        return isLoggedIn() ? currentUser.getRole() : null;
    }

    public String currentUserId() {
        return isLoggedIn() ? currentUser.getUserId() : null;
    }
}
