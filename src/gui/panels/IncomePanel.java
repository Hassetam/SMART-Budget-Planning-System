package gui.panels;

import model.Income;
import model.User;
import service.FinanceService;
import util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class IncomePanel extends JPanel {

    // Logged in user
    private User currentUser;

    // Service
    private FinanceService financeService;

    // Labels
    private JLabel titleLabel;
    private JLabel amountLabel;
    private JLabel expectedLabel;
    private JLabel dateLabel;
    private JLabel descriptionLabel;

    // Inputs
    private JTextField amountField;
    private JCheckBox expectedCheckBox;
    private JTextField dateField;
    private JTextField descriptionField;

    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    // Table
    private JTable incomeTable;
    private DefaultTableModel tableModel;

    // Panels
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JScrollPane tableScrollPane;

    public IncomePanel(User currentUser) {

        this.currentUser = currentUser;

        financeService = new FinanceService();

        initializeComponents();
        layoutComponents();
        loadIncome();
        registerListeners();

    }

    private void initializeComponents() {

        setLayout(new BorderLayout(15, 15));
        setBackground(UIConstants.BACKGROUND_COLOR);

        titleLabel = new JLabel("Income Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);

        amountLabel = new JLabel("Amount");
        expectedLabel = new JLabel("Expected");
        dateLabel = new JLabel("Date");
        descriptionLabel = new JLabel("Description");

        amountField = new JTextField(15);

        expectedCheckBox = new JCheckBox("Expected Income");
        expectedCheckBox.setBackground(UIConstants.BACKGROUND_COLOR);

        dateField = new JTextField(LocalDate.now().toString());

        descriptionField = new JTextField(20);

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        JButton[] buttons = {
                addButton,
                updateButton,
                deleteButton,
                clearButton
        };

        for (JButton button : buttons) {

            button.setFont(UIConstants.BUTTON_FONT);
            button.setBackground(UIConstants.BUTTON_COLOR);
            button.setForeground(Color.WHITE);

        }

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new String[] {
                "ID",
                "Amount",
                "Expected",
                "Date",
                "Description"
        });

        incomeTable = new JTable(tableModel);

        incomeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableScrollPane = new JScrollPane(incomeTable);

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

    }

    private void layoutComponents() {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(expectedLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(expectedCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private void loadIncome() {

        tableModel.setRowCount(0);

        for (Income income : financeService.getIncome(currentUser.getUserId())) {

            tableModel.addRow(new Object[] {

                    income.getIncomeId(),
                    income.getAmount(),
                    income.isExpected(),
                    income.getDateReceived(),
                    income.getDescription()

            });

        }

    }

    private void registerListeners() {

        addButton.addActionListener(e -> addIncome());

        updateButton.addActionListener(e -> updateIncome());

        deleteButton.addActionListener(e -> deleteIncome());

        clearButton.addActionListener(e -> clearFields());

        incomeTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                fillFieldsFromTable();

            }

        });

    }

    private void addIncome() {

        try {

            Income income = new Income(

                    currentUser.getUserId(),

                    Double.parseDouble(amountField.getText()),

                    expectedCheckBox.isSelected(),

                    LocalDate.parse(dateField.getText()),

                    descriptionField.getText()

            );

            if (financeService.addIncome(income)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Income added successfully.");

                loadIncome();

                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add income.");

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid input.");

        }

    }

    private void updateIncome() {

        int row = incomeTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an income record.");

            return;
        }

        try {

            Income income = new Income(

                    (Integer) tableModel.getValueAt(row, 0),

                    currentUser.getUserId(),

                    Double.parseDouble(amountField.getText()),

                    expectedCheckBox.isSelected(),

                    LocalDate.parse(dateField.getText()),

                    descriptionField.getText()

            );

            if (financeService.updateIncome(income)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Income updated successfully.");

                loadIncome();

                clearFields();

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid input.");

        }

    }

    private void deleteIncome() {

        int row = incomeTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an income record.");

            return;
        }

        int incomeId = (Integer) tableModel.getValueAt(row, 0);

        int option = JOptionPane.showConfirmDialog(

                this,

                "Delete selected income?",

                "Confirm Delete",

                JOptionPane.YES_NO_OPTION

        );

        if (option == JOptionPane.YES_OPTION) {

            financeService.deleteIncome(incomeId);

            loadIncome();

            clearFields();

        }

    }

    private void fillFieldsFromTable() {

        int row = incomeTable.getSelectedRow();

        if (row == -1) {

            return;

        }

        amountField.setText(
                tableModel.getValueAt(row, 1).toString());

        expectedCheckBox.setSelected(
                (Boolean) tableModel.getValueAt(row, 2));

        dateField.setText(
                tableModel.getValueAt(row, 3).toString());

        Object description = tableModel.getValueAt(row, 4);

        descriptionField.setText(
                description == null ? "" : description.toString());

    }

    private void clearFields() {

        amountField.setText("");

        expectedCheckBox.setSelected(false);

        dateField.setText(LocalDate.now().toString());

        descriptionField.setText("");

        incomeTable.clearSelection();

    }

}