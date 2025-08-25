/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partF.chainOfRes;

/**
 *
 * @author Deneth
 */
public class LoggingHandler extends Handler {

    @Override
    public boolean handle(Request request) {
        System.out.println("Access log - userToken: " + request.getUserToken() + ", resource: " + request.getResource());
        if (next != null) {
            return next.handle(request);
        }
        return true;
    }
}
