package gui.panels;

import model.User;
import model.Goal;
import model.GeneralGoal;
import model.OccasionGoal;

import service.GoalService;

import util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class GoalPanel extends JPanel {

        // ==========================================================
        // USER AND SERVICE
        // ==========================================================
        private User currentUser;

        private GoalService goalService;

        // ==========================================================
        // INPUT COMPONENTS
        // ==========================================================
        private JTextField goalNameField;
        private JTextField occasionTypeField;
        private JTextField targetAmountField;
        private JTextField savedAmountField;
        private JTextField deadlineField;

        private JRadioButton generalRadio;
        private JRadioButton occasionRadio;

        // ==========================================================
        // BUTTONS
        // ==========================================================
        private JButton addGoalButton;
        private JButton refreshButton;
        private JButton updateButton;
        private JButton addSavingsButton;
        private JButton completeButton;
        private JButton deleteButton;

        // ==========================================================
        // TABLE
        // ==========================================================
        private JTable goalTable;

        private DefaultTableModel tableModel;

        // Stores selected goal ID
        private int selectedGoalId = -1;

        // ==========================================================
        // CONSTRUCTOR
        // ==========================================================
        public GoalPanel(User currentUser) {

                this.currentUser = currentUser;

                goalService = new GoalService();

                initializeComponents();

                layoutComponents();

                registerListeners();

                loadGoals();

        }

        // ==========================================================
        // INITIALIZE COMPONENTS
        // ==========================================================
        private void initializeComponents() {

                setLayout(new BorderLayout(20, 20));

                setBackground(
                                UIConstants.BACKGROUND_COLOR);

                // Text fields
                goalNameField = new JTextField();

                occasionTypeField = new JTextField();

                targetAmountField = new JTextField();

                savedAmountField = new JTextField();

                deadlineField = new JTextField();

                // Radio buttons
                generalRadio = new JRadioButton("General Goal");

                occasionRadio = new JRadioButton("Occasion Goal");

                ButtonGroup group = new ButtonGroup();

                group.add(generalRadio);

                group.add(occasionRadio);

                generalRadio.setSelected(true);

                occasionTypeField.setEnabled(false);

                // Buttons
                addGoalButton = new JButton("Add Goal");

                refreshButton = new JButton("Refresh");

                updateButton = new JButton("Update");

                addSavingsButton = new JButton("Add Savings");

                completeButton = new JButton("Complete");

                deleteButton = new JButton("Delete");

                addGoalButton.setFont(UIConstants.BUTTON_FONT);

                refreshButton.setFont(UIConstants.BUTTON_FONT);

                updateButton.setFont(UIConstants.BUTTON_FONT);

                addSavingsButton.setFont(UIConstants.BUTTON_FONT);

                completeButton.setFont(UIConstants.BUTTON_FONT);

                deleteButton.setFont(UIConstants.BUTTON_FONT);

                deleteButton.setBackground(
                                UIConstants.DELETE_BUTTON_COLOR);

                deleteButton.setForeground(Color.WHITE);

                // Table
                String[] columns = {
                                "ID",
                                "Goal Name",
                                "Type",
                                "Occasion",
                                "Target",
                                "Saved",
                                "Deadline",
                                "Completed"

                };

                tableModel = new DefaultTableModel(columns, 0) {

                        @Override
                        public boolean isCellEditable(
                                        int row,
                                        int column) {

                                return false;
                        }

                };

                goalTable = new JTable(tableModel);

                goalTable.setSelectionMode(
                                ListSelectionModel.SINGLE_SELECTION);

                goalTable.setFont(
                                UIConstants.NORMAL_FONT);

                goalTable.getTableHeader()
                                .setFont(
                                                UIConstants.BUTTON_FONT);

        }

        // ==========================================================
        // LAYOUT
        // ==========================================================
        private void layoutComponents() {

                JPanel titlePanel = new JPanel();

                titlePanel.setBackground(
                                UIConstants.BACKGROUND_COLOR);

                JLabel title = new JLabel("GOALS");

                title.setFont(
                                UIConstants.TITLE_FONT);

                titlePanel.add(title);

                add(
                                titlePanel,
                                BorderLayout.NORTH);

                JPanel formPanel = new JPanel(
                                new GridLayout(8, 2, 10, 10));

                formPanel.setBackground(
                                UIConstants.PANEL_COLOR);

                formPanel.setBorder(
                                BorderFactory.createTitledBorder(
                                                "Create Goal"));

                formPanel.add(
                                new JLabel("Goal Name"));

                formPanel.add(
                                goalNameField);

                formPanel.add(
                                new JLabel("Goal Type"));

                JPanel typePanel = new JPanel(
                                new FlowLayout(
                                                FlowLayout.LEFT));

                typePanel.add(generalRadio);

                typePanel.add(occasionRadio);

                formPanel.add(typePanel);

                formPanel.add(
                                new JLabel("Occasion Type"));

                formPanel.add(
                                occasionTypeField);

                formPanel.add(
                                new JLabel("Target Amount"));

                formPanel.add(
                                targetAmountField);

                formPanel.add(
                                new JLabel("Saved Amount"));

                formPanel.add(
                                savedAmountField);

                formPanel.add(
                                new JLabel("Deadline YYYY-MM-DD"));

                formPanel.add(
                                deadlineField);

                formPanel.add(
                                new JLabel());

                formPanel.add(
                                addGoalButton);

                JPanel centerPanel = new JPanel(
                                new BorderLayout(10, 10));

                centerPanel.setBackground(
                                UIConstants.BACKGROUND_COLOR);

                centerPanel.add(
                                formPanel,
                                BorderLayout.NORTH);

                JPanel tablePanel = new JPanel(
                                new BorderLayout());

                tablePanel.setBorder(
                                BorderFactory.createTitledBorder(
                                                "Your Goals"));

                tablePanel.add(
                                new JScrollPane(goalTable));

                centerPanel.add(
                                tablePanel,
                                BorderLayout.CENTER);

                add(
                                centerPanel,
                                BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel();

                buttonPanel.setBackground(
                                UIConstants.BACKGROUND_COLOR);

                buttonPanel.add(refreshButton);

                buttonPanel.add(updateButton);

                buttonPanel.add(addSavingsButton);

                buttonPanel.add(completeButton);

                buttonPanel.add(deleteButton);

                add(
                                buttonPanel,
                                BorderLayout.SOUTH);

        }

        // ==========================================================
        // LOAD TABLE DATA
        // ==========================================================
        private void loadGoals() {

                tableModel.setRowCount(0);

                List<Goal> goals = goalService.getGoalsByUser(
                                currentUser.getUserId());

                for (Goal goal : goals) {

                        String occasion = "";

                        if (goal instanceof OccasionGoal og) {

                                occasion = og.getOccasionType();

                        }

                        tableModel.addRow(
                                        new Object[] {
                                                        goal.getGoalId(),
                                                        goal.getGoalName(),
                                                        goal.getGoalType(),
                                                        occasion,
                                                        goal.getTargetAmount(),
                                                        goal.getSavedAmount(),
                                                        goal.getDeadline(),
                                                        goal.isCompleted()
                                        });

                }

        }

        // ==========================================================
        // LISTENERS
        // ==========================================================
        private void registerListeners() {

                generalRadio.addActionListener(e -> {

                        occasionTypeField.setText("");

                        occasionTypeField.setEnabled(false);

                });

                occasionRadio.addActionListener(e -> {

                        occasionTypeField.setEnabled(true);

                });

                addGoalButton.addActionListener(e -> addGoal());

                refreshButton.addActionListener(e -> loadGoals());

                updateButton.addActionListener(e -> updateGoal());

                addSavingsButton.addActionListener(e -> addSavings());

                completeButton.addActionListener(e -> completeGoal());

                deleteButton.addActionListener(e -> deleteGoal());

                goalTable.getSelectionModel()
                                .addListSelectionListener(e -> {

                                        int row = goalTable.getSelectedRow();

                                        if (row >= 0) {

                                                selectedGoalId = Integer.parseInt(
                                                                tableModel.getValueAt(
                                                                                row,
                                                                                0).toString());

                                                fillFieldsFromTable(row);

                                        }

                                });

        }

        // ==========================================================
        // ADD GOAL
        // ==========================================================
        private void addGoal() {

                try {

                        String name = goalNameField.getText().trim();

                        double target = Double.parseDouble(
                                        targetAmountField.getText());

                        double saved = Double.parseDouble(
                                        savedAmountField.getText());

                        LocalDate deadline = LocalDate.parse(
                                        deadlineField.getText());

                        Goal goal;

                        if (generalRadio.isSelected()) {

                                goal = new GeneralGoal(
                                                currentUser.getUserId(),
                                                name,
                                                "General",
                                                target,
                                                saved,
                                                deadline,
                                                false);

                        } else {

                                String occasion = occasionTypeField
                                                .getText()
                                                .trim();

                                if (occasion.isEmpty()) {

                                        JOptionPane.showMessageDialog(
                                                        this,
                                                        "Enter occasion type.");

                                        return;
                                }

                                goal = new OccasionGoal(
                                                currentUser.getUserId(),
                                                name,
                                                "Occasion",
                                                target,
                                                saved,
                                                deadline,
                                                false,
                                                occasion);

                        }

                        if (goalService.addGoal(goal)) {

                                JOptionPane.showMessageDialog(
                                                this,
                                                "Goal added successfully.");

                                clearFields();

                                loadGoals();

                        } else {

                                JOptionPane.showMessageDialog(
                                                this,
                                                "Failed to add goal.");

                        }

                } catch (NumberFormatException e) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Amount must be numeric.");

                } catch (DateTimeParseException e) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Date format must be YYYY-MM-DD.");

                }

        }

        // ==========================================================
        // UPDATE GOAL
        // ==========================================================
        private void updateGoal() {

                if (selectedGoalId == -1) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Select a goal first.");

                        return;

                }

                Goal goal = goalService.getGoalById(
                                selectedGoalId);

                if (goal == null) {

                        return;

                }

                try {

                        goal.setGoalName(
                                        goalNameField.getText());

                        goal.setTargetAmount(
                                        Double.parseDouble(
                                                        targetAmountField.getText()));

                        goal.setSavedAmount(
                                        Double.parseDouble(
                                                        savedAmountField.getText()));

                        goal.setDeadline(
                                        LocalDate.parse(
                                                        deadlineField.getText()));

                        if (goal instanceof OccasionGoal og) {

                                og.setOccasionType(
                                                occasionTypeField.getText());

                        }

                        if (goalService.updateGoal(goal)) {

                                JOptionPane.showMessageDialog(
                                                this,
                                                "Goal updated.");

                                loadGoals();

                        }

                } catch (Exception e) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Invalid data.");

                }

        }

        // ==========================================================
        // ADD SAVINGS
        // ==========================================================
        private void addSavings() {

                if (selectedGoalId == -1) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Select a goal.");

                        return;

                }

                String amount = JOptionPane.showInputDialog(
                                this,
                                "Enter new saved amount:");

                try {

                        double value = Double.parseDouble(amount);

                        if (goalService.updateSavedAmount(
                                        selectedGoalId,
                                        value)) {

                                JOptionPane.showMessageDialog(
                                                this,
                                                "Savings updated.");

                                loadGoals();

                        }

                } catch (Exception e) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Invalid amount.");

                }

        }

        // ==========================================================
        // COMPLETE GOAL
        // ==========================================================
        private void completeGoal() {

                if (selectedGoalId == -1) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Select a goal.");

                        return;

                }

                if (goalService.setGoalCompleted(
                                selectedGoalId,
                                true)) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Goal completed!");

                        loadGoals();

                }

        }

        // ==========================================================
        // DELETE GOAL
        // ==========================================================
        private void deleteGoal() {

                if (selectedGoalId == -1) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Select a goal.");

                        return;

                }

                int choice = JOptionPane.showConfirmDialog(
                                this,
                                "Delete this goal?");

                if (choice == JOptionPane.YES_OPTION) {

                        if (goalService.deleteGoal(
                                        selectedGoalId)) {

                                JOptionPane.showMessageDialog(
                                                this,
                                                "Goal deleted.");

                                selectedGoalId = -1;

                                clearFields();

                                loadGoals();

                        }

                }

        }

        // ==========================================================
        // RESTORE (READY FOR SETTINGS)
        // ==========================================================
        public void restoreGoal(int goalId) {

                if (goalService.restoreGoal(goalId)) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Goal restored.");

                        loadGoals();

                }

        }

        // ==========================================================
        // HELPERS
        // ==========================================================
        private void fillFieldsFromTable(int row) {

                goalNameField.setText(
                                tableModel.getValueAt(row, 1)
                                                .toString());

                String type = tableModel.getValueAt(row, 2)
                                .toString();

                if (type.equalsIgnoreCase("General")) {

                        generalRadio.setSelected(true);

                        occasionTypeField.setEnabled(false);

                } else {

                        occasionRadio.setSelected(true);

                        occasionTypeField.setEnabled(true);

                        occasionTypeField.setText(
                                        tableModel.getValueAt(row, 3)
                                                        .toString());

                }

                targetAmountField.setText(
                                tableModel.getValueAt(row, 4)
                                                .toString());

                savedAmountField.setText(
                                tableModel.getValueAt(row, 5)
                                                .toString());

                deadlineField.setText(
                                tableModel.getValueAt(row, 6)
                                                .toString());

        }

        private void clearFields() {

                goalNameField.setText("");

                occasionTypeField.setText("");

                targetAmountField.setText("");

                savedAmountField.setText("");

                deadlineField.setText("");

                generalRadio.setSelected(true);

                occasionTypeField.setEnabled(false);

                selectedGoalId = -1;

        }

}
