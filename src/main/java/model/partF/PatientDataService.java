/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partF;

/**
 *
 * @author Deneth
 */
public class PatientDataService implements DataService {
    private byte[] storedData;

    @Override
    public byte[] getData() {
        return storedData;
    }

    @Override
    public void saveData(byte[] data) {
        this.storedData = data;
    }
}
