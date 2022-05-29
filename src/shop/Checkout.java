package shop;

import client.Client;
import enums.CheckoutState;
import exceptions.CheckoutException;

import java.util.LinkedList;
import java.util.Queue;

public class Checkout {
    private static long idCounter = 0;

    private CheckoutState isWorking;
    private String ID = "CHECKOUT_";
    private Cashier workingCashier;

    private Shop shop;
    private final Queue<Client> clients;

    public Checkout() {
        this.ID = "CHECKOUT_" + idCounter++;
        this.clients = new LinkedList<Client>();
    }

    public String getID() {
        return ID;
    }

    public Cashier getWorkingCashier() {
        return workingCashier;
    }

    public void setWorkingCashier(Cashier workingCashier) {
        this.workingCashier = workingCashier;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public CheckoutState getIsWorking() {
        return isWorking;
    }

    public void setIsWorking(CheckoutState isWorking) {
        this.isWorking = isWorking;
    }

    public boolean isWorking() {
        return this.getIsWorking() != CheckoutState.NONWORKING_CHECKOUT;
    }

    public Queue<Client> getClients() {
        return clients;
    }

    public boolean checkIfWorking() throws CheckoutException {
        if (this.isWorking()) {
            return true;
        } else {
            throw new CheckoutException(String.format("Checkout with ID: " +
                    this.getID() + " is not working right now!"));
        }
    }

    @Override
    public String toString() {
        String workingCashier;
        if (this.workingCashier == null) {
            workingCashier = "No cashier assigned to this checkout";
        } else {
            workingCashier = this.workingCashier.getName();
        }
        return "Checkout{" +
                "CheckoutID = '" + ID + '\'' +
                ", Is checkout working = " + this.isWorking() +
                ", Cashier name = '" + workingCashier + '\'' +
                '}';
    }

}
