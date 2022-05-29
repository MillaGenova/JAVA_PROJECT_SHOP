package shop;

import client.Client;
import person.Person;

public class Cashier extends Person {
    private static long idCounter = 0;

    private String ID = "CASHIER_";
    private double salary;
    private String assignedCheckout;
    private String workplace;

    public Cashier(String name, double salary) {
        super(name);
        this.ID = "CASHIER_" + idCounter++;
        this.salary = salary;
    }

    public String getID() {
        return ID;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getAssignedCheckout() {
        return assignedCheckout;
    }

    public void setAssignedCheckout(String assignedCheckout) {
        this.assignedCheckout = assignedCheckout;
    }

    public void chargeClient(Client client, double price) {
        client.setBalance(client.getBalance() - price);
    }

    @Override
    public String toString() {
        return "\n \sCashier{" +
                " \n \s \sname = '" + super.getName() + '\'' +
                ",\n \s \sworkplace = '" + workplace + '\'' +
                ",\n \s \sID = '" + ID + '\'' +
                ",\n \s \ssalary = " + salary +
                '}';
    }
}
