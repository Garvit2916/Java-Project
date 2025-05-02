package ui;

import service.BillService;

import javax.swing.*;
import java.awt.*;

public class ElectricityBillGUI {

    private static BillService billService = new BillService();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Electricity Bill System");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JTextField nameField = new JTextField(15);
        JTextField unitsField = new JTextField(10);
        JTextField idField = new JTextField(10);

        // ComboBox for Bill Type selection
        String[] billTypes = {"Residential", "Commercial"};
        JComboBox<String> typeComboBox = new JComboBox<>(billTypes);

        JButton addButton = new JButton("Add Bill");
        JButton displayButton = new JButton("Display All Bills");
        JButton searchButton = new JButton("Search Bill");
        JButton deleteButton = new JButton("Delete Bill");
        JButton updateButton = new JButton("Update Bill");
        JButton payButton = new JButton("Pay Bill");
        JButton increaseDaysButton = new JButton("Increase Days");

        JList<String> billList = new JList<>(new String[]{}); 
        frame.add(new JLabel("Customer Name:"));
        frame.add(nameField);
        frame.add(new JLabel("Units Consumed:"));
        frame.add(unitsField);
        frame.add(new JLabel("Bill Type:"));
        frame.add(typeComboBox);
        frame.add(new JLabel("Customer ID (for search/delete/update/pay):"));
        frame.add(idField);

        frame.add(addButton);
        frame.add(displayButton);
        frame.add(searchButton);
        frame.add(deleteButton);
        frame.add(updateButton);
        frame.add(payButton);
        frame.add(increaseDaysButton);

        // Add Bill
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String unitsText = unitsField.getText().trim();
            String type = (String) typeComboBox.getSelectedItem();

            if (name.isEmpty() || unitsText.isEmpty() || type.isEmpty()) {
                showMessage("All fields must be filled!");
                return;
            }

            try {
                int units = Integer.parseInt(unitsText);
                billService.addBill(name, units, type);
                showMessage("Bill added successfully.");
            } catch (NumberFormatException ex) {
                showMessage("Units must be a number!");
            }
        });

        // Search Bill
        searchButton.addActionListener(e -> {
            String id = idField.getText().trim();
            if (!id.isEmpty()) {
                billService.searchBill(id); // Prints to console
            } else {
                showMessage("Please enter a Customer ID to search.");
            }
        });

        // Delete Bill
        deleteButton.addActionListener(e -> {
            String id = idField.getText().trim();
            if (!id.isEmpty()) {
                billService.deleteBill(id);
            } else {
                showMessage("Please enter a Customer ID to delete.");
            }
        });

        // Update Bill
        updateButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String newName = nameField.getText().trim();
            String unitsText = unitsField.getText().trim();

            if (id.isEmpty() || newName.isEmpty() || unitsText.isEmpty()) {
                showMessage("ID, new name, and new units are required!");
                return;
            }

            try {
                int newUnits = Integer.parseInt(unitsText);
                billService.updateBill(id, newName, newUnits);
            } catch (NumberFormatException ex) {
                showMessage("Units must be a number!");
            }
        });

        // Pay Bill
        payButton.addActionListener(e -> {
            String id = idField.getText().trim();
            if (!id.isEmpty()) {
                billService.payBill(id);
            } else {
                showMessage("Please enter a Customer ID to pay.");
            }
        });

        // Increase Days
        increaseDaysButton.addActionListener(e -> billService.increaseDays());

        frame.setVisible(true);
    }

    private static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}  