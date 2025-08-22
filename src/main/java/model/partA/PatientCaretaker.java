
package model.partA;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Deneth
 */
public class PatientCaretaker {
    private Map<String, PatientMemento> savedStates = new HashMap<>();

    public void saveState(String patientID, PatientMemento memento) {
        savedStates.put(patientID, memento);
    }

    public PatientMemento getState(String patientID) {
        return savedStates.get(patientID);
    }
}
