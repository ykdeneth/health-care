package gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.FlatteningPathIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import model.Medicine;
import model.partB.Appointment;
import model.partB.AppointmentMediator;
import model.partB.StaffSchedule;
import model.partC.BillingAbstraction;
import model.partC.BillingEngineSelector;
import model.partC.BillingLine;
import model.partC.BillingSummary;
import model.partC.BillingSummaryStore;
import model.partC.PatientServiceLinesStore;
import model.partD.AccessControlChainBuilder;
import model.partD.AccessRequest;
import model.partD.Permission;
import model.partD.RoleHandler;
import model.partD.staff.AuthService;
import model.partD.staff.PermissionMatrix;
import model.partD.staff.SessionManager;
import model.partD.staff.StaffRegistry;
import model.partE.DiagnosticReportVisitor;
import model.partE.FinancialReportVisitor;
import model.partE.PatientElement;
import model.partE.TreatmentSummaryVisitor;
import model.partE.Visitor;
import model.partF.DataService;
import model.partF.EncryptionDecorator;
import model.partF.LoggingDecorator;
import model.partF.PatientDataService;
import model.partF.chainOfRes.AuthHandler;
import model.partF.chainOfRes.AuthorizationHandler;
import model.partF.chainOfRes.Handler;
import model.partF.chainOfRes.LoggingHandler;
import model.partF.chainOfRes.Request;
import net.sf.jasperreports.engine.JasperReport;

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
    int id = 1;
    private final Map<String, String> assignedRoles = new HashMap<>();
    private final StaffRegistry staffRegistry = new StaffRegistry();
    private final SessionManager sessionManager = new SessionManager();
    private final AuthService authService = new AuthService(staffRegistry, sessionManager);
    JComboBox<String> cbUserIDs;
    JComboBox<String> cbRolell;
    JComboBox<String> cbUserss;
    JComboBox<String> cbDoctor;
    private final AppointmentMediator mediator = new AppointmentMediator();
    Map<String, String[]> usersByRolell;
    private DataService patientDataService;
    private Handler securityChain;
    private List<Medicine> medicineList = new ArrayList<>();
    JTextField txtPatientId;
    private List<Patient> patients = new ArrayList<>();

    public HealthcareManagementSystemGUI() {
        initializeDataServices();
        initializeSecurityChain();
        showLoginDialog();

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

        JPanel signoutPanel = new JPanel(null);
        signOutPanel(signoutPanel);
        tabbedPane.addTab("Sign Out", signoutPanel);

        add(tabbedPane);
        currentPatient = new Patient();
        caretaker = new PatientCaretaker();

    }

    private void initializeDataServices() {
        DataService baseService = new PatientDataService();
        baseService = new EncryptionDecorator(baseService);
        baseService = new LoggingDecorator(baseService);

        this.patientDataService = baseService;
    }

    private void initializeSecurityChain() {
        Handler auth = new AuthHandler();
        Handler authorize = new AuthorizationHandler();
        Handler log = new LoggingHandler();

        auth.setNext(authorize).setNext(log);
        this.securityChain = auth;
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
            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.REGISTER_PATIENT);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to register patients.");
                return;
            }

            // Create security Request object using user token and resource
            String token = userIds;  // or any unique user session token
            Request req = new Request(token, "PatientRecordAccess");

            // Run through CoR chain to authenticate & authorize
            if (!securityChain.handle(req)) {
                JOptionPane.showMessageDialog(panel, "Access denied by security chain.");
                return;
            }

            try {
                int b = id++;
                // 1. Collect values from GUI
                String name = txtName.getText();
                Date dob = (Date) dateSpinner.getValue();   // directly a Date
                String gender = cbGender.getSelectedItem().toString();
                String bloodGroup = cbBloodGroup.getSelectedItem().toString();
                String contact = txtContact.getText();
                String address = txtAddress.getText(); // You can later add address to entity if needed

                // 2. Create Patient entity
                Patient patient = new Patient();
                patient.setId(b);
                patient.setName(name);
                patient.setDOB(dob);
                patient.setGender(gender);
                patient.setBloodGroup(bloodGroup);
                patient.setContact(contact);
                patient.setAddress(address);

                // 3. Persist using Hibernate
//                org.hibernate.Session session = util.HibernateUtil.getSessionFactory().openSession();
//                session.beginTransaction();
//
//                session.persist(patient);
//
//                session.getTransaction().commit();
//                session.close();
                String key = String.valueOf(patient.getId());
                model.partA.PatientMemento snapshot = patient.saveToMemento();
                caretaker.saveState(key, snapshot);

                // Serialize patient object to byte[] (you need to implement serialization)
                byte[] dataToSave = serializePatient(patient);

                // Save data securely using decorator wrapped service
                patientDataService.saveData(dataToSave);

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

    private byte[] serializePatient(Patient patient) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(patient);
            oos.flush();
            return bos.toByteArray();
        }
    }

    private Patient deserializePatient(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data); ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Patient) ois.readObject();
        }
    }

    private void createStaffRegistrationForm(JPanel panel) {
        JLabel lblId = new JLabel("User Id:");
        lblId.setBounds(20, 20, 100, 25);
        panel.add(lblId);
        JTextField txtId = new JTextField();
        txtId.setBounds(130, 20, 200, 25);
        panel.add(txtId);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(20, 60, 100, 25);
        panel.add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(130, 60, 180, 25);
        panel.add(txtPass);

        JLabel lblBlood = new JLabel("Role:");
        lblBlood.setBounds(20, 100, 100, 25);
        panel.add(lblBlood);
        JComboBox<String> cbNewRole = new JComboBox<>(new String[]{"Doctor", "Nurse", "Pharmacist", "Admin"});
        cbNewRole.setBounds(130, 100, 120, 25);
        panel.add(cbNewRole);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(20, 140, 100, 25);
        panel.add(lblName);
        JTextField txtName = new JTextField();
        txtName.setBounds(130, 140, 200, 25);
        panel.add(txtName);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(130, 220, 100, 30);
        panel.add(btnRegister);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(250, 220, 100, 30);
        panel.add(btnClear);
        System.out.println(sessionManager.isLoggedIn());
        System.out.println(sessionManager.currentRole());
        System.out.println(sessionManager.currentUserId());
        btnRegister.addActionListener(e -> {
            // Add patient registration logic here or validate input

            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.STAFF_REGISTER);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to register staff.");
                return;
            }

            String uid = txtId.getText().trim();
            String name = txtName.getText().trim();
            String role = (String) cbNewRole.getSelectedItem();
            String pwd = new String(txtPass.getPassword());

            boolean success = authService.registerStaff(uid, role, pwd);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registered " + uid + " as " + role);
                Set<String> allUserIds = staffRegistry.snapshot().keySet();
                cbUserIDs.removeAllItems();
                for (String userId : allUserIds) {
                    cbUserIDs.addItem(userId);
                }
                cbUserss.removeAllItems();
                for (String userId : allUserIds) {
                    cbUserss.addItem(userId);
                }
                cbDoctor.removeAllItems();
                for (String userId : allUserIds) {
                    cbDoctor.addItem(userId);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Registration failed (user exists or not authorized).");
            }
        });

        btnClear.addActionListener(e -> {
            txtName.setText("");
            txtId.setText("");
            cbNewRole.setSelectedIndex(0);
//            cbBloodGroup.setSelectedIndex(0);
            txtPass.setText("");
        });
    }

    public List<Medicine> getMedicines() {
        return new ArrayList<>(medicineList); // return a copy to prevent external modification
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
            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.MEDICINE_STOCKE_MANAGEMENT);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to manage medicine stock management.");
                return;
            }
            String name = txtName.getText().trim();
            String qty = txtQY.getText().trim();
            String price = txtPU.getText().trim();
            String supplier = txtSUP.getText().trim();

            double price2 = Double.parseDouble(txtPU.getText().trim());
            int qty2 = Integer.parseInt(txtQY.getText().trim());
            double total2 = price2 * qty2;

            // Set total into txtAM
            txtAM.setText(String.valueOf(total2));

            if (name.isEmpty() || qty.isEmpty() || price.isEmpty() || supplier.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Auto-generate Drug ID based on row count
            String drugId = "D" + String.format("%03d", tableModel.getRowCount() + 1);

            // Add row to table
            tableModel.addRow(new Object[]{drugId, name, price, qty});
            Medicine newMedicine = new Medicine(drugId, name, price2, qty2, supplier);
            medicineList.add(newMedicine);
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

        txtPatientId = new JTextField();
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
        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(250, 310, 100, 30);
        panel.add(btnClear);

        btnSearch.addActionListener(e -> {

            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.VIEW_RECORDS);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to view patient records.");
                return;
            }

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
            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.EDIT_RECORDS);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to edit patient records.");
                return;
            }
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
//        JLabel lblSearch = new JLabel("Patient ID:");
//        lblSearch.setBounds(20, 20, 100, 25);
//        panel.add(lblSearch);
//
//        JTextField txtPatientId = new JTextField();
//        txtPatientId.setBounds(130, 20, 150, 25);
//        panel.add(txtPatientId);
//
//        JButton btnSearch = new JButton("Search");
//        btnSearch.setBounds(300, 20, 100, 25);
//        panel.add(btnSearch);
//
//        // ... Add other patient management UI components as before
//        JLabel lblName = new JLabel("Name:");
//        lblName.setBounds(440, 20, 100, 25);
//        panel.add(lblName);
//        JTextField txtName = new JTextField();
//        txtName.setBounds(500, 20, 200, 25);
//        panel.add(txtName);

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
            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.TREATMENTS_AND_MEDICAL_NOTES);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to manage patient treatment and medical notes.");
                return;
            }
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
            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.TREATMENTS_AND_MEDICAL_NOTES);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to manage patient treatment and medical notes.");
                return;
            }

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

                String userIds = authService.getCurrentUserId();  // Current logged-in user ID
                String roles = authService.getCurrentRole();
                boolean allowed = isAllowedByChain(userIds, roles, Permission.TREATMENTS_AND_MEDICAL_NOTES);

                if (!allowed) {
                    JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to manage patient treatment and medical notes.");
                    return;
                }

                String category = (String) cbCategory.getSelectedItem();
                String medicine = (String) cbMedicine.getSelectedItem();
                String qtyStr = txtQty.getText();
                int qty2 = qtyStr.isEmpty() ? 0 : Integer.parseInt(qtyStr);

                String mainService = "Medications"; // since this panel explicitly handles Medication/Surgeries/Diagnostics
                String desc = medicine;
                if (medicine == null || medicine.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select a name.");
                    return;
                }
                if (category != null && !category.isBlank()) {
                    desc = medicine + " (" + category + ")";
                }

// You may not know unit price here; use 0 and allow billing to edit later
                java.math.BigDecimal unitPrice = java.math.BigDecimal.ZERO;
                String patientId = txtPatientId.getText();
                /* get the patient id for this panel context */

                PatientServiceLinesStore.add(patientId, mainService, desc, qty2, unitPrice);
                tableModel3.addRow(new Object[]{category, medicine, qty2});
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

        JLabel lblDoctor = new JLabel("Select Doctor/Staff:");
        lblDoctor.setBounds(20, 60, 120, 25);
        panel.add(lblDoctor);
        cbDoctor = new JComboBox<>(new String[]{"doctor1", "doctor2", "Dr. Alice"});
        cbDoctor.setBounds(150, 60, 200, 25);
        panel.add(cbDoctor);

        Set<String> allUserIds = staffRegistry.snapshot().keySet();
        cbDoctor.removeAllItems();
        for (String userId : allUserIds) {
            cbDoctor.addItem(userId);
        }

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
        DefaultTableModel apptModel = new DefaultTableModel(columns, 0);

        JTable table = new JTable(apptModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 320, 790, 200);
        panel.add(scrollPane);

        btnBook.addActionListener(e -> {
            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.MAKE_APPOINTMENT);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to to make appointments.");
                return;
            }

            String doctor = (String) cbDoctor.getSelectedItem();
            LocalDate apptDate = LocalDate.parse(txtDate.getText().trim());
            LocalTime apptTime = LocalTime.parse(txtTime.getText().trim());

// Check availability through mediator schedule
            StaffSchedule schedule = mediator.getOrCreateSchedule(doctor);
            if (!schedule.isAvailable(apptDate, apptTime)) {
                JOptionPane.showMessageDialog(panel, "Selected Doctor is not available at " + apptDate + " " + apptTime);
                return;
            }
// Prepare appointment (after collecting inputs and validating format)
            Appointment appt = new Appointment(
                    java.util.UUID.randomUUID().toString(),
                    txtPatient.getText().trim(),
                    cbDoctor.getSelectedItem().toString(),
                    cbDept.getSelectedItem().toString(),
                    cbFacility.getSelectedItem().toString(),
                    LocalDate.parse(txtDate.getText().trim()),
                    LocalTime.parse(txtTime.getText().trim())
            );
            boolean booked = mediator.bookAppointment(appt);
            if (booked) {
                apptModel.addRow(new Object[]{
                    appt.getId(), appt.getPatient(), appt.getDoctor(), appt.getDepartment(),
                    appt.getFacility(), appt.getDate().toString(), appt.getTime().toString()
                });
                JOptionPane.showMessageDialog(panel, "Appointment booked successfully.");
            } else {
                JOptionPane.showMessageDialog(panel, "Selected time is not available for this doctor.");
            }

        });
        btnCancel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String apptId = (String) apptModel.getValueAt(row, 0);
                boolean canceled = mediator.cancelAppointment(apptId);
                if (canceled) {
                    apptModel.removeRow(row);
                    JOptionPane.showMessageDialog(panel, "Appointment cancelled.");
                } else {
                    JOptionPane.showMessageDialog(panel, "Could not cancel appointment.");
                }
            }
        });
    }

    private void createBillingPanel(JPanel panel) {
        JLabel lblPatient = new JLabel("Patient ID:");
        lblPatient.setBounds(20, 20, 100, 25);
        panel.add(lblPatient);
        JTextField txtPatientId2 = new JTextField();
        txtPatientId2.setBounds(130, 20, 180, 25);
        panel.add(txtPatientId2);

        JButton btnPsearch = new JButton("Search");
        btnPsearch.setBounds(340, 20, 120, 25);
        panel.add(btnPsearch);

//        String[] columnsn = {"Service", "Name", "Quantity"};
//        DefaultTableModel tableModel3 = new DefaultTableModel(columnsn, 0);
//        JTable table = new JTable(tableModel3);
//        JScrollPane scrollPane1 = new JScrollPane(table);
//        scrollPane1.setBounds(20, 60, 540, 150);
//        panel.add(scrollPane1);
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
        lblQuantity.setBounds(760, 260, 80, 25);
        panel.add(lblQuantity);
        JTextField txtQuantity = new JTextField();
        txtQuantity.setBounds(820, 260, 100, 25);
        panel.add(txtQuantity);

        JLabel lblUnitPrice = new JLabel("Unit Price:");
        lblUnitPrice.setBounds(550, 260, 80, 25);
        panel.add(lblUnitPrice);
        JTextField txtUnitPrice = new JTextField();
        txtUnitPrice.setBounds(630, 260, 100, 25);
        panel.add(txtUnitPrice);

        //////////////////
        JLabel lblCategory = new JLabel("Service:");
        lblCategory.setBounds(350, 260, 70, 25);
        lblCategory.setVisible(false);
        panel.add(lblCategory);

        JComboBox<String> cbCategory = new JComboBox<>(new String[]{"Surgeries", "Diagnostics"});
        cbCategory.setBounds(410, 260, 130, 25);
        cbCategory.setVisible(false);
        panel.add(cbCategory);

        cbService.addActionListener(e -> {
            String selected = (String) cbService.getSelectedItem();
            boolean show = "Medications".equals(selected);
            lblCategory.setVisible(show);
            cbCategory.setVisible(show);

            // Refresh panel to update UI
            panel.revalidate();
            panel.repaint();
        });

        // Combo Box 2 - Medicine Names (initially for Medication category)
        JLabel lblMedicine = new JLabel("Description :");
        lblMedicine.setBounds(20, 310, 100, 25);
        panel.add(lblMedicine);

        JTextField txtDescription = new JTextField();
        txtDescription.setBounds(150, 310, 160, 25);
        panel.add(txtDescription);

        /////////////////
        JLabel lblInsurance = new JLabel("Insurance Provider:");
        lblInsurance.setBounds(380, 310, 130, 25);
        panel.add(lblInsurance);
        JTextField txtInsurance = new JTextField();
        txtInsurance.setBounds(500, 310, 120, 25);
        panel.add(txtInsurance);

        JButton btnAddService = new JButton("Add Service");
        btnAddService.setBounds(670, 310, 120, 25);
        panel.add(btnAddService);

        JButton btnSubmitClaim = new JButton("Submit Claim");
        btnSubmitClaim.setBounds(810, 310, 130, 25);
        panel.add(btnSubmitClaim);

        // Billing table
        String[] columns = {"Main Service ", "Description", "Quantity", "Unit Price", "Total"};
        Object[][] data = {}; // Initially empty
//        JTable billingTable = new JTable(new Object[0][11], columns);
        DefaultTableModel billingModel = new DefaultTableModel(columns, 0); // 0 rows initially
        JTable billingTable = new JTable(billingModel);
        JScrollPane scrollPane = new JScrollPane(billingTable);
        scrollPane.setBounds(20, 60, 790, 170);
        panel.add(scrollPane);

        JLabel lblTotal = new JLabel("Total: $0.00");
        lblTotal.setBounds(680, 580, 120, 25);
        panel.add(lblTotal);

        btnPsearch.addActionListener(e -> {
            String pid = txtPatientId2.getText().trim();
            if (pid.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Enter Patient ID.");
                return;
            }
            DefaultTableModel model = (DefaultTableModel) billingTable.getModel();
            model.setRowCount(0);
            List<BillingLine> lines = PatientServiceLinesStore.find(pid);
            BigDecimal sum = BigDecimal.ZERO;
            for (BillingLine l : lines) {
                model.addRow(new Object[]{l.getMainService(), l.getDescription(), l.getQuantity(), l.getUnitPrice(), l.getLineTotal()});
                sum = sum.add(l.getLineTotal());
            }
            lblTotal.setText("Total: $" + sum);
        });

        btnAddService.addActionListener(e -> {
            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.BILLING_AND_PRESCRIPTION);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to billing and prescription area.");
                return;
            }
            String pid = txtPatientId.getText().trim();
            if (pid.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Enter Patient ID first.");
                return;
            }

            String mainService = (String) cbService.getSelectedItem();              // Consultations | Treatments | Medications
            String description = txtDescription.getText().trim();

            if ("Medications".equals(mainService) && cbCategory.isVisible()) {
                String cat = (String) cbCategory.getSelectedItem();                 // Surgeries/Diagnostics when visible (per your toggle)
                if (cat != null && !cat.isBlank()) {
                    description = description.isBlank() ? cat : description + " (" + cat + ")";
                }
            }

            int qty;
            java.math.BigDecimal unitPrice;
            try {
                qty = Integer.parseInt(txtQuantity.getText().trim());
                unitPrice = new java.math.BigDecimal(txtUnitPrice.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Quantity and Unit Price must be numeric.");
                return;
            }

            // UI table add
            DefaultTableModel model = (javax.swing.table.DefaultTableModel) billingTable.getModel();
            BigDecimal total = unitPrice.multiply(java.math.BigDecimal.valueOf(qty));
            model.addRow(new Object[]{mainService, description, qty, unitPrice, total});

            // Persist for this patient
            PatientServiceLinesStore.add(pid, mainService, description, qty, unitPrice);

            // Update label
            updateBillingTotalFromTable((DefaultTableModel) billingTable.getModel(), lblTotal);
        });
        btnSubmitClaim.addActionListener(e -> {
            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.BILLING_AND_PRESCRIPTION);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to billing and prescription area.");
                return;
            }
            String pid = txtPatientId.getText().trim();
            String insurer = txtInsurance.getText().trim(); // empty = direct

            // Build a fresh billing session (Bridge abstraction)
            BillingAbstraction session = BillingEngineSelector.newSession(pid, insurer);

            DefaultTableModel model = (DefaultTableModel) billingTable.getModel();
            for (int r = 0; r < model.getRowCount(); r++) {
                String mainService = String.valueOf(model.getValueAt(r, 0));
                String desc = String.valueOf(model.getValueAt(r, 1));
                int qty = Integer.parseInt(String.valueOf(model.getValueAt(r, 2)));
                java.math.BigDecimal unit = new java.math.BigDecimal(String.valueOf(model.getValueAt(r, 3)));
                session.addLine(new BillingLine(mainService, desc, qty, unit));
            }

            BillingSummary summary = session.submit();

            javax.swing.JOptionPane.showMessageDialog(panel,
                    "Subtotal: " + summary.getSubTotal()
                    + "\nPatient pays: " + summary.getPatientPays()
                    + "\nInsurer pays: " + summary.getInsurerPays()
                    + (summary.getClaimReference() != null ? ("\nClaim Ref: " + summary.getClaimReference()) : "")
                    + (summary.getNote() != null ? ("\nNote: " + summary.getNote()) : "")
                    + "\nDate: " + new Date().toString()
            );
            BillingSummaryStore.saveBillingSummary(pid, summary, new Date());

        });
    }

    private void updateBillingTotalFromTable(DefaultTableModel model, JLabel lblTotal) {
        BigDecimal sum = java.math.BigDecimal.ZERO;
        for (int r = 0; r < model.getRowCount(); r++) {
            Object v = model.getValueAt(r, 4);
            if (v != null) {
                sum = sum.add(new java.math.BigDecimal(String.valueOf(v)));
            }
        }
        lblTotal.setText("Total: $" + sum);
    }

    private void createRolePermissionPanel(JPanel panel) {
        JLabel lblUser = new JLabel("Select User:");
        lblUser.setBounds(20, 20, 100, 25);
        panel.add(lblUser);
        cbUserIDs = new JComboBox<>();
        Set<String> allUserIds = staffRegistry.snapshot().keySet();
        cbUserIDs.setBounds(130, 20, 180, 25);
        panel.add(cbUserIDs);
        cbUserIDs.removeAllItems();
        for (String userId : allUserIds) {
            cbUserIDs.addItem(userId);
        }

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
            {"Reports", true, true, false, true},
            {"Register Patient", true, true, true, true},
            {"Treatement and Medical Notes", true, true, true, true},
            {"Make Appointment", true, true, true, true},
            {"Roles and Permissions", true, true, true, true},
            {"Billing and prescriptions", true, true, true, true},
            {"Staff Registration", true, true, true, true},
            {"Staff Schedule", true, true, true, true},
            {"Medicine Stock Management", true, true, true, true},};
        DefaultTableModel permissionTable = new DefaultTableModel(data, columns) {
//            public Class getColumnClass(int column) {
//                return (column == 0) ? String.class : Boolean.class;
//            }
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only role permission columns (1 to 4) are editable
                return column > 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 0) ? String.class : Boolean.class;
            }
        };
        JTable permissionTable2 = new JTable(permissionTable);
        JScrollPane scrollPane = new JScrollPane(permissionTable2);
        scrollPane.setBounds(20, 70, 790, 350);
        panel.add(scrollPane);

        loadPermissionsToTable(permissionTable, permissionTable2);

        JButton btnTestPermission = new JButton("Save Selected Action");
        btnTestPermission.setBounds(20, 430, 200, 30);
        panel.add(btnTestPermission);

//        JButton btnSavePermissions = new JButton("Save Permissions");
//        btnSavePermissions.setBounds(250, 430, 150, 30);
//        panel.add(btnSavePermissions);
        btnAssign.addActionListener(e -> {
            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.ROLES_AND_PERMISSIONS);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to manage roles and permissions.");
                return;
            }
            String user = (String) cbUserIDs.getSelectedItem();
            String role = (String) cbRole.getSelectedItem();
            if (user == null || role == null) {
                JOptionPane.showMessageDialog(panel, "Please select user and role.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            assignedRoles.put(user, role);
            JOptionPane.showMessageDialog(panel, "Assigned role '" + role + "' to user '" + user + "'.");
        });
//        btnTestPermission.addActionListener(e -> {
//            int row = permissionTable.getSelectedRow();
//            if (row < 0) {
//                JOptionPane.showMessageDialog(panel, "Please select an action row in the table.");
//                return;
//            }
//
//            String actionName = permissionTable.getValueAt(row, 0).toString(); // first column is action string
//            Permission permission = mapActionToPermission(actionName);
//            if (permission == null) {
//                JOptionPane.showMessageDialog(panel, "This action is not mapped to a permission in code.");
//                return;
//            }
//
//            String user = (String) cbUserIDs.getSelectedItem();
//            // If you want to use the assigned role, prefer that; otherwise use current selection from cbRole.
//            String role = assignedRoles.getOrDefault(user, (String) cbRole.getSelectedItem());
//
//            boolean allowed = isAllowedByChain(user, role, permission);
//            JOptionPane.showMessageDialog(panel,
//                    (allowed ? "ALLOWED" : "DENIED") + " for user=" + user + ", role=" + role + ", action=" + actionName);
//        });
        btnTestPermission.addActionListener(e -> {
            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roless = authService.getCurrentRole();
            boolean alloweds = isAllowedByChain(userIds, roless, Permission.BILLING_AND_PRESCRIPTION);

            if (!alloweds) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to billing and prescription area.");
                return;
            }
            String[] roles = {"Doctor", "Nurse", "Pharmacist", "Admin"};
            // We'll rebuild the sets based on table checkbox states
            Map<String, EnumSet<Permission>> newPermissions = new HashMap<>();

            for (int col = 1; col <= roles.length; col++) { // columns 1 to 4 (roles)
                EnumSet<Permission> perms = EnumSet.noneOf(Permission.class);
                String role = roles[col - 1];
                for (int row = 0; row < permissionTable.getRowCount(); row++) {
                    Boolean allowed = (Boolean) permissionTable.getValueAt(row, col);
                    if (allowed != null && allowed) {
                        String actionName = (String) permissionTable.getValueAt(row, 0);
                        Permission perm = mapActionToPermission(actionName);
                        if (perm != null) {
                            perms.add(perm);
                        }
                    }
                }
                newPermissions.put(role, perms);
            }

            // Update your in-memory PermissionMatrix (assumed imported)
            for (Map.Entry<String, EnumSet<Permission>> entry : newPermissions.entrySet()) {
                PermissionMatrix.setPermissionsForRole(entry.getKey(), entry.getValue());
            }

            JOptionPane.showMessageDialog(panel, "Permissions updated in memory for roles successfully.");
        });
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

        btnGenerateReport.addActionListener(e -> {
            String userIds = authService.getCurrentUserId();
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.GENERATE_REPORTS);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to generate reports.");
                return;
            }

            String patientIdStr = txtPatientId.getText().trim();
            if (patientIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter Patient ID.");
                return;
            }

            Date fromDate, toDate;
            try {
                fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(txtFromDate.getText().trim());
                toDate = new SimpleDateFormat("yyyy-MM-dd").parse(txtToDate.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Please enter valid From and To dates in yyyy-MM-dd format.");
                return;
            }
            String reportType = (String) cbReportType.getSelectedItem();

            // Obtain patient from your DB or in-memory store
//            Patient patient = loadPatientById(Integer.parseInt(patientIdStr));
            PatientMemento memento = caretaker.getState(patientIdStr);

          
            if (memento == null) {
                JOptionPane.showMessageDialog(panel, "Patient not found.");
                return;
            }
            currentPatient.restoreFromMemento(memento);
            Visitor visitor = null;
            switch (reportType) {
                case "Treatment Summary":
                    visitor = new TreatmentSummaryVisitor();
                    break;
                case "Diagnostic":
                    visitor = new DiagnosticReportVisitor();
                    break;
                case "Financial":
                    visitor = new FinancialReportVisitor(fromDate, toDate);
                    break;
            }

            if (visitor != null) {
                PatientElement patientElem = new PatientElement(currentPatient);
                patientElem.accept(visitor);

                // Preview report with print button
                openReportPreview(reportType, visitor.getReport());
            }
        });

    }

    private void signOutPanel(JPanel panel) {

        JButton btnSignOut = new JButton("Sign Out");
        btnSignOut.setBounds(400, 50, 150, 25);
        panel.add(btnSignOut);

        btnSignOut.addActionListener(e -> {

            logoutAndReLogin();

        });
    }

    private void logoutAndReLogin() {
        authService.logout();
        JOptionPane.showMessageDialog(this, "Signed out.");
        showLoginDialog();
    }

    private void showLoginDialog() {
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();

        Object[] message = {
            "User ID:", txtUser,
            "Password:", txtPass
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            Request req = new Request(user, "LoginAttempt");

            // Use your security chain for access check and logging attempts
            if (securityChain == null || !securityChain.handle(req)) {
                JOptionPane.showMessageDialog(this, "Access denied by security checks.");
                showLoginDialog(); // retry
                return;
            }

            boolean ok = authService.login(user, pass);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
                showLoginDialog(); // try again
            } else {
                JOptionPane.showMessageDialog(this, "Welcome, " + authService.getCurrentUserId() + " (" + authService.getCurrentRole() + ")");
                // Optionally enable tabs based on role, or just keep permission checks on actions
//                HealthcareManagementSystemGUI h = new HealthcareManagementSystemGUI();
//                h.setVisible(true);
//                this.dispose();

            }
        } else {
            System.exit(0); // no login -> exit
        }
    }

    private void createStaffSchedulePanel(JPanel panel) {
        JLabel lblUser = new JLabel("Select User:");
        lblUser.setBounds(20, 20, 100, 25);
//        lblUser.setBounds(20, 20, 100, 25);
        panel.add(lblUser);
        cbUserss = new JComboBox<>();
        cbUserss.setBounds(130, 20, 180, 25);
//        cbUser.setBounds(130, 20, 180, 25);
        panel.add(cbUserss);

        Set<String> allUserIds = staffRegistry.snapshot().keySet();
        cbUserss.removeAllItems();
        for (String userId : allUserIds) {
            cbUserss.addItem(userId);
        }

//        JLabel lblRole = new JLabel("Assign Role:");
//        lblRole.setBounds(20, 20, 100, 25);
////        lblRole.setBounds(350, 20, 100, 25);
//        panel.add(lblRole);
//        cbRolell = new JComboBox<>(new String[]{"Doctor", "Nurse", "Pharmacist", "Admin"});
//        cbRolell.setBounds(130, 20, 180, 25);
////        cbRole.setBounds(450, 20, 180, 25);
//        panel.add(cbRolell);
//        JButton btnAssign = new JButton("Assign Role");
//        btnAssign.setBounds(650, 20, 120, 25);
//        panel.add(btnAssign);
//        usersByRolell = new HashMap<>();
//        usersByRolell.put("Doctor", getUsersByRole("Doctor"));
//        usersByRolell.put("Nurse", getUsersByRole("Nurse"));
//        usersByRolell.put("Pharmacist", getUsersByRole("Pharmacist"));
//        usersByRolell.put("Admin", getUsersByRole("Admin"));
// Populate user combo box based on initial role selected
//        String selectedRole = (String) cbRolell.getSelectedItem();
//        updateUserComboBoxByRole(selectedRole);
//        String selectedRole = "Doctor";
//        cbUserss.setModel(new DefaultComboBoxModel<>(usersByRolell.getOrDefault(selectedRole, new String[]{})));
// Add listener to role combo box to update users combo box whenever role changes
//        cbRolell.addActionListener(e -> {
//            String role = (String) cbRolell.getSelectedItem();
////            if (role != null) {
////                String[] users = usersByRole.get(role);
////                if (users != null) {
////                    cbUser.setModel(new DefaultComboBoxModel<>(users));
////                } else {
////                    cbUser.setModel(new DefaultComboBoxModel<>(new String[]{}));
////                }
////            }
////            cbUser.setModel(new DefaultComboBoxModel<>(usersByRole.getOrDefault(role, new String[]{})));
//            updateUserComboBoxByRole(role);
//        });
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
            String user = (String) cbUserss.getSelectedItem();
//            String role = (String) cbRolell.getSelectedItem();

            String userIds = authService.getCurrentUserId();  // Current logged-in user ID
            String roles = authService.getCurrentRole();
            boolean allowed = isAllowedByChain(userIds, roles, Permission.STAFF_SCHEDULE);

            if (!allowed) {
                JOptionPane.showMessageDialog(panel, "Access denied: You don't have permission to schedule staff.");
                return;
            }
            Date selectedDate = (Date) dateSpinner.getValue();
            Date startTime = (Date) startSpinner.getValue();
            Date endTime = (Date) endSpinner.getValue();

            // Convert to LocalDate + LocalTime
            LocalDate date = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime start = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime end = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            StaffSchedule schedule = mediator.getOrCreateSchedule(user);
            schedule.addAvailableSlot(date, start, end);

            lblResult.setText("Availability saved for " + user + " on " + date + " from " + start + " to " + end);
        });
//            panel.setVisible(true);
        ///////////////////////////////////////////////////////////

    }

    private void updateUserComboBoxByRole(String role) {
        String[] users = usersByRolell.getOrDefault(role, new String[]{});
        cbUserss.setModel(new DefaultComboBoxModel<>(users));
    }

    private Permission mapActionToPermission(String actionName) {
        switch (actionName) {
            case "View Records":
                return Permission.VIEW_RECORDS;
            case "Edit Records":
                return Permission.EDIT_RECORDS;
            case "Reports":
                return Permission.GENERATE_REPORTS;
            case "Register Patient":
                return Permission.REGISTER_PATIENT;
            case "Treatement and Medical Notes":
                return Permission.TREATMENTS_AND_MEDICAL_NOTES;
            case "Make Appointment":
                return Permission.MAKE_APPOINTMENT;
            case "Roles and Permissions":
                return Permission.ROLES_AND_PERMISSIONS;
            case "Billing and prescriptions":
                return Permission.BILLING_AND_PRESCRIPTION;
            case "Staff Registration":
                return Permission.STAFF_REGISTER;
            case "Staff Schedule":
                return Permission.STAFF_SCHEDULE;
            case "Medicine Stock Management":
                return Permission.MEDICINE_STOCKE_MANAGEMENT;

            // example mapping
            // Add mappings as needed for your actions, or create more enum values if you want finer control.
            default:
                return null; // Unknown/unsupported action
        }
    }

    private boolean isAllowedByChain(String userId, String selectedRole, Permission permission) {
        // Build chain in escalation order. If your policy is single-role only, just pass List.of(selectedRole).
        java.util.List<String> roles = java.util.Arrays.asList(selectedRole, "Admin"); // Admin at end as fallback, optional per your policy.
        RoleHandler chain = AccessControlChainBuilder.buildChain(roles);

        if (permission == null || chain == null) {
            return false;
        }

        AccessRequest req = new AccessRequest(userId, selectedRole, permission);
        return chain.handle(req);
    }

    private void loadPermissionsToTable(DefaultTableModel model, JTable table) {
        String[] roles = {"Doctor", "Nurse", "Pharmacist", "Admin"};
        model.setRowCount(0); // clear existing data

        // Define all actions (rows) corresponding to your Permission enum / mapActionToPermission
        String[] actions = {"View Records", "Edit Records", "Reports", "Billing and prescriptions", "Register Patient", "Treatment and Medical Notes", "Make Appointment", "Roles and Permissions", "Staff Registration", "Staff Schedule", "Medicine Stock Management"};

        for (String action : actions) {
            Object[] row = new Object[roles.length + 1];
            row[0] = action;
            for (int i = 0; i < roles.length; i++) {
                EnumSet<Permission> perms = PermissionMatrix.getPermissionsForRole(roles[i]);
                Permission perm = mapActionToPermission(action);
                row[i + 1] = perm != null && perms.contains(perm);
            }
            model.addRow(row);
        }
    }

    private String[] getUsersByRole(String role) {
        // Example: filtering assignedRoles map that stores userId->role from Part D
        return assignedRoles.entrySet().stream()
                .filter(entry -> role.equalsIgnoreCase(entry.getValue()))
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }
    // Method to open a report preview window for given report text and title

    private void openReportPreview(String reportTitle, String reportText) {
        JFrame reportFrame = new JFrame(reportTitle);
        reportFrame.setSize(600, 500);
        reportFrame.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JTextArea reportArea = new JTextArea(reportText);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnPrint = new JButton("Print");
        panel.add(btnPrint, BorderLayout.SOUTH);

        btnPrint.addActionListener(e -> {
            try {
                boolean done = reportArea.print();
                if (done) {
                    JOptionPane.showMessageDialog(reportFrame, "Printing complete", "Print", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(reportFrame, "Printing cancelled", "Print", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(reportFrame, "Error printing: " + ex.getMessage(), "Print", JOptionPane.ERROR_MESSAGE);
            }
        });

        reportFrame.setContentPane(panel);
        reportFrame.setVisible(true);
    }

    private Patient loadPatientById(int patientId) {
        for (Patient p : patients) {
            if (p.getId() == patientId) {
                return p;
            }
        }
        return null; // not found
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
