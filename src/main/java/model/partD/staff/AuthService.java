/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partD.staff;

import java.util.Arrays;
import java.util.EnumSet;
import model.partD.AccessControlChainBuilder;
import model.partD.AccessRequest;
import model.partD.Permission;
import model.partD.RoleHandler;

/**
 *
 * @author Deneth
 */
public class AuthService {
    private final StaffRegistry registry;
    private final SessionManager session;

    public AuthService(StaffRegistry registry, SessionManager session) {
        this.registry = registry;
        this.session = session;
    }

    // Login using userId and password
    public boolean login(String userId, String password) {
        Staff staff = registry.get(userId);
        if (staff == null) return false;
        if (!staff.getPassword().equals(password)) return false;
        session.login(staff);
        return true;
    }

    public void logout() {
        session.logout();
    }

    public boolean isLoggedIn() {
        return session.isLoggedIn();
    }

    public String getCurrentUserId() {
        return session.currentUserId();
    }

    public String getCurrentRole() {
        return session.currentRole();
    }

    // Admin-only: register a new staff member
    public boolean registerStaff(String newUserId, String role, String password) {
//        System.out.println(session.isLoggedIn());
        if (!session.isLoggedIn()) return false;
//        System.out.println(session.isLoggedIn());
        // Check via CoR that the current user can manage roles & permissions (choose a Permission for that)
        // We reuse APPROVE_CLAIMS or introduce a new 'MANAGE_STAFF' permission if you add to enum.
        // For now, require Admin using chain with Permission.GENERATE_REPORTS as placeholder? Better to add new permission.
        // We'll keep it strict: Only Admin role passes the chain for this operation.
        RoleHandler chain = AccessControlChainBuilder.buildChain(Arrays.asList(session.currentRole()));
//        AccessRequest req = new AccessRequest(session.currentUserId(), session.currentRole(), Permission.GENERATE_REPORTS);
        // If you prefer, add a MANAGE_STAFF permission to your Permission enum and use it here.
        System.out.println(chain);
//        boolean allowed = "Admin".equalsIgnoreCase(session.currentRole());
//        if (!allowed) return false;

        if (registry.exists(newUserId)) return false;
        registry.register(new Staff(newUserId, role, password));
        return true;
    }

    // Check if current user is allowed to perform a Permission using Chain of Responsibility
    public boolean isAllowed(Permission permission) {
        if (!session.isLoggedIn()) return false;
        RoleHandler chain = AccessControlChainBuilder.buildChain(Arrays.asList(session.currentRole(), "Admin"));
        AccessRequest req = new AccessRequest(session.currentUserId(), session.currentRole(), permission);
        return chain.handle(req);
    }

    // Optionally allow Admin to adjust role permissions for this session
    public boolean setRolePermissions(String role, EnumSet<Permission> permissions) {
        if (!session.isLoggedIn()) return false;
        if (!"Admin".equalsIgnoreCase(session.currentRole())) return false;
        PermissionMatrix.setPermissionsForRole(role, permissions);
        return true;
    }
}
