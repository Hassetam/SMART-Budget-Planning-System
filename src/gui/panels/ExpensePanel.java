package gui.panels;

import model.Expense;
import model.User;
import service.FinanceService;
import util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class ExpensePanel extends JPanel {

    // Logged in user
    private User currentUser;

    // Service
    private FinanceService financeService;

    // Labels
    private JLabel titleLabel;
    private JLabel amountLabel;
    private JLabel categoryLabel;
    private JLabel dateLabel;
    private JLabel descriptionLabel;

    // Inputs
    private JTextField amountField;
    private JComboBox<String> categoryComboBox;
    private JTextField dateField;
    private JTextField descriptionField;

    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    // Table
    private JTable expenseTable;
    private DefaultTableModel tableModel;

    // Panels
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JScrollPane tableScrollPane;

    public ExpensePanel(User currentUser) {

        this.currentUser = currentUser;

        financeService = new FinanceService();

        initializeComponents();
        layoutComponents();
        loadExpenses();
        registerListeners();

    }

    private void initializeComponents() {

        setLayout(new BorderLayout(15, 15));
        setBackground(UIConstants.BACKGROUND_COLOR);

        titleLabel = new JLabel("Expense Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);

        amountLabel = new JLabel("Amount");
        categoryLabel = new JLabel("Category");
        dateLabel = new JLabel("Date");
        descriptionLabel = new JLabel("Description");

        amountField = new JTextField(15);

        categoryComboBox = new JComboBox<>(new String[] {
                "Food",
                "Transport",
                "Bills",
                "Shopping",
                "Health",
                "Education",
                "Entertainment",
                "Other"
        });

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
                "Category",
                "Date",
                "Description"
        });

        expenseTable = new JTable(tableModel);

        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableScrollPane = new JScrollPane(expenseTable);

        formPanel = new JPanel(new GridBagLayout());

        buttonPanel = new JPanel();

    }

    private void layoutComponents() {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(amountField, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(categoryComboBox, gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(dateField, gbc);

        // Description
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

    private void registerListeners() {

        addButton.addActionListener(e -> addExpense());

        updateButton.addActionListener(e -> updateExpense());

        deleteButton.addActionListener(e -> deleteExpense());

        clearButton.addActionListener(e -> clearFields());

        expenseTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                fillFieldsFromTable();

            }

        });

    }

    private void loadExpenses() {

        tableModel.setRowCount(0);

        for (Expense expense : financeService.getExpenses(currentUser.getUserId())) {

            tableModel.addRow(new Object[] {

                    expense.getExpenseId(),
                    expense.getAmount(),
                    expense.getCategory(),
                    expense.getDateSpent(),
                    expense.getDescription()

            });

        }

    }

    private void addExpense() {

        try {

            Expense expense = new Expense(

                    currentUser.getUserId(),

                    Double.parseDouble(amountField.getText()),

                    categoryComboBox.getSelectedItem().toString(),

                    LocalDate.parse(dateField.getText()),

                    descriptionField.getText()

            );

            if (financeService.addExpense(expense)) {

                JOptionPane.showMessageDialog(this, "Expense added.");

                loadExpenses();

                clearFields();

            }

            else {

                JOptionPane.showMessageDialog(this, "Failed to add expense.");

            }

        }

        catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid input.");

        }

    }

    private void updateExpense() {

        int row = expenseTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(this, "Select an expense first.");

            return;

        }

        try {

            Expense expense = new Expense(

                    (Integer) tableModel.getValueAt(row, 0),

                    currentUser.getUserId(),

                    Double.parseDouble(amountField.getText()),

                    categoryComboBox.getSelectedItem().toString(),

                    LocalDate.parse(dateField.getText()),

                    descriptionField.getText()

            );

            if (financeService.updateExpense(expense)) {

                JOptionPane.showMessageDialog(this, "Expense updated.");

                loadExpenses();

                clearFields();

            }

        }

        catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid input.");

        }

    }

    private void deleteExpense() {

        int row = expenseTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(this, "Select an expense.");

            return;

        }

        int expenseId = (Integer) tableModel.getValueAt(row, 0);

        int option = JOptionPane.showConfirmDialog(

                this,

                "Delete selected expense?",

                "Confirm",

                JOptionPane.YES_NO_OPTION

        );

        if (option == JOptionPane.YES_OPTION) {

            financeService.deleteExpense(expenseId);

            loadExpenses();

            clearFields();

        }

    }

    private void fillFieldsFromTable() {

        int row = expenseTable.getSelectedRow();

        if (row == -1) {

            return;

        }

        amountField.setText(tableModel.getValueAt(row, 1).toString());

        categoryComboBox.setSelectedItem(tableModel.getValueAt(row, 2));

        dateField.setText(tableModel.getValueAt(row, 3).toString());

        Object description = tableModel.getValueAt(row, 4);

        descriptionField.setText(description == null ? "" : description.toString());

    }

    private void clearFields() {

        amountField.setText("");

        categoryComboBox.setSelectedIndex(0);

        dateField.setText(LocalDate.now().toString());

        descriptionField.setText("");

        expenseTable.clearSelection();

    }

}