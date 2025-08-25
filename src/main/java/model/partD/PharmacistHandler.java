/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partD;
import java.util.EnumSet;
import model.partD.staff.PermissionMatrix;
/**
 *
 * @author Deneth
 */


public class PharmacistHandler extends RoleHandler {
//    private static final EnumSet<Permission> ALLOWED = EnumSet.of(
//            Permission.VIEW_RECORDS
//            // Optionally add medication-dispense actions if modeled as permissions
//    );

    @Override
    protected boolean canHandle(AccessRequest request) {
        if (!"Pharmacist".equalsIgnoreCase(request.getRole())) return false;
       return PermissionMatrix.getPermissionsForRole("Pharmacist").contains(request.getPermission());
    }
}
