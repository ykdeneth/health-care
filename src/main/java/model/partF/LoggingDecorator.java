/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partF;

import java.time.LocalDateTime;

/**
 *
 * @author Deneth
 */
public class LoggingDecorator extends DataServiceDecorator {

    public LoggingDecorator(DataService wrappee) {
        super(wrappee);
    }

    @Override
    public byte[] getData() {
        System.out.println(LocalDateTime.now() + ": Accessing data");
        return wrappee.getData();
    }

    @Override
    public void saveData(byte[] data) {
        System.out.println(LocalDateTime.now() + ": Saving data");
        wrappee.saveData(data);
    }
}
