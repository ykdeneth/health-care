/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partD;

import java.util.EnumSet;

/**
 *
 * @author Deneth
 */
public class AdminHandler extends RoleHandler {
    private static final EnumSet<Permission> ALLOWED = EnumSet.allOf(Permission.class);

    @Override
    protected boolean canHandle(AccessRequest request) {
        if (!"Admin".equalsIgnoreCase(request.getRole())) return false;
        return ALLOWED.contains(request.getPermission());
    }
}
