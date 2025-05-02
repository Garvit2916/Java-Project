package service;

public interface BillOperations {
    void addBill(String name, int units, String type);
    void displayAllBills();
    void searchBill(String customerId);
    void deleteBill(String customerId);
    void updateBill(String customerId, String newName, int newUnits); 
    void payBill(String customerId);
    void increaseDays();
}