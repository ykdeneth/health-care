/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partC;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Deneth
 */
public class BillingSummaryStore {

    private static final Map<String, List<BillingRecord>> STORE = new ConcurrentHashMap<>();

    /**
     * Record class holds summary and date.
     */
    public static class BillingRecord {
        private final BillingSummary summary;
        private final Date date;

        public BillingRecord(BillingSummary summary, Date date) {
            this.summary = summary;
            this.date = date;
        }

        public BillingSummary getSummary() {
            return summary;
        }

        public Date getDate() {
            return date;
        }
    }

    /**
     * Save a billing summary with patient id and date.
     * Thread-safe to allow concurrent saves.
     */
    public static void saveBillingSummary(String patientId, BillingSummary summary, Date date) {
        STORE.computeIfAbsent(patientId, pid -> new ArrayList<>())
             .add(new BillingRecord(summary, date));
    }

    /**
     * Retrieve all billing summaries for a patient.
     * Returns a copy to avoid external mutation.
     */
    public static List<BillingRecord> getBillingSummaries(String patientId) {
        List<BillingRecord> records = STORE.get(patientId);
        if (records == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(records);
    }
    
    /**
     * Clear all billing summaries for a patient.
     */
    public static void clearBillingSummaries(String patientId) {
        STORE.remove(patientId);
    }
}
