package shop;

import enums.ProductCategory;
import enums.ProductsDatabaseCategory;
import exceptions.ExpiredDateException;
import exceptions.ProductsDatabaseException;
import interfaces.ProductStorage;
import products.Product;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map.Entry;

public class ProductsDatabase implements ProductStorage {
    final private ProductsDatabaseCategory inventoryCategory;
    private final HashMap<Product, Integer> products;

    public ProductsDatabase(final ProductsDatabaseCategory inventoryCategory) {
        this.products = new HashMap<>();
        this.inventoryCategory = inventoryCategory;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public void addProducts(final Product product, final int amount) throws ProductsDatabaseException {
        if (product != null) {
            final int currentAmount = this.products.getOrDefault(product, 0);
            this.products.put(product, currentAmount + amount);
        } else {
            throw new ProductsDatabaseException("You cannot add Null objects to products!");
        }
    }

    public void removeProducts(final Product product, final int amount) throws ProductsDatabaseException {
        // `removeProducts` removes `amount` number of product form data base if
        // there are `amount` or more existing instances. If there are less existing
        // instances, all are removed form the data base.
        if (this.products.get(product) >= amount) {
            this.products.replace(product, this.products.get(product) - amount);
        } else {
            int existingInstances = this.products.get(product);
            this.products.replace(product, 0);
            throw new ProductsDatabaseException(String.format("Cannot remove %d instances" +
                            " of product \"" + product.getName() + "\" as there are %d instances! " +
                            "Removing all existing instances",
                    amount, existingInstances));
        }
    }

    public synchronized void checkoutAvailable(ProductsDatabase shopInventory,
                                               final Product product,
                                               final int amount) throws ProductsDatabaseException, ExpiredDateException {
        // `checkoutAvailable` adds available products from other db ot this
        int availableAmount = shopInventory.getProducts().get(product);
        if (product.getCategory() == ProductCategory.FOOD_PRODUCT) {
            long leftDays = ChronoUnit.DAYS.between(LocalDate.now(), product.getExpireDate());
            if (leftDays < 0) {
                try {
                    shopInventory.removeProducts(product, availableAmount);
                } catch (ProductsDatabaseException exception) {
                    exception.printStackTrace();
                }
                throw new ExpiredDateException("Cannot sell product \""
                        + product.getName() + "\" as it has expired.");
            }
        }
        if (availableAmount >= amount) {
            try {
                this.addProducts(product, amount);
            } catch (ProductsDatabaseException exceptionAddProduct) {
                exceptionAddProduct.printStackTrace();
            }
        } else {
            this.addProducts(product, availableAmount);
            throw new ProductsDatabaseException(String.format("Cannot sell %d instances" +
                            " of product \"" + product.getName() + "\" as there are %d instances! " +
                            "All existing instances will be sold to the client",
                    amount, availableAmount));
        }
    }

    public double calculateTotal() {
        double total = 0;
        if (this.inventoryCategory == ProductsDatabaseCategory.DATABASE_DELIVERED) {
            for (Entry<Product, Integer> entry : this.products.entrySet()) {
                total += entry.getKey().getDeliveryPrice() * entry.getValue();
            }
            total = Math.round(total * 100.0) / 100.0;
            return total;
        } else {
            for (Entry<Product, Integer> entry : this.products.entrySet()) {
                total += entry.getKey().getFinalPrice() * entry.getValue();
            }
            total = Math.round(total * 100.0) / 100.0;
            return total;
        }

    }

    @Override
    public String toString() {
        return "ProductsDatabase{" +
                "\n\s inventoryCategory=" + inventoryCategory +
                ",\n\s products=" + products +
                '}';
    }
}
