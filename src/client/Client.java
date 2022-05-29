package client;

import person.Person;

public class Client extends Person {

    private final Basket basket;
    private double balance;

    public Client(String name, double balance) {
        super(name);
        this.balance = balance;
        this.basket = new Basket();
    }

    public Basket getBasket() {
        return basket;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
