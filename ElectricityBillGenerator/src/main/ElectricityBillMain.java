package main;

import service.BillService;
import java.util.Scanner;

public class ElectricityBillMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BillService service = new BillService();
        int choice;

        do {
            System.out.println("\n--- Electricity Bill System ---");
            System.out.println("1. Add Bill");
            System.out.println("2. Display All Bills");
            System.out.println("3. Search Bill");
            System.out.println("4. Delete Bill");
            System.out.println("5. Update Bill");
            System.out.println("6. Increase Days (Simulate 1 day)");
            System.out.println("7. Pay Bill");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Please enter a number: ");
                sc.next();
            }

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Units: ");
                    int units = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Type (Residential/Commercial): ");
                    String type = sc.nextLine();
                    service.addBill(name, units, type);
                    break;
                case 2:
                    service.displayAllBills();
                    break;
                case 3:
                    System.out.print("Enter Customer ID: ");
                    String id = sc.nextLine();
                    service.searchBill(id);
                    break;
                case 4:
                    System.out.print("Enter Customer ID to delete: ");
                    String delId = sc.nextLine();
                    service.deleteBill(delId);
                    break;
                case 5:
                    System.out.print("Enter Customer ID to update: ");
                    String updateId = sc.nextLine();
                    System.out.print("Enter new name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter new units: ");
                    int newUnits = sc.nextInt();
                    sc.nextLine();
                    service.updateBill(updateId, newName, newUnits);
                    break;
                case 6:
                    service.increaseDays();
                    break;
                case 7:
                    System.out.print("Enter Customer ID to Pay: ");
                    String payId = sc.nextLine();
                    service.payBill(payId);
                    break;
                case 8:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 8);  

        sc.close();
    }
}