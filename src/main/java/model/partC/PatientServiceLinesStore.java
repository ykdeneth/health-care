/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partC;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deneth
 */
public class PatientServiceLinesStore {
    private static final Map<String, List<BillingLine>> DB = new HashMap<>();

    public static synchronized void add(String patientId, String mainService, String description, int qty, BigDecimal unitPrice) {
        DB.computeIfAbsent(patientId, k -> new ArrayList<>())
          .add(new BillingLine(mainService, description, qty, unitPrice));
    }

    public static synchronized List<BillingLine> find(String patientId) {
        return new ArrayList<>(DB.getOrDefault(patientId, Collections.emptyList()));
    }

    public static synchronized void clear(String patientId) { DB.remove(patientId); }
}
