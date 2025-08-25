/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partF.chainOfRes;

/**
 *
 * @author Deneth
 */
public class AuthHandler extends Handler {

    @Override
    public boolean handle(Request request) {
        // simulate token validation
        boolean valid = request.getUserToken() != null && request.getUserToken().length() > 3;
        if (!valid) {
            System.out.println("Authentication failed for token: " + request.getUserToken());
            return false;
        }
        if (next != null) {
            return next.handle(request);
        }
        return true;
    }
}
