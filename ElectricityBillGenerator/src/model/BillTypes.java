package model;

public class BillTypes {
    private static int idCounter = 1;

    public static class ResidentialBill extends Bill {
        public ResidentialBill(String name, int units) {
            super("CUST" + idCounter++, name, units, "Residential");
        }

        public void calculateBill() {
            totalBill = units * 2.0;
        }
    }

    public static class CommercialBill extends Bill {
        public CommercialBill(String name, int units) {
            super("CUST" + idCounter++, name, units, "Commercial");
        }

        @Override
        public void calculateBill() {
            totalBill = units * 5.0;
        }
    }
}