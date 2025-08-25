/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partF.chainOfRes;

/**
 *
 * @author Deneth
 */
public class Request {
    private String userToken;
    private String resource;

    public Request(String userToken, String resource) {
        this.userToken = userToken;
        this.resource = resource;
    }

    public String getUserToken() { return userToken; }
    public String getResource() { return resource; }
}
