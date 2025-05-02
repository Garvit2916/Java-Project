package service;

import model.Bill;
import model.BillTypes.CommercialBill;
import model.BillTypes.ResidentialBill;

import java.sql.*;
import java.util.*;

public class BillService implements BillOperations {
    private final List<Bill> billList = new ArrayList<>();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public void addBill(String name, int units, String type) {
        for (Bill b : billList) {
            if (b.name.equalsIgnoreCase(name)) {
                System.out.println("Bill already exists for this customer.");
                return;
            }
        }

        Bill bill;
        if (type.equalsIgnoreCase("residential")) {
            bill = new ResidentialBill(name, units);
        } else if (type.equalsIgnoreCase("commercial")) {
            bill = new CommercialBill(name, units);
        } else {
            System.out.println("Invalid bill type. Please use Residential or Commercial.");
            return;
        }

        billList.add(bill);
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = con.prepareStatement(
                     "INSERT INTO bills VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, bill.customerId);
            stmt.setString(2, bill.name);
            stmt.setInt(3, bill.units);
            stmt.setString(4, bill.type);
            stmt.setDouble(5, bill.totalBill);
            stmt.setInt(6, bill.daysSinceGeneration);
            stmt.setBoolean(7, bill.isPaid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        System.out.println("Bill added successfully for " + name + " with Customer ID: " + bill.customerId);
    }


    public void displayAllBills() {
        billList.clear();
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM bills")) {
            while (rs.next()) {
                Bill bill;
                String type = rs.getString("type");
                if (type.equalsIgnoreCase("residential")) {
                    bill = new ResidentialBill(rs.getString("name"), rs.getInt("units"));
                } else {
                    bill = new CommercialBill(rs.getString("name"), rs.getInt("units"));
                }
                bill.customerId = rs.getString("customer_id");
                bill.totalBill = rs.getDouble("total_bill");
                bill.daysSinceGeneration = rs.getInt("days_since_generation");
                bill.isPaid = rs.getBoolean("is_paid");

                billList.add(bill);
                bill.displayBillDetails();
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void searchBill(String customerId) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM bills WHERE customer_id = ?")) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                Bill bill = type.equalsIgnoreCase("residential") ?
                            new ResidentialBill(rs.getString("name"), rs.getInt("units")) :
                            new CommercialBill(rs.getString("name"), rs.getInt("units"));
                bill.customerId = rs.getString("customer_id");
                bill.totalBill = rs.getDouble("total_bill");
                bill.daysSinceGeneration = rs.getInt("days_since_generation");
                bill.isPaid = rs.getBoolean("is_paid");
                System.out.println("Bill found:");
                bill.displayBillDetails();
            } else {
                System.out.println("Bill not found for Customer ID: " + customerId);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void deleteBill(String customerId) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = con.prepareStatement("DELETE FROM bills WHERE customer_id = ?")) {
            stmt.setString(1, customerId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Bill deleted successfully for Customer ID: " + customerId);
            } else {
                System.out.println("Bill not found for Customer ID: " + customerId);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void updateBill(String customerId, String newName, int newUnits) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement getStmt = con.prepareStatement("SELECT * FROM bills WHERE customer_id = ?");
             PreparedStatement updateStmt = con.prepareStatement(
                     "UPDATE bills SET name = ?, units = ?, total_bill = ? WHERE customer_id = ?")) {
            getStmt.setString(1, customerId);
            ResultSet rs = getStmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                Bill bill = type.equalsIgnoreCase("residential") ?
                            new ResidentialBill(newName, newUnits) :
                            new CommercialBill(newName, newUnits);
                bill.customerId = customerId;

                updateStmt.setString(1, newName);
                updateStmt.setInt(2, newUnits);
                updateStmt.setDouble(3, bill.totalBill);
                updateStmt.setString(4, customerId);
                updateStmt.executeUpdate();

                System.out.println("Bill updated successfully:");
                bill.displayBillDetails();
            } else {
                System.out.println("Bill not found for Customer ID: " + customerId);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void increaseDays() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = con.createStatement()) {
            int rows = stmt.executeUpdate("UPDATE bills SET days_since_generation = days_since_generation + 1");
            System.out.println("1 day added to " + rows + " bills.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void payBill(String customerId) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement getStmt = con.prepareStatement("SELECT * FROM bills WHERE customer_id = ?");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE bills SET is_paid = ? WHERE customer_id = ?")) {

            getStmt.setString(1, customerId);
            ResultSet rs = getStmt.executeQuery();
            if (rs.next()) {
                boolean isPaid = rs.getBoolean("is_paid");
                if (isPaid) {
                    System.out.println("Bill already paid for Customer ID: " + customerId);
                    return;
                }

                int days = rs.getInt("days_since_generation");
                double fine = 0;
                if (days > 15) {
                    fine = (days - 15) * 10;
                }
                double total = rs.getDouble("total_bill") + fine;
                updateStmt.setBoolean(1, true);
                updateStmt.setString(2, customerId);
                updateStmt.executeUpdate();

                System.out.println("Amount paid (including fine): ₹" + total);
            } else {
                System.out.println("Bill not found for Customer ID: " + customerId);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
