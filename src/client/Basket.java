package client;

import exceptions.ProductsDatabaseException;
import interfaces.ProductStorage;
import products.Product;

import java.util.HashMap;

public class Basket implements ProductStorage {

    private final HashMap<Product, Integer> products;

    public Basket() {
        this.products = new HashMap<>();
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    @Override
    public void addProducts(final Product product, final int amount) throws ProductsDatabaseException {
        if (product != null) {
            final int currentAmount = this.products.getOrDefault(product, 0);
            this.products.put(product, currentAmount + amount);
        } else {
            throw new ProductsDatabaseException("You cannot add Null objects to products!");
        }
    }

    @Override
    public void removeProducts(final Product product, final int amount) throws ProductsDatabaseException {
        if (this.products.get(product) >= amount) {
            this.products.replace(product, this.products.get(product) - amount);
        } else {
            int existingInstances = this.products.get(product);
            this.products.replace(product, 0);
            throw new ProductsDatabaseException(String.format("Cannot remove more products, than added in basket" +
                            "Removing all \"" + product.getName() + "\" products from basket",
                    amount, existingInstances));
        }
    }

    @Override
    public double calculateTotal() {
        return this.products.entrySet().
                parallelStream().
                mapToDouble(product -> product.getKey().getFinalPrice() * product.getValue()).
                sum();
    }

    @Override
    public String toString() {
        return "Basket{" +
                "products = " + products +
                '}';
    }
}
