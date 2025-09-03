import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

public class BloodDonationSystem extends JFrame {
    private DataManager dataManager;
    private DefaultTableModel donorModel, bankModel, donationModel;
    private JTable donorTable, bankTable, donationTable;
    private JComboBox<String> bloodGroupFilterBox;
    private JComboBox<Donor> donorBox;
    private JComboBox<BloodBank> bankBox;
    private JTextField dateField;
    private JLabel donorWarningLabel;
    private JTabbedPane tabs;

    public BloodDonationSystem() {
        setTitle("Blood Donation Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        dataManager = FileHandler.loadData();

        // TabbedPane with selected tab text red (same behavior)
        tabs = new JTabbedPane() {
            @Override
            public void setForegroundAt(int index, Color color) {
                super.setForegroundAt(index, color);
            }
        };
        tabs.setFont(new Font("Arial", Font.BOLD, 16));
        tabs.setBackground(new Color(0x1E, 0x1E, 0x1E));
        tabs.setForeground(Color.WHITE);
        tabs.setOpaque(true);

        tabs.addChangeListener(e -> {
            for (int i = 0; i < tabs.getTabCount(); i++) {
                tabs.setForegroundAt(i, Color.WHITE);
            }
            tabs.setForegroundAt(tabs.getSelectedIndex(), Color.RED);
        });
        tabs.add("Donors", createDonorPanel());
        tabs.add("Blood Banks", createBloodBankPanel());
        tabs.add("Donations", createDonationPanel());
        JPanel homePanel = createHomePanel();
        getContentPane().setLayout(new BorderLayout());

        add(homePanel, BorderLayout.CENTER);

        getContentPane().setBackground(new Color(0x21, 0x21, 0x21));
        setVisible(true);
    }

    // ================= HOME PANEL =================
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x21, 0x21, 0x21));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel title = new JLabel("Blood Donation Management System");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0x21, 0x21, 0x21));
        buttonPanel.setLayout(new GridLayout(1, 3, 20, 0));
        buttonPanel.add(createHomeButton("Donors", 0));
        buttonPanel.add(createHomeButton("Blood Banks", 1));
        buttonPanel.add(createHomeButton("Donations", 2));
        gbc.gridy = 1;
        panel.add(buttonPanel, gbc);
        return panel;
    }

    private JButton createHomeButton(String text, int tabIndex) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 60));

        button.addActionListener(e -> {
            getContentPane().removeAll();
            add(tabs, BorderLayout.CENTER);
            tabs.setSelectedIndex(tabIndex);
            revalidate();
            repaint();
        });
        return button;
    }

    // =================== DONOR PANEL ===================
    private JPanel createDonorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0x21, 0x21, 0x21)); // #212121

        donorModel = new DefaultTableModel(new String[] { "ID", "Name", "Blood Group", "Age", "Gender" }, 0);

        donorTable = new JTable(donorModel);
        loadDonors();
        customizeTable(donorTable);
        JScrollPane donorScroll = new JScrollPane(donorTable);
        donorScroll.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        donorScroll.setPreferredSize(new Dimension(950, 430));

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));

        form.setBackground(new Color(0x21, 0x21, 0x21));
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextField nameField = new JTextField();
        addPlaceholder(nameField, "Enter Donor Name");

        JComboBox<String> groupBox = new JComboBox<>(new String[] {
                "Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        });
        JTextField ageField = new JTextField();
        addPlaceholder(ageField, "Enter Age");

        JRadioButton maleBtn = new JRadioButton("Male");
        JRadioButton femaleBtn = new JRadioButton("Female");
        JRadioButton customBtn = new JRadioButton("Custom");

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);
        genderGroup.add(customBtn);
        for (JRadioButton rb : new JRadioButton[] { maleBtn, femaleBtn, customBtn }) {
            rb.setBackground(new Color(0x21, 0x21, 0x21));
            rb.setForeground(Color.WHITE);
            rb.setFocusPainted(false);
        }
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(new Color(0x21, 0x21, 0x21));
        genderPanel.add(maleBtn);
        genderPanel.add(femaleBtn);
        genderPanel.add(customBtn);

        form.add(styledLabel("ID:"));
        form.add(styledLabel("(Auto)"));
        form.add(styledLabel("Name:"));
        form.add(nameField);
        form.add(styledLabel("Age:"));
        form.add(ageField);
        form.add(styledLabel("Gender:"));
        form.add(genderPanel);
        form.add(styledLabel("Blood Group:"));
        form.add(groupBox);

        JButton addBtn = createStyledButton("Add");
        JButton deleteBtn = createStyledButton("Delete");
        JButton updateBtn = createStyledButton("Update");
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(0x21, 0x21, 0x21));
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String group = String.valueOf(groupBox.getSelectedItem());
            String ageText = ageField.getText().trim();
            String gender = maleBtn.isSelected() ? "Male"
                    : femaleBtn.isSelected() ? "Female" : customBtn.isSelected() ? "Custom" : "";
            if (name.isEmpty() || name.equals("Enter Donor Name") || group.equals("Select Blood Group") ||
                    ageText.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter donor details properly!");
                return;
            }
            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age!");
                return;
            }
            boolean exists = false;
            for (Donor d : dataManager.getDonors()) {
                if (d.getName().equalsIgnoreCase(name) && d.getBloodGroup().equalsIgnoreCase(group)
                        && d.getAge() == age && d.getGender().equalsIgnoreCase(gender)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                JOptionPane.showMessageDialog(this, "Donor already exists!");
            } else {
                int id = dataManager.getNextDonorId();
                Donor d = new Donor(id, name, group, age, gender);
                dataManager.addDonor(d);
                FileHandler.saveData(dataManager);
                loadDonors();
                refreshDonationComboBoxes();
                nameField.setText("");
                groupBox.setSelectedIndex(0);
                ageField.setText("");
                genderGroup.clearSelection();
            }
        });
        updateBtn.addActionListener(e -> {
            int row = donorTable.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(donorModel.getValueAt(row, 0).toString());
                String newName = nameField.getText().trim();
                String newGroup = String.valueOf(groupBox.getSelectedItem());
                String ageText = ageField.getText().trim();
                String gender = maleBtn.isSelected() ? "Male"
                        : femaleBtn.isSelected() ? "Female" : customBtn.isSelected() ? "Custom" : "";

                if (newName.isEmpty() || newName.equals("Enter Donor Name") || newGroup.equals("Select Blood Group")
                        || ageText.isEmpty() || gender.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter donor details properly!");
                    return;
                }
                int age;
                try {
                    age = Integer.parseInt(ageText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid age!");
                    return;
                }
                dataManager.updateDonor(id, newName, newGroup, age, gender);
                FileHandler.saveData(dataManager);
                loadDonors();
                refreshDonationComboBoxes();
            }
        });
        deleteBtn.addActionListener(e -> {
            int row = donorTable.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(donorModel.getValueAt(row, 0).toString());
                dataManager.deleteDonor(id);
                FileHandler.saveData(dataManager);
                loadDonors();
                refreshDonationComboBoxes();
            }
        });

        panel.add(donorScroll, BorderLayout.CENTER);
        panel.add(form, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // =================== BLOOD BANK PANEL ===================
    private JPanel createBloodBankPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0x1E, 0x1E, 0x1E));

        bankModel = new DefaultTableModel(new String[] { "ID", "Name", "Location" }, 0);
        bankTable = new JTable(bankModel);
        loadBloodBanks();
        customizeTable(bankTable);

        JScrollPane bankScroll = new JScrollPane(bankTable);
        bankScroll.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        bankScroll.setPreferredSize(new Dimension(950, 430));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBackground(new Color(0x1E, 0x1E, 0x1E));
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextField nameField = new JTextField();
        addPlaceholder(nameField, "Enter Blood Bank Name");

        JTextField locationField = new JTextField();
        addPlaceholder(locationField, "Enter Location");

        form.add(styledLabel("ID:"));
        form.add(styledLabel("(Auto)"));
        form.add(styledLabel("Blood Bank Name:"));
        form.add(nameField);
        form.add(styledLabel("Location:"));
        form.add(locationField);

        JButton addBtn = createStyledButton("Add");
        JButton updateBtn = createStyledButton("Update");
        JButton deleteBtn = createStyledButton("Delete");

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(0x21, 0x21, 0x21));
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        addBtn.addActionListener(e -> {
            nameField.setForeground(Color.BLACK);
            locationField.setForeground(Color.BLACK);
            String name = nameField.getText().trim();
            String loc = locationField.getText().trim();

            if (name.isEmpty() || name.equals("Enter Blood Bank Name") || loc.isEmpty()
                    || loc.equals("Enter Location")) {
                JOptionPane.showMessageDialog(this, "Please enter blood bank details properly!");
                return;
            }
            boolean exists = false;
            for (BloodBank b : dataManager.getBloodBanks()) {
                if (b.getName().equalsIgnoreCase(name) && b.getLocation().equalsIgnoreCase(loc)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                JOptionPane.showMessageDialog(this, "Blood Bank already exists!");
            } else {
                int id = dataManager.getNextBloodBankId();
                BloodBank b = new BloodBank(id, name, loc);
                dataManager.addBloodBank(b);
                FileHandler.saveData(dataManager);
                loadBloodBanks();
                refreshDonationComboBoxes();
                nameField.setText("");
                locationField.setText("");
            }
        });
        updateBtn.addActionListener(e -> {
            int row = bankTable.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(bankModel.getValueAt(row, 0).toString());
                String newName = nameField.getText().trim();
                String newLoc = locationField.getText().trim();
                if (newName.isEmpty() || newName.equals("Enter Blood Bank Name") || newLoc.isEmpty()
                        || newLoc.equals("Enter Location")) {
                    JOptionPane.showMessageDialog(this, "Please enter blood bank details properly!");
                    return;
                }
                dataManager.updateBloodBank(id, newName, newLoc);
                FileHandler.saveData(dataManager);
                loadBloodBanks();
                refreshDonationComboBoxes();
            }
        });
        deleteBtn.addActionListener(e -> {
            int row = bankTable.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(bankModel.getValueAt(row, 0).toString());
                dataManager.deleteBloodBank(id);
                FileHandler.saveData(dataManager);
                loadBloodBanks();
                refreshDonationComboBoxes();
            }
        });

        panel.add(bankScroll, BorderLayout.CENTER);
        panel.add(form, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void addPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    // =================== DONATION PANEL ===================
    private JPanel createDonationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0x21, 0x21, 0x21));

        donationModel = new DefaultTableModel(new String[] { "ID", "Donor", "Blood Bank", "Date" }, 0);
        donationTable = new JTable(donationModel);
        loadDonations();
        customizeTable(donationTable);

        JScrollPane donationScroll = new JScrollPane(donationTable);
        donationScroll.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        donationScroll.setPreferredSize(new Dimension(950, 430));

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBackground(new Color(0x1E, 0x1E, 0x1E));
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        bloodGroupFilterBox = new JComboBox<>(
                new String[] { "Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" });
        donorBox = new JComboBox<>();
        donorBox.addItem(new Donor(-1, "Select Donor", "", 0, "")); // Placeholder [same as old]
        bankBox = new JComboBox<>();
        dateField = new JTextField();
        donorWarningLabel = new JLabel("");
        donorWarningLabel.setForeground(Color.RED);

        addPlaceholder(dateField, "Enter Date (dd/mm/yyyy)");

        dateField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = dateField.getText();
                if ((text.length() == 2 || text.length() == 5) && !text.endsWith("/")) {
                    dateField.setText(text + "/");
                }
            }
        });

        // Filter donors by blood group
        bloodGroupFilterBox.addActionListener(e -> {
            String selectedGroup = (String) bloodGroupFilterBox.getSelectedItem();
            donorBox.removeAllItems();
            donorBox.addItem(new Donor(-1, "Select Donor", "", 0, ""));
            donorWarningLabel.setText("");

            if (selectedGroup != null && !selectedGroup.equals("Select Blood Group")) {
                int count = 0;
                for (Donor d : dataManager.getDonors()) {
                    if (d.getBloodGroup().equalsIgnoreCase(selectedGroup)) {
                        donorBox.addItem(d);
                        count++;
                    }
                }
                donorWarningLabel.setText("Total Donors: " + count);
            }
        });

        refreshBankComboBox();

        form.add(styledLabel("Select Blood Group:"));
        form.add(bloodGroupFilterBox);
        form.add(styledLabel("Donor:"));
        form.add(donorBox);
        form.add(donorWarningLabel);
        form.add(new JLabel(""));
        form.add(styledLabel("Blood Bank:"));
        form.add(bankBox);
        form.add(styledLabel("Date (dd/mm/yyyy):"));
        form.add(dateField);

        JButton addBtn = createStyledButton("Add");
        JButton updateBtn = createStyledButton("Update");
        JButton deleteBtn = createStyledButton("Delete");

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(0x21, 0x21, 0x21));
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        addBtn.addActionListener(e -> addDonationAction());
        deleteBtn.addActionListener(e -> deleteDonationAction());
        updateBtn.addActionListener(e -> updateDonationAction());

        panel.add(donationScroll, BorderLayout.CENTER);
        panel.add(form, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // =================== Customization Helpers ===================
    private void customizeTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setBackground(new Color(0x21, 0x21, 0x21));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(0x46, 0x46, 0x46)); // #464646
        table.setSelectionBackground(new Color(0x8B, 0x00, 0x00)); // #8B0000
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBackground(new Color(0x32, 0x32, 0x32)); // #323232
        header.setForeground(Color.RED);
        header.setOpaque(true);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(200, 0, 0)); // bright red on selection
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? new Color(0x2C, 0x2C, 0x2C) : new Color(0x3A, 0x3A, 0x3A));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(0xB4, 0x00, 0x00)); // #B40000
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(130, 42));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x78, 0x00, 0x00), 2),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(new Color(0xC8, 0x00, 0x00));
            } // hover

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(new Color(0xB4, 0x00, 0x00));
            } // normal

            public void mousePressed(MouseEvent evt) {
                btn.setBackground(new Color(0x78, 0x00, 0x00));
            } // click

            public void mouseReleased(MouseEvent evt) {
                btn.setBackground(new Color(0xC8, 0x00, 0x00));
            } // release
        });
        return btn;
    }

    private JLabel styledLabel(String text) {
        JLabel j = new JLabel(text);
        j.setFont(new Font("Arial", Font.BOLD, 14));
        j.setForeground(Color.WHITE);
        return j;
    }

    // =================== Logic Methods ===================
    private void addDonationAction() {
        Donor donor = (Donor) donorBox.getSelectedItem();
        BloodBank bank = (BloodBank) bankBox.getSelectedItem();
        String date = dateField.getText().trim();

        if (donor == null || donor.getId() == -1) {
            donorWarningLabel.setText("Please select a donor!");
            return;
        }
        if (bank == null) {
            JOptionPane.showMessageDialog(this, "Please select a blood bank!");
            return;
        }
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Invalid date! Use dd/mm/yyyy with 4-digit year.");
            return;
        }

        Donation d = new Donation(dataManager.getNextDonationId(), donor, bank, date);
        dataManager.addDonation(d);
        FileHandler.saveData(dataManager);
        loadDonations();

        clearDonationFields();
    }

    private void deleteDonationAction() {
        int row = donationTable.getSelectedRow();
        if (row >= 0) {
            int id = Integer.parseInt(donationModel.getValueAt(row, 0).toString());
            dataManager.deleteDonationById(id);
            FileHandler.saveData(dataManager);
            loadDonations();
            clearDonationFields();
        }
    }

    private void updateDonationAction() {
        int row = donationTable.getSelectedRow();
        if (row >= 0) {
            Donation selectedDonation = dataManager.getDonations().get(row);
            Donor donor = (Donor) donorBox.getSelectedItem();
            BloodBank bank = (BloodBank) bankBox.getSelectedItem();
            String date = dateField.getText().trim();
            if (donor == null || donor.getId() == -1) {
                donorWarningLabel.setText("Please select a donor!");
                return;
            }
            if (bank == null) {
                JOptionPane.showMessageDialog(this, "Please select a blood bank!");
                return;
            }
            if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
                JOptionPane.showMessageDialog(this, "Invalid date! Use dd/mm/yyyy with 4-digit year.");
                return;
            }

            selectedDonation = new Donation(selectedDonation.getId(), donor, bank, date);
            dataManager.getDonations().set(row, selectedDonation);
            FileHandler.saveData(dataManager);
            loadDonations();

            clearDonationFields();
        }
    }

    private void clearDonationFields() {
        bloodGroupFilterBox.setSelectedIndex(0);
        donorBox.removeAllItems();
        donorBox.addItem(new Donor(-1, "Select Donor", "", 0, ""));
        refreshBankComboBox();
        dateField.setText("");
        donorWarningLabel.setText("");
    }

    private void refreshBankComboBox() {
        DefaultComboBoxModel<BloodBank> bankModelDisplay = new DefaultComboBoxModel<>();
        for (BloodBank b : dataManager.getBloodBanks()) {
            // show "id | name | location" in dropdown without changing stored data
            bankModelDisplay.addElement(new BloodBank( // [added for multi-file setup] display wrapper object
                    b.getId(),
                    b.getId() + " | " + b.getName() + " | " + b.getLocation(),
                    b.getLocation()));
        }
        bankBox.setModel(bankModelDisplay);
    }

    private void refreshDonationComboBoxes() {
        String selectedGroup = (String) bloodGroupFilterBox.getSelectedItem();
        donorBox.removeAllItems();
        donorBox.addItem(new Donor(-1, "Select Donor", "", 0, ""));
        if (selectedGroup != null && !selectedGroup.equals("Select Blood Group")) {
            for (Donor d : dataManager.getDonors()) {
                if (d.getBloodGroup().equalsIgnoreCase(selectedGroup)) {
                    donorBox.addItem(d);
                }
            }
        }
        refreshBankComboBox();
    }

    private void loadDonors() {
        donorModel.setRowCount(0);
        for (Donor d : dataManager.getDonors()) {
            donorModel.addRow(new Object[] { d.getId(), d.getName(), d.getBloodGroup(), d.getAge(), d.getGender() });
        }
    }

    private void loadBloodBanks() {
        bankModel.setRowCount(0);
        for (BloodBank b : dataManager.getBloodBanks()) {
            bankModel.addRow(new Object[] { b.getId(), b.getName(), b.getLocation() });
        }
    }

    private void loadDonations() {
        donationModel.setRowCount(0);
        for (Donation d : dataManager.getDonations()) {
            donationModel.addRow(new Object[] {
                    d.getId(),
                    d.getDonor().getName(),
                    d.getBloodBank().getName(),
                    d.getDate()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BloodDonationSystem::new);
    }

}
