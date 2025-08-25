/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partF.chainOfRes;

/**
 *
 * @author Deneth
 */
public class AuthorizationHandler extends Handler {

    @Override
    public boolean handle(Request request) {
        // For demo, all authenticated users are authorized
        System.out.println("Authorization passed for resource: " + request.getResource());
        if (next != null) {
            return next.handle(request);
        }
        return true;
    }
}
