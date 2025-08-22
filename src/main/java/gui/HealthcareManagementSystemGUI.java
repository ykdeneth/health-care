package gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.FlatteningPathIterator;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.partA.Patient;
import model.partA.PatientCaretaker;
import model.partA.PatientMemento;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import javax.swing.UIManager;

/**
 *
 * @author Deneth
 */
public class HealthcareManagementSystemGUI extends JFrame {

    private Patient currentPatient;
    private PatientCaretaker caretaker;

    private final model.partA.PatientCaretaker caretaker2 = new model.partA.PatientCaretaker();
    private final model.partA.Patient currentPatient2 = new model.partA.Patient();
    DefaultTableModel historyTable;
    DefaultTableModel treatmentTable;

    public HealthcareManagementSystemGUI() {
        setTitle("Healthcare Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the Tabbed Pane container
        JTabbedPane tabbedPane = new JTabbedPane();

        // Patient Record Management Panel
        JPanel patientPanel = new JPanel(null);
        createPatientRecordPanel(patientPanel);
        tabbedPane.addTab("Patient Records", patientPanel);

        // Appointment Scheduling Panel
        JPanel appointmentPanel = new JPanel(null);
        createAppointmentPanel(appointmentPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);

        // Billing & Insurance Claims Panel
        JPanel billingPanel = new JPanel(null);
        createBillingPanel(billingPanel);
        tabbedPane.addTab("Billing, Prescriptions & Claims", billingPanel);

        // Role & Permission Management Panel
        JPanel rolePanel = new JPanel(null);
        createRolePermissionPanel(rolePanel);
        tabbedPane.addTab("Roles & Permissions", rolePanel);

        // Medical Report Generation Panel
        JPanel reportPanel = new JPanel(null);
        createReportGenerationPanel(reportPanel);
        tabbedPane.addTab("Reports", reportPanel);

        // Security Features Panel
        JPanel securityPanel = new JPanel(null);
//        securityPanel.setLayout(new BorderLayout());
//        securityPanel.add(new JLabel("Security features and audit log will be here."), BorderLayout.CENTER);
        createStaffRecordPanel(securityPanel);
        tabbedPane.addTab("Staff Schedules", securityPanel);

        JPanel inventoryPanel = new JPanel(null);
//        securityPanel.setLayout(new BorderLayout());
//        securityPanel.add(new JLabel("Security features and audit log will be here."), BorderLayout.CENTER);
        createMedicineStockManagement(inventoryPanel);
        tabbedPane.addTab("Medicine Stock Management", inventoryPanel);

        add(tabbedPane);
        currentPatient = new Patient();
        caretaker = new PatientCaretaker();
    }

    private void createPatientRecordPanel(JPanel panel) {
        panel.setLayout(new BorderLayout());

        // Inner tabbed pane within patientPanel
        JTabbedPane innerTabs = new JTabbedPane();

        // Create Register Patient tab
        JPanel registerPanel = new JPanel(null);
        createPatientRegistrationForm(registerPanel);
        innerTabs.addTab("Register Patient", registerPanel);

        JPanel medical = new JPanel(null);
        createPatientManagementUI2(medical);
        innerTabs.addTab("Treatments and Medical Notes", medical);
        // Create Manage Patients tab (existing patient record UI)
        JPanel managePanel = new JPanel(null);
        createPatientManagementUI(managePanel);
        innerTabs.addTab("Manage Patients", managePanel);

        panel.add(innerTabs, BorderLayout.CENTER);
    }

    private void createStaffRecordPanel(JPanel panel) {
        panel.setLayout(new BorderLayout());

        // Inner tabbed pane within patientPanel
        JTabbedPane innerTabs = new JTabbedPane();

        // Create Register Patient tab
        JPanel registerPanel = new JPanel(null);
        createStaffRegistrationForm(registerPanel);
        innerTabs.addTab("Register Staff", registerPanel);

        JPanel staff = new JPanel(null);
        createStaffSchedulePanel(staff);
        innerTabs.addTab("StaffSchedule", staff);
        // Create Manage Patients tab (existing patient record UI)

        panel.add(innerTabs, BorderLayout.CENTER);
    }

    private void createPatientRegistrationForm(JPanel panel) {
        JLabel lblName = new JLabel("Full Name:");
        lblName.setBounds(20, 20, 100, 25);
        panel.add(lblName);
        JTextField txtName = new JTextField();
        txtName.setBounds(130, 20, 200, 25);
        panel.add(txtName);

        JLabel lblDOB = new JLabel("Date of Birth:");
        lblDOB.setBounds(20, 60, 100, 25);
        panel.add(lblDOB);
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setBounds(130, 60, 150, 25);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        panel.add(dateSpinner);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(20, 100, 100, 25);
        panel.add(lblGender);
        JComboBox<String> cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cbGender.setBounds(130, 100, 120, 25);
        panel.add(cbGender);

        JLabel lblBlood = new JLabel("Blood Group:");
        lblBlood.setBounds(20, 140, 100, 25);
        panel.add(lblBlood);
        JComboBox<String> cbBloodGroup = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
        cbBloodGroup.setBounds(130, 140, 120, 25);
        panel.add(cbBloodGroup);

        JLabel lblContact = new JLabel("Contact Number:");
        lblContact.setBounds(20, 180, 120, 25);
        panel.add(lblContact);
        JTextField txtContact = new JTextField();
        txtContact.setBounds(150, 180, 180, 25);
        panel.add(txtContact);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(20, 220, 100, 25);
        panel.add(lblAddress);
        JTextArea txtAddress = new JTextArea();
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        JScrollPane scrollAddress = new JScrollPane(txtAddress);
        scrollAddress.setBounds(130, 220, 300, 70);
        panel.add(scrollAddress);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(130, 320, 100, 30);
        panel.add(btnRegister);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(250, 320, 100, 30);
        panel.add(btnClear);

        btnRegister.addActionListener(e -> {
            try {
                // 1. Collect values from GUI
                String name = txtName.getText();
                Date dob = (Date) dateSpinner.getValue();   // directly a Date
                String gender = cbGender.getSelectedItem().toString();
                String bloodGroup = cbBloodGroup.getSelectedItem().toString();
                String contact = txtContact.getText();
                String address = txtAddress.getText(); // You can later add address to entity if needed

                // 2. Create Patient entity
                Patient patient = new Patient();
                patient.setName(name);
                patient.setDOB(dob);
                patient.setGender(gender);
                patient.setBloodGroup(bloodGroup);
                patient.setContact(contact);
                patient.setAddress(address);

                // 3. Persist using Hibernate
                org.hibernate.Session session = util.HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();

                session.persist(patient);

                session.getTransaction().commit();
                session.close();

                String key = String.valueOf(patient.getId());
                model.partA.PatientMemento snapshot = patient.saveToMemento();
                caretaker.saveState(key, snapshot);

                JOptionPane.showMessageDialog(panel, "Patient Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnClear.addActionListener(e -> {
            txtName.setText("");
//            txtDOB.setText("");
            cbGender.setSelectedIndex(0);
            cbBloodGroup.setSelectedIndex(0);
            txtContact.setText("");
        });
    }

    private void createStaffRegistrationForm(JPanel panel) {
        JLabel lblName = new JLabel("Full Name:");
        lblName.setBounds(20, 20, 100, 25);
        panel.add(lblName);
        JTextField txtName = new JTextField();
        txtName.setBounds(130, 20, 200, 25);
        panel.add(txtName);

        JLabel lblDOB = new JLabel("Date of Birth:");
        lblDOB.setBounds(20, 60, 100, 25);
        panel.add(lblDOB);
        JTextField txtDOB = new JTextField();
        txtDOB.setBounds(130, 60, 150, 25);
        panel.add(txtDOB);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(20, 100, 100, 25);
        panel.add(lblGender);
        JComboBox<String> cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cbGender.setBounds(130, 100, 120, 25);
        panel.add(cbGender);

        JLabel lblBlood = new JLabel("Role:");
        lblBlood.setBounds(20, 140, 100, 25);
        panel.add(lblBlood);
        JComboBox<String> cbBloodGroup = new JComboBox<>(new String[]{"Doctor", "Nurse", "Pharmacist", "Admin"});
        cbBloodGroup.setBounds(130, 140, 120, 25);
        panel.add(cbBloodGroup);

        JLabel lblContact = new JLabel("Contact Number:");
        lblContact.setBounds(20, 180, 120, 25);
        panel.add(lblContact);
        JTextField txtContact = new JTextField();
        txtContact.setBounds(150, 180, 180, 25);
        panel.add(txtContact);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(130, 220, 100, 30);
        panel.add(btnRegister);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(250, 220, 100, 30);
        panel.add(btnClear);

        btnRegister.addActionListener(e -> {
            // Add patient registration logic here or validate input
            JOptionPane.showMessageDialog(panel, "Patient Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        btnClear.addActionListener(e -> {
            txtName.setText("");
            txtDOB.setText("");
            cbGender.setSelectedIndex(0);
            cbBloodGroup.setSelectedIndex(0);
            txtContact.setText("");
        });
    }

    private void createMedicineStockManagement(JPanel panel) {
        JLabel lblName = new JLabel("Drug Name:");
        lblName.setBounds(20, 20, 100, 25);
        panel.add(lblName);
        JTextField txtName = new JTextField();
        txtName.setBounds(130, 20, 200, 25);
        panel.add(txtName);

        JLabel lblDOB = new JLabel("Qty:");
        lblDOB.setBounds(20, 60, 100, 25);
        panel.add(lblDOB);
        JTextField txtQY = new JTextField();
        txtQY.setBounds(130, 60, 150, 25);
        panel.add(txtQY);

        JLabel lblGender = new JLabel("Price Per Unit:");
        lblGender.setBounds(20, 100, 100, 25);
        panel.add(lblGender);
        JTextField txtPU = new JTextField();
        txtPU.setBounds(130, 100, 150, 25);
        panel.add(txtPU);

        JLabel lblBlood = new JLabel("Supplier:");
        lblBlood.setBounds(20, 140, 100, 25);
        panel.add(lblBlood);
        JTextField txtSUP = new JTextField();
        txtSUP.setBounds(130, 140, 150, 25);
        panel.add(txtSUP);

        JLabel lblContact = new JLabel("Total Amount:");
        lblContact.setBounds(20, 180, 120, 25);
        panel.add(lblContact);
        JTextField txtAM = new JTextField();
        txtAM.setBounds(150, 180, 180, 25);
        panel.add(txtAM);
        txtAM.setEditable(false);

//        try {
//            double price = Double.parseDouble(txtPU.getText().trim());
//            int qty = Integer.parseInt(txtQY.getText().trim());
//            double total = price * qty;
//            txtAM.setText(String.valueOf(total));
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(panel, "Total Wrong", "Error", JOptionPane.ERROR_MESSAGE);
//        }
        JButton btnRegister = new JButton("Add");
        btnRegister.setBounds(130, 220, 100, 30);
        panel.add(btnRegister);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(250, 220, 100, 30);
        panel.add(btnClear);

//        btnRegister.addActionListener(e -> {
//            // Add patient registration logic here or validate input
//            JOptionPane.showMessageDialog(panel, "Patient Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
//        });
        String[] columnNames = {"Drug ID", "Drug Name", "Unit Price", "QTY"};
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(400, 20, 450, 230); // right side of form
        panel.add(scrollPane);

        // --- Add Button Action ---
        btnRegister.addActionListener(e -> {
            String name = txtName.getText().trim();
            String qty = txtQY.getText().trim();
            String price = txtPU.getText().trim();

            double price2 = Double.parseDouble(txtPU.getText().trim());
            int qty2 = Integer.parseInt(txtQY.getText().trim());
            double total2 = price2 * qty2;

            // Set total into txtAM
            txtAM.setText(String.valueOf(total2));

            if (name.isEmpty() || qty.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Auto-generate Drug ID based on row count
            String drugId = "D" + String.format("%03d", tableModel.getRowCount() + 1);

            // Add row to table
            tableModel.addRow(new Object[]{drugId, name, price, qty});

            JOptionPane.showMessageDialog(panel, "Drug added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        btnClear.addActionListener(e -> {
            txtName.setText("");
            txtQY.setText("");
            txtPU.setText("");
            txtSUP.setText("");
            txtAM.setText("");
        });
    }

    private void createPatientManagementUI(JPanel panel) {
        JLabel lblSearch = new JLabel("Patient ID:");
        lblSearch.setBounds(20, 20, 100, 25);
        panel.add(lblSearch);

        JTextField txtPatientId = new JTextField();
        txtPatientId.setBounds(130, 20, 150, 25);
        panel.add(txtPatientId);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(300, 20, 100, 25);
        panel.add(btnSearch);

        // ... Add other patient management UI components as before
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(20, 60, 100, 25);
        panel.add(lblName);
        JTextField txtName = new JTextField();
        txtName.setBounds(130, 60, 200, 25);
        panel.add(txtName);

        JLabel lblDOB = new JLabel("Date of Birth:");
        lblDOB.setBounds(20, 100, 100, 25);
        panel.add(lblDOB);
        JTextField txtDOB = new JTextField();
        txtDOB.setBounds(130, 100, 150, 25);
        panel.add(txtDOB);

//        JLabel lblDate = new JLabel("Select Date:");
//        lblDate.setBounds(300, 100, 100, 25);
//        panel.add(lblDate);
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setBounds(300, 100, 150, 25);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        panel.add(dateSpinner);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(20, 140, 100, 25);
        panel.add(lblGender);
        JComboBox<String> cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cbGender.setBounds(130, 140, 100, 25);
        panel.add(cbGender);

        JLabel lblBlood = new JLabel("Blood Group:");
        lblBlood.setBounds(20, 180, 100, 25);
        panel.add(lblBlood);
        JComboBox<String> cbBloodGroup = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
        cbBloodGroup.setBounds(130, 180, 100, 25);
        panel.add(cbBloodGroup);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(20, 220, 100, 25);
        panel.add(lblAddress);
        JTextArea txtAddress = new JTextArea();
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        JScrollPane scrollAddress = new JScrollPane(txtAddress);
        scrollAddress.setBounds(130, 220, 300, 70);
        panel.add(scrollAddress);

        // Replace Medical History text area with a scrollable JTable
        JLabel lblHistory = new JLabel("Medical History:");
        lblHistory.setBounds(460, 20, 120, 25);
        panel.add(lblHistory);

        String[] historyColumns = {"Date", "Condition", "Doctor Notes"};
        Object[][] historyData = { //            {"2025-01-10", "Hypertension", "Prescribed medication A"},
        //            {"2025-03-15", "Diabetes", "Recommended diet control"}
        };
        historyTable = new DefaultTableModel(historyData, historyColumns);
        JTable historyTable2 = new JTable(historyTable);
        historyTable2.setFillsViewportHeight(true);
        JScrollPane scrollHistory = new JScrollPane(historyTable2);
        scrollHistory.setBounds(460, 50, 350, 110);
        panel.add(scrollHistory);

// Replace Allergies text area with a scrollable JTable renamed as Treatment Plans
        JLabel lblTreatment = new JLabel("Treatment Plans:");
        lblTreatment.setBounds(460, 170, 120, 25);
        panel.add(lblTreatment);

        String[] treatmentColumns = {"Start Date", "End Date", "Treatment Description", "Status"};
        Object[][] treatmentData = { //            {"2025-04-01", "2025-04-15", "Antibiotic course", "Completed"},
        //            {"2025-05-01", "2025-06-01", "Physical Therapy", "Ongoing"}
        };
        treatmentTable = new DefaultTableModel(treatmentData, treatmentColumns);
        JTable treatmentTable2 = new JTable(treatmentTable);
        treatmentTable2.setFillsViewportHeight(true);
        JScrollPane scrollTreatment = new JScrollPane(treatmentTable2);
        scrollTreatment.setBounds(460, 200, 350, 90);
        panel.add(scrollTreatment);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(130, 310, 100, 30);
        panel.add(btnSave);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBounds(250, 310, 100, 30);
        panel.add(btnEdit);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(370, 310, 100, 30);
        panel.add(btnClear);

        btnSearch.addActionListener(e -> {
            String patientID = txtPatientId.getText().trim();
            if (patientID.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter Patient ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PatientMemento memento = caretaker.getState(patientID);
            if (memento != null) {
                currentPatient.restoreFromMemento(memento);

                txtName.setText(currentPatient.getName());

//                Date today = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(currentPatient.getDOB());

                txtDOB.setText(formattedDate);
                cbGender.setSelectedItem(currentPatient.getGender());
                cbBloodGroup.setSelectedItem(currentPatient.getBloodGroup());

                txtAddress.setText(currentPatient.getAddress());

                // If you have tables for history/treatments, reload them from currentPatient:
                historyTable.setRowCount(0);
                for (String[] row : currentPatient.getMedicalHistory()) {
                    historyTable.addRow(row);
                }
                //

                treatmentTable.setRowCount(0);
                for (String[] row : currentPatient.getTreatmentPlans()) {
                    treatmentTable.addRow(row);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "No saved data for this patient.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnSave.addActionListener(e -> {

            currentPatient.setName(txtName.getText());
            currentPatient.setDOB((Date) dateSpinner.getValue());
            currentPatient.setGender(cbGender.getSelectedItem().toString());
            currentPatient.setBloodGroup(cbBloodGroup.getSelectedItem().toString());
            currentPatient.setAddress(txtAddress.getText());
            String patientID = txtPatientId.getText().trim();
            if (!patientID.isEmpty()) {
                caretaker.saveState(patientID, currentPatient.saveToMemento());
                JOptionPane.showMessageDialog(panel, "Patient Data Saved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panel, "Please enter Patient ID before saving.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

    }

    private void createPatientManagementUI2(JPanel panel) {
        JLabel lblSearch = new JLabel("Patient ID:");
        lblSearch.setBounds(20, 20, 100, 25);
        panel.add(lblSearch);

        JTextField txtPatientId = new JTextField();
        txtPatientId.setBounds(130, 20, 150, 25);
        panel.add(txtPatientId);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(300, 20, 100, 25);
        panel.add(btnSearch);

        // ... Add other patient management UI components as before
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(440, 20, 100, 25);
        panel.add(lblName);
        JTextField txtName = new JTextField();
        txtName.setBounds(500, 20, 200, 25);
        panel.add(txtName);

        // Replace Medical History text area with a scrollable JTable
        JLabel lblHistory = new JLabel("Medical Notes:");
        lblHistory.setBounds(20, 60, 120, 25);
        panel.add(lblHistory);

        JTextArea txtAddress = new JTextArea();
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        JScrollPane scrollAddress = new JScrollPane(txtAddress);
        scrollAddress.setBounds(130, 60, 300, 70);
        panel.add(scrollAddress);

        JLabel lblCondition = new JLabel("Condition:");
        lblCondition.setBounds(460, 60, 100, 25);
        panel.add(lblCondition);
        JTextField txtCondition = new JTextField();
        txtCondition.setBounds(530, 60, 200, 25);
        panel.add(txtCondition);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBounds(760, 60, 60, 25);
        panel.add(btnAdd);

        JLabel lblTreatment = new JLabel("Treatment Plans:");
        lblTreatment.setBounds(20, 160, 120, 25);
        panel.add(lblTreatment);

        JTextArea txtAddress2 = new JTextArea();
        txtAddress2.setLineWrap(true);
        txtAddress2.setWrapStyleWord(true);
        JScrollPane scrollAddress2 = new JScrollPane(txtAddress2);
        scrollAddress2.setBounds(130, 160, 300, 70);
        panel.add(scrollAddress2);

        JLabel lblFrom = new JLabel("From:");
        lblFrom.setBounds(460, 150, 100, 25);
        panel.add(lblFrom);
        SpinnerDateModel fromDateModel = new SpinnerDateModel();
        JSpinner fromDateSpinner = new JSpinner(fromDateModel);
        JSpinner.DateEditor fromDateEditor = new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd");
        fromDateSpinner.setEditor(fromDateEditor);
        fromDateSpinner.setBounds(460, 170, 100, 25);  // set according to your layout
        panel.add(fromDateSpinner);

// Create "To Date" spinner
        JLabel lblTo = new JLabel("To:");
        lblTo.setBounds(460, 210, 100, 25);
        panel.add(lblTo);
        SpinnerDateModel toDateModel = new SpinnerDateModel();
        JSpinner toDateSpinner = new JSpinner(toDateModel);
        JSpinner.DateEditor toDateEditor = new JSpinner.DateEditor(toDateSpinner, "yyyy-MM-dd");
        toDateSpinner.setEditor(toDateEditor);
        toDateSpinner.setBounds(460, 230, 100, 25);
        panel.add(toDateSpinner);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(600, 160, 60, 25);
        panel.add(lblStatus);
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Completed", "Ongoing", "Canceled"});
        cbStatus.setBounds(650, 160, 120, 25);
        panel.add(cbStatus);

        JButton btnAdd2 = new JButton("Add");
        btnAdd2.setBounds(790, 160, 60, 25);
        panel.add(btnAdd2);

        // Combo Box 1 - Category
        JLabel lblCategory = new JLabel("Service:");
        lblCategory.setBounds(20, 270, 100, 25);
        panel.add(lblCategory);

        JComboBox<String> cbCategory = new JComboBox<>(new String[]{"Medication", "Surgeries", "Diagnostics"});
        cbCategory.setBounds(130, 270, 150, 25);
        panel.add(cbCategory);

        // Combo Box 2 - Medicine Names (initially for Medication category)
        JLabel lblMedicine = new JLabel("Medicine Name:");
        lblMedicine.setBounds(20, 310, 100, 25);
        panel.add(lblMedicine);

        JComboBox<String> cbMedicine = new JComboBox<>();
        cbMedicine.setBounds(130, 310, 150, 25);
        panel.add(cbMedicine);

        // Populate medicine names for Medication by default
        String[] medications = {"Paracetamol", "Aspirin", "Ibuprofen"};
        String[] surgeries = {"Appendectomy", "Gallbladder Removal"};
        String[] diagnostics = {"Blood Test", "X-Ray"};

        // Function to update medicine combo based on category selection
        cbCategory.addActionListener(e -> {
            String selected = (String) cbCategory.getSelectedItem();
            cbMedicine.removeAllItems();
            String[] items = {};
            if ("Medication".equals(selected)) {
                items = medications;
            } else if ("Surgeries".equals(selected)) {
                items = surgeries;
            } else if ("Diagnostics".equals(selected)) {
                items = diagnostics;
            }

            for (String item : items) {
                cbMedicine.addItem(item);
            }
        });

        // Trigger initial population
        cbCategory.setSelectedIndex(0);

        // Quantity input
        JLabel lblQty = new JLabel("Quantity:");
        lblQty.setBounds(20, 350, 100, 25);
        panel.add(lblQty);

        JTextField txtQty = new JTextField();
        txtQty.setBounds(130, 350, 150, 25);
        panel.add(txtQty);

        // Add button
        JButton btnAdd3 = new JButton("Add");
        btnAdd3.setBounds(130, 390, 150, 30);
        panel.add(btnAdd3);

        JButton btnClearTable = new JButton("Clear Table");
        btnClearTable.setBounds(130, 450, 150, 30); // set appropriate bounds
        panel.add(btnClearTable);

        JButton btnSave = new JButton("Save and Send Prescription to Pharmacist");
        btnSave.setBounds(130, 490, 250, 30); // set appropriate bounds
        panel.add(btnSave);
        // Table to display added entries
        String[] columns = {"Service", "Name", "Quantity"};
        DefaultTableModel tableModel3 = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel3);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(320, 270, 540, 150);
        panel.add(scrollPane);

        //////////btn
        btnAdd.addActionListener(e -> {
            // date for history is typically "today" (or use a separate picker if you have one)
            Date date = (Date) new Date(); // reuse DOB spinner as 'note date' if acceptable
            String condition = txtCondition.getText().trim();
            String notes = txtAddress.getText().trim();

            if (condition.isEmpty() || notes.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Enter both Condition and Notes.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);

            // 1) Add to table UI
            historyTable.addRow(new Object[]{dateStr, condition, notes});

            // 2) Add to currentPatient’s list (Memento source)
            currentPatient.getMedicalHistory().add(new String[]{dateStr, condition, notes});

            // clear inputs
            txtCondition.setText("");
            txtAddress.setText("");
        });
        //////////btn
        //////////btn2
        btnAdd2.addActionListener(e -> {
            String startStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format((Date) fromDateSpinner.getValue());
            String endStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format((Date) toDateSpinner.getValue());
            String desc = txtAddress2.getText().trim();
            String status = (String) cbStatus.getSelectedItem();

            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Enter treatment description.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 1) Add to table UI
            treatmentTable.addRow(new Object[]{startStr, endStr, desc, status});

            // 2) Add to currentPatient’s list (Memento source)
            currentPatient.getTreatmentPlans().add(new String[]{startStr, endStr, desc, status});

            // clear inputs
            txtAddress2.setText("");
            // (keep dates and status as-is for convenience)
        });
        //////////btn2

        // Add button action to add selected row only when pressed
        btnAdd3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String category = (String) cbCategory.getSelectedItem();
                String medicine = (String) cbMedicine.getSelectedItem();
                String qtyStr = txtQty.getText();

                if (medicine == null || medicine.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select a name.");
                    return;
                }

                try {
                    int qty;
                    if (qtyStr.isEmpty()) {
                        qty = 0;
                    } else {
                        qty = Integer.parseInt(qtyStr);
                    }
                    if (qty < 0) {
                        JOptionPane.showMessageDialog(null, "Quantity must be positive.");
                        return;
                    }
                    // Add row to table

                    tableModel3.addRow(new Object[]{category, medicine, qty});
                    txtQty.setText("");  // clear qty input
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Quantity must be a valid integer.");
                }
            }
        });
        btnClearTable.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);  // Clear all rows in the table
        });

    }

    private void createAppointmentPanel(JPanel panel) {
        // Appointment scheduling components
        JLabel lblPatient = new JLabel("Patient ID/Name:");
        lblPatient.setBounds(20, 20, 120, 25);
        panel.add(lblPatient);
        JTextField txtPatient = new JTextField();
        txtPatient.setBounds(150, 20, 200, 25);
        panel.add(txtPatient);

        JLabel lblDoctor = new JLabel("Select Doctor:");
        lblDoctor.setBounds(20, 60, 120, 25);
        panel.add(lblDoctor);
        JComboBox<String> cbDoctor = new JComboBox<>(new String[]{"Dr. Smith", "Dr. John", "Dr. Alice"});
        cbDoctor.setBounds(150, 60, 200, 25);
        panel.add(cbDoctor);

        JLabel lblDept = new JLabel("Department:");
        lblDept.setBounds(20, 100, 120, 25);
        panel.add(lblDept);
        JComboBox<String> cbDept = new JComboBox<>(new String[]{"Consultations", "Diagnostics", "Surgeries"});
        cbDept.setBounds(150, 100, 200, 25);
        panel.add(cbDept);

        JLabel lblFacility = new JLabel("Facility:");
        lblFacility.setBounds(20, 140, 120, 25);
        panel.add(lblFacility);
        JComboBox<String> cbFacility = new JComboBox<>(new String[]{"Main Hospital", "Clinic A", "Clinic B"});
        cbFacility.setBounds(150, 140, 200, 25);
        panel.add(cbFacility);

        JLabel lblDate = new JLabel("Appointment Date:");
        lblDate.setBounds(20, 180, 120, 25);
        panel.add(lblDate);
        JTextField txtDate = new JTextField("YYYY-MM-DD");
        txtDate.setBounds(150, 180, 150, 25);
        panel.add(txtDate);

        JLabel lblTime = new JLabel("Time Slot:");
        lblTime.setBounds(20, 220, 120, 25);
        panel.add(lblTime);
        JTextField txtTime = new JTextField("HH:MM");
        txtTime.setBounds(150, 220, 150, 25);
        panel.add(txtTime);

        JButton btnBook = new JButton("Book Appointment");
        btnBook.setBounds(150, 270, 180, 30);
        panel.add(btnBook);

        JButton btnCancel = new JButton("Cancel Appointment");
        btnCancel.setBounds(340, 270, 180, 30);
        panel.add(btnCancel);

        // Table for appointments
        String[] columns = {"ID", "Patient", "Doctor", "Dept", "Facility", "Date", "Time"};
        Object[][] data = {
            {"A001", "John Doe", "Dr. Smith", "Cardiology", "Main Hospital", "2025-08-21", "10:00"},
            {"A002", "Alice W.", "Dr. John", "Neurology", "Clinic A", "2025-08-22", "14:00"}
        };
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 320, 790, 200);
        panel.add(scrollPane);
    }

    private void createBillingPanel(JPanel panel) {
        JLabel lblPatient = new JLabel("Patient ID:");
        lblPatient.setBounds(20, 20, 100, 25);
        panel.add(lblPatient);
        JTextField txtPatientId = new JTextField();
        txtPatientId.setBounds(130, 20, 180, 25);
        panel.add(txtPatientId);

        JButton btnPsearch = new JButton("Search");
        btnPsearch.setBounds(340, 20, 120, 25);
        panel.add(btnPsearch);

        String[] columnsn = {"Service", "Name", "Quantity"};
        DefaultTableModel tableModel3 = new DefaultTableModel(columnsn, 0);
        JTable table = new JTable(tableModel3);
        JScrollPane scrollPane1 = new JScrollPane(table);
        scrollPane1.setBounds(20, 60, 540, 150);
        panel.add(scrollPane1);

        JLabel lblServiceCode = new JLabel("Main Service:");
        lblServiceCode.setBounds(20, 260, 100, 25);
        panel.add(lblServiceCode);

        JComboBox<String> cbService = new JComboBox<>(new String[]{"Consultations", "Treatments", "Medications"});
        cbService.setBounds(130, 260, 180, 25);
        panel.add(cbService);

//        JTextField txtServiceCode = new JTextField();
//        txtServiceCode.setBounds(130, 60, 180, 25);
//        panel.add(txtServiceCode);
        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setBounds(350, 260, 80, 25);
        panel.add(lblQuantity);
        JTextField txtQuantity = new JTextField();
        txtQuantity.setBounds(430, 260, 100, 25);
        panel.add(txtQuantity);

        JLabel lblUnitPrice = new JLabel("Unit Price:");
        lblUnitPrice.setBounds(550, 260, 80, 25);
        panel.add(lblUnitPrice);
        JTextField txtUnitPrice = new JTextField();
        txtUnitPrice.setBounds(630, 260, 100, 25);
        panel.add(txtUnitPrice);

        //////////////////
        JLabel lblCategory = new JLabel("Service:");
        lblCategory.setBounds(760, 260, 70, 25);
        panel.add(lblCategory);

        JComboBox<String> cbCategory = new JComboBox<>(new String[]{"Medication", "Surgeries", "Diagnostics"});
        cbCategory.setBounds(820, 260, 150, 25);
        panel.add(cbCategory);

        // Combo Box 2 - Medicine Names (initially for Medication category)
        JLabel lblMedicine = new JLabel("Medicine Name:");
        lblMedicine.setBounds(380, 310, 100, 25);
        panel.add(lblMedicine);

        JComboBox<String> cbMedicine = new JComboBox<>();
        cbMedicine.setBounds(500, 310, 150, 25);
        panel.add(cbMedicine);

        // Populate medicine names for Medication by default
        String[] medications = {"Paracetamol", "Aspirin", "Ibuprofen"};
        String[] surgeries = {"Appendectomy", "Gallbladder Removal"};
        String[] diagnostics = {"Blood Test", "X-Ray"};

        // Function to update medicine combo based on category selection
        cbCategory.addActionListener(e -> {
            String selected = (String) cbCategory.getSelectedItem();
            cbMedicine.removeAllItems();
            String[] items = {};
            if ("Medication".equals(selected)) {
                items = medications;
            } else if ("Surgeries".equals(selected)) {
                items = surgeries;
            } else if ("Diagnostics".equals(selected)) {
                items = diagnostics;
            }

            for (String item : items) {
                cbMedicine.addItem(item);
            }
        });

        // Trigger initial population
        cbCategory.setSelectedIndex(0);
        /////////////////

        JLabel lblInsurance = new JLabel("Insurance Provider:");
        lblInsurance.setBounds(20, 310, 130, 25);
        panel.add(lblInsurance);
        JComboBox<String> cbInsurance = new JComboBox<>(new String[]{"None", "Provider A", "Provider B"});
        cbInsurance.setBounds(150, 310, 200, 25);
        panel.add(cbInsurance);

        JButton btnAddService = new JButton("Add Service");
        btnAddService.setBounds(670, 310, 120, 25);
        panel.add(btnAddService);

        JButton btnSubmitClaim = new JButton("Submit Claim");
        btnSubmitClaim.setBounds(810, 310, 130, 25);
        panel.add(btnSubmitClaim);

        // Billing table
        String[] columns = {"Service Code", "Description", "Quantity", "Unit Price", "Total"};
        Object[][] data = {}; // Initially empty
        JTable billingTable = new JTable(new Object[0][11], columns);
        JScrollPane scrollPane = new JScrollPane(billingTable);
        scrollPane.setBounds(20, 350, 790, 200);
        panel.add(scrollPane);

        JLabel lblTotal = new JLabel("Total: $0.00");
        lblTotal.setBounds(680, 580, 120, 25);
        panel.add(lblTotal);
    }

    private void createRolePermissionPanel(JPanel panel) {
        JLabel lblUser = new JLabel("Select User:");
        lblUser.setBounds(20, 20, 100, 25);
        panel.add(lblUser);
        JComboBox<String> cbUser = new JComboBox<>(new String[]{"Dr. Smith", "Nurse Amy", "Admin"});
        cbUser.setBounds(130, 20, 180, 25);
        panel.add(cbUser);

        JLabel lblRole = new JLabel("Assign Role:");
        lblRole.setBounds(350, 20, 100, 25);
        panel.add(lblRole);
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"Doctor", "Nurse", "Pharmacist", "Admin"});
        cbRole.setBounds(450, 20, 180, 25);
        panel.add(cbRole);

        JButton btnAssign = new JButton("Assign Role");
        btnAssign.setBounds(650, 20, 120, 25);
        panel.add(btnAssign);

        // Permissions Table (Role vs Action)
        String[] columns = {"Action", "Doctor", "Nurse", "Pharmacist", "Admin"};
        Object[][] data = {
            {"View Records", true, true, true, true},
            {"Edit Records", true, true, false, true},
            {"Approve Claims", true, false, false, true},
            {"Generate Reports", true, true, true, true}
        };
        JTable permissionTable = new JTable(data, columns) {
            public Class getColumnClass(int column) {
                return (column == 0) ? String.class : Boolean.class;
            }
        };
        JScrollPane scrollPane = new JScrollPane(permissionTable);
        scrollPane.setBounds(20, 70, 790, 350);
        panel.add(scrollPane);
    }

    private void createReportGenerationPanel(JPanel panel) {
        JLabel lblPatient = new JLabel("Patient ID:");
        lblPatient.setBounds(20, 20, 100, 25);
        panel.add(lblPatient);
        JTextField txtPatientId = new JTextField();
        txtPatientId.setBounds(130, 20, 180, 25);
        panel.add(txtPatientId);

        JLabel lblFromDate = new JLabel("From (YYYY-MM-DD):");
        lblFromDate.setBounds(20, 60, 150, 25);
        panel.add(lblFromDate);
        JTextField txtFromDate = new JTextField();
        txtFromDate.setBounds(180, 60, 130, 25);
        panel.add(txtFromDate);

        JLabel lblToDate = new JLabel("To (YYYY-MM-DD):");
        lblToDate.setBounds(350, 60, 120, 25);
        panel.add(lblToDate);
        JTextField txtToDate = new JTextField();
        txtToDate.setBounds(470, 60, 130, 25);
        panel.add(txtToDate);

        JLabel lblReportType = new JLabel("Report Type:");
        lblReportType.setBounds(20, 100, 100, 25);
        panel.add(lblReportType);
        JComboBox<String> cbReportType = new JComboBox<>(new String[]{"Treatment Summary", "Diagnostic", "Financial"});
        cbReportType.setBounds(130, 100, 180, 25);
        panel.add(cbReportType);

        JButton btnGenerateReport = new JButton("Generate Report");
        btnGenerateReport.setBounds(350, 100, 150, 25);
        panel.add(btnGenerateReport);

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBounds(20, 150, 790, 350);
        panel.add(scrollPane);
    }

    private void createStaffSchedulePanel(JPanel panel) {
        JLabel lblUser = new JLabel("Select User:");
        lblUser.setBounds(350, 20, 100, 25);
//        lblUser.setBounds(20, 20, 100, 25);
        panel.add(lblUser);
        JComboBox<String> cbUser = new JComboBox<>(new String[]{"Dr. Smith", "Nurse Amy", "Admin"});
        cbUser.setBounds(450, 20, 180, 25);
//        cbUser.setBounds(130, 20, 180, 25);
        panel.add(cbUser);

        JLabel lblRole = new JLabel("Assign Role:");
        lblRole.setBounds(20, 20, 100, 25);
//        lblRole.setBounds(350, 20, 100, 25);
        panel.add(lblRole);
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"Doctor", "Nurse", "Pharmacist", "Admin"});
        cbRole.setBounds(130, 20, 180, 25);
//        cbRole.setBounds(450, 20, 180, 25);
        panel.add(cbRole);

//        JButton btnAssign = new JButton("Assign Role");
//        btnAssign.setBounds(650, 20, 120, 25);
//        panel.add(btnAssign);
        Map<String, String[]> usersByRole = new HashMap<>();
        usersByRole.put("Doctor", new String[]{"Dr. Smith", "Dr. Brown", "Dr. Johnson"});
        usersByRole.put("Nurse", new String[]{"Nurse Amy", "Nurse Bob"});
        usersByRole.put("Pharmacist", new String[]{"Pharmacist Joe", "Pharmacist Ann"});
        usersByRole.put("Admin", new String[]{"Admin Mike", "Admin Jill"});

// Populate user combo box based on initial role selected
        String selectedRole = (String) cbRole.getSelectedItem();
        if (selectedRole != null) {
            cbUser.setModel(new DefaultComboBoxModel<>(usersByRole.get(selectedRole)));
        }

// Add listener to role combo box to update users combo box whenever role changes
        cbRole.addActionListener(e -> {
            String role = (String) cbRole.getSelectedItem();
            if (role != null) {
                String[] users = usersByRole.get(role);
                if (users != null) {
                    cbUser.setModel(new DefaultComboBoxModel<>(users));
                } else {
                    cbUser.setModel(new DefaultComboBoxModel<>(new String[]{}));
                }
            }
        });

        ///////////////////////////////////////////////////////////
        // --- Date Picker ---
        JLabel lblDate = new JLabel("Select Date:");
        lblDate.setBounds(20, 60, 100, 25);
        panel.add(lblDate);

        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setBounds(130, 60, 150, 25);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        panel.add(dateSpinner);

        // --- Start Time Picker ---
        JLabel lblStart = new JLabel("Start Time:");
        lblStart.setBounds(20, 100, 100, 25);
        panel.add(lblStart);

        SpinnerDateModel startModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        JSpinner startSpinner = new JSpinner(startModel);
        startSpinner.setBounds(130, 100, 150, 25);
        startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "HH:mm"));
        panel.add(startSpinner);

        // --- End Time Picker ---
        JLabel lblEnd = new JLabel("End Time:");
        lblEnd.setBounds(20, 140, 100, 25);
        panel.add(lblEnd);

        SpinnerDateModel endModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        JSpinner endSpinner = new JSpinner(endModel);
        endSpinner.setBounds(130, 140, 150, 25);
        endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "HH:mm"));
        panel.add(endSpinner);

        // --- Button + Result ---
        JButton btnSave = new JButton("Save Schedule");
        btnSave.setBounds(20, 180, 150, 25);
        panel.add(btnSave);

        JLabel lblResult = new JLabel("");
        lblResult.setBounds(20, 220, 450, 25);
        panel.add(lblResult);

        btnSave.addActionListener(e -> {
            Date selectedDate = (Date) dateSpinner.getValue();
            Date startTime = (Date) startSpinner.getValue();
            Date endTime = (Date) endSpinner.getValue();

            // Convert to LocalDate + LocalTime
            LocalDate date = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime start = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime end = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            lblResult.setText("Date: " + date + ", From: " + start + " To: " + end);
        });
//            panel.setVisible(true);
        ///////////////////////////////////////////////////////////

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            // Set FlatLaf Light theme
            UIManager.setLookAndFeel(new FlatLightLaf());

            // Or set Dark theme
            // UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF: " + ex);
        }

        SwingUtilities.invokeLater(() -> {

            HealthcareManagementSystemGUI frame = new HealthcareManagementSystemGUI();
            frame.setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
