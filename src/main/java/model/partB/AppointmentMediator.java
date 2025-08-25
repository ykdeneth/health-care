/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deneth
 */
public class AppointmentMediator {
    // Map doctorName -> their schedule
    private final Map<String, StaffSchedule> schedules = new HashMap<>();
    private final List<Appointment> appointments = new ArrayList<>();

    public StaffSchedule getOrCreateSchedule(String staff) {
        return schedules.computeIfAbsent(staff, StaffSchedule::new);
    }

    public boolean bookAppointment(Appointment appt) {
        StaffSchedule schedule = getOrCreateSchedule(appt.getDoctor());

        // Real-time conflict & slot check
        if (!schedule.isAvailable(appt.getDate(), appt.getTime())) {
            return false; // Not available
        }
        schedule.reserveSlot(appt.getDate(), appt.getTime());
        appointments.add(appt);
        return true;
    }

    public boolean cancelAppointment(String apptId) {
        Iterator<Appointment> it = appointments.iterator();
        while (it.hasNext()) {
            Appointment appt = it.next();
            if (appt.getId().equals(apptId)) {
                // Release slot
                getOrCreateSchedule(appt.getDoctor()).releaseSlot(appt.getDate(), appt.getTime());
                it.remove();
                return true;
            }
        }
        return false;
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }
}
