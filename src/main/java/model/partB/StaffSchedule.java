/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partB;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Deneth
 */
public class StaffSchedule {
    private final String staffName;
    // Map Date -> Set of available times
    private final Map<LocalDate, Set<LocalTime>> availableSlots = new HashMap<>();

    public StaffSchedule(String staffName) {
        this.staffName = staffName;
    }

    public void addAvailableSlot(LocalDate date, LocalTime start, LocalTime end) {
        Set<LocalTime> slots = availableSlots.computeIfAbsent(date, d -> new HashSet<>());
        LocalTime t = start;
        while (!t.isAfter(end.minusMinutes(1))) {
            slots.add(t);
            t = t.plusMinutes(30); // Slot size: 30min, can adjust
        }
    }

    public boolean isAvailable(LocalDate date, LocalTime time) {
        Set<LocalTime> slots = availableSlots.get(date);
        return slots != null && slots.contains(time);
    }

    public boolean reserveSlot(LocalDate date, LocalTime time) {
        Set<LocalTime> slots = availableSlots.get(date);
        return slots != null && slots.remove(time);
    }

    public void releaseSlot(LocalDate date, LocalTime time) {
        Set<LocalTime> slots = availableSlots.computeIfAbsent(date, d -> new HashSet<>());
        slots.add(time);
    }
}
