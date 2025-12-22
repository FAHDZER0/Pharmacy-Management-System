package Classes;

interface CustomerInterface {
    String getCustomerID();
    String getCustomerName();
    String getPharmacyAssociation();
    String getCustomerAddress();
    String getPersonalPhoneNumber();
    double getHealthInsuranceNumber();
    double getCustomerDebt();
    void printCustomer();
}

public class Customer implements CustomerInterface {
    private final String customerID;   // Made immutable
    private final String customerName;
    private final String pharmacyAssociation;
    private final String customerAddress;
    private final String personalPhoneNumber;
    private final double healthInsuranceNumber;
    private final double customerDebt;

    private Customer(CustomerBuilder builder) {
        this.customerID = builder.customerID;
        this.customerName = builder.customerName;
        this.pharmacyAssociation = builder.pharmacyAssociation;
        this.customerAddress = builder.customerAddress;
        this.personalPhoneNumber = builder.personalPhoneNumber;
        this.healthInsuranceNumber = builder.healthInsuranceNumber;
        this.customerDebt = builder.customerDebt;
    }

    public static class CustomerBuilder {
        private String customerID;
        private String customerName;
        private String pharmacyAssociation = "None";
        private String customerAddress;
        private String personalPhoneNumber = "N/A";
        private double healthInsuranceNumber = 0.0;
        private double customerDebt = 0.0;

        public CustomerBuilder customerID(String id) { this.customerID = id; return this; }
        public CustomerBuilder customerName(String name) { this.customerName = name; return this; }
        public CustomerBuilder pharmacyAssociation(String association) { this.pharmacyAssociation = association; return this; }
        public CustomerBuilder customerAddress(String address) { this.customerAddress = address; return this; }
        public CustomerBuilder personalPhoneNumber(String phone) { this.personalPhoneNumber = phone; return this; }
        public CustomerBuilder healthInsuranceNumber(double insurance) { this.healthInsuranceNumber = insurance; return this; }
        public CustomerBuilder customerDebt(double debt) { this.customerDebt = debt; return this; }

        public Customer build() {
            return new Customer(this);
        }
    }

    public String getCustomerID() {
        return customerID;
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getPharmacyAssociation() {
        return pharmacyAssociation;
    }
    public String getCustomerAddress() {
        return customerAddress;
    }
    public String getPersonalPhoneNumber() {
        return personalPhoneNumber;
    }
    public double getHealthInsuranceNumber() {
        return healthInsuranceNumber;
    }
    public double getCustomerDebt() {
        return customerDebt;
    }
    public void printCustomer() {
        System.out.println("Customer ID: " + customerID);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Pharmacy Association: " + pharmacyAssociation);
        System.out.println("Customer Address: " + customerAddress);
        System.out.println("Personal Phone Number: " + personalPhoneNumber);
        System.out.println("Health Insurance Number: " + healthInsuranceNumber);
        System.out.println("Customer Debt: " + customerDebt);
    }

}