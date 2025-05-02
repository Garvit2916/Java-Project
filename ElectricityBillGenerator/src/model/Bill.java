package model;

public abstract class Bill {
    public String customerId;
    public String name;
    public int units;
    public String type;
    public double totalBill;
    public boolean isPaid;
    public int daysSinceGeneration;

    public Bill(String customerId, String name, int units, String type) {
        this.customerId = customerId;
        this.name = name;
        this.units = units;
        this.type = type;
        this.isPaid = false;
        this.daysSinceGeneration = 0;
        calculateBill();
    }

    public abstract void calculateBill();

    public void displayBillDetails() {
        System.out.println("\nCustomer ID: " + customerId);
        System.out.println("Name: " + name);
        System.out.println("Units Consumed: " + units);
        System.out.println("Total Bill: ₹" + totalBill);
        System.out.println("Status: " + (isPaid ? "Paid" : "Unpaid"));

        if (!isPaid && daysSinceGeneration > 15) {
            int lateDays = daysSinceGeneration - 15;
            double fine = lateDays * 10;
            System.out.println("Late Fine: ₹" + fine);
            System.out.println("Total Payable (Bill + Fine): ₹" + (totalBill + fine));
        }
    }
}