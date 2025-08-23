/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partD;

/**
 *
 * @author Deneth
 */
public abstract class RoleHandler {
    protected RoleHandler next;

    public RoleHandler setNext(RoleHandler nextHandler) {
        this.next = nextHandler;
        return nextHandler;
    }

    // Template method: tries current handler; if not handled, delegates to next
    public final boolean handle(AccessRequest request) {
        if (canHandle(request)) {
            return true;
        }
        if (next != null) {
            return next.handle(request);
        }
        return false;
    }

    protected abstract boolean canHandle(AccessRequest request);
}