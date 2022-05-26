package shop;


import enums.CheckoutState;
import enums.ProductCategory;
import enums.ProductsDatabaseCategory;
import exceptions.ProductsDatabaseException;
import person.Person;
import products.Product;

import java.util.ArrayList;
import java.util.List;


public class Shop {
    private String name;
    private double foodMarkup;
    private double nonFoodMarkup;
    private int daysCloseToExpire;
    private double expireDiscount;

    private final ProductsDatabase deliveredProducts;
    private final ProductsDatabase soldProducts;
    private final ProductsDatabase inventory;

    private final List<Cashier> cashiers;
    private final List<Checkout> checkouts;

    private int receiptsCount;
    private double receiptTotalIncome;

    public Shop(String name, double foodMarkup, double nonFoodMarkup, int daysCloseToExpire, double expireDiscount) {
        this.name = name;
        this.foodMarkup = foodMarkup;
        this.nonFoodMarkup = nonFoodMarkup;
        this.daysCloseToExpire = daysCloseToExpire;
        this.expireDiscount = expireDiscount;

        this.deliveredProducts = new ProductsDatabase(ProductsDatabaseCategory.DATABASE_DELIVERED);
        this.soldProducts = new ProductsDatabase(ProductsDatabaseCategory.DATABASE_SOLD);
        this.inventory = new ProductsDatabase(ProductsDatabaseCategory.DATABASE_INVENTORY);

        this.cashiers = new ArrayList();
        this.checkouts = new ArrayList();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFoodMarkup() {
        return foodMarkup;
    }

    public void setFoodMarkup(double foodMarkup) {
        this.foodMarkup = foodMarkup;
    }

    public double getNonFoodMarkup() {
        return nonFoodMarkup;
    }

    public void setNonFoodMarkup(double nonFoodMarkup) {
        this.nonFoodMarkup = nonFoodMarkup;
    }

    public int getDaysCloseToExpire() {
        return daysCloseToExpire;
    }

    public void setDaysCloseToExpire(int daysCloseToExpire) {
        this.daysCloseToExpire = daysCloseToExpire;
    }

    public double getExpireDiscount() {
        return expireDiscount;
    }

    public void setExpireDiscount(double expireDiscount) {
        this.expireDiscount = expireDiscount;
    }

    public ProductsDatabase getDeliveredProducts() {
        return deliveredProducts;
    }

    public ProductsDatabase getSoldProducts() {
        return soldProducts;
    }

    public ProductsDatabase getInventory() {
        return inventory;
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public List<Checkout> getCheckouts() {
        return checkouts;
    }

    public int getReceiptsCount() {
        return receiptsCount;
    }

    public void setReceiptsCount(int receiptsCount) {
        this.receiptsCount = receiptsCount;
    }

    public double getReceiptTotalIncome() {
        // return receiptTotalIncome;
        return Math.round(receiptTotalIncome * 100.0) / 100.0;
    }

    public void setReceiptTotalIncome(double receiptTotalIncome) {
        this.receiptTotalIncome = receiptTotalIncome;
    }

    public void deliverProduct(Product product, int amount) {
        // `deliverProduct` handles delivering products int the shop.
        // First we add it in `deliveredProducts` database, then set the
        // final price of the product according to this shop and last add
        // the product in the shop inventory
        try {
            this.deliveredProducts.addProducts(product, amount);
            product.setFinalPrice(this.foodMarkup, this.nonFoodMarkup,
                    this.daysCloseToExpire, this.expireDiscount);
            this.inventory.addProducts(product, amount);
        } catch (ProductsDatabaseException exception) {
            exception.printStackTrace();
        }
    }

    public void sellProduct(Product product, int amount) {
        // `sellProduct` handles selling products in the shop.
        // First the product is removed form the `inventory`, then added
        // to the `soldProducts` database. Inconsistences  in amount of product
        // are handled via exceptions
        int existingProducts = this.inventory.getProducts().get(product);
        try {
            this.inventory.removeProducts(product, amount);
            this.soldProducts.addProducts(product, amount);
        } catch (ProductsDatabaseException exception) {
            try {
                this.soldProducts.addProducts(product, existingProducts);
            } catch (ProductsDatabaseException exceptionAddProduct) {
                exceptionAddProduct.printStackTrace();
            }
            exception.printStackTrace();
        }
    }

    public void hireCashier(Person futureCashier, double salary) {
        Cashier newCashier = new Cashier(futureCashier.getName(), salary);
        newCashier.setWorkplace(this.getName());
        this.cashiers.add(newCashier);
    }

    public void createNewCheckout() {
        Checkout newCheckout = new Checkout();
        newCheckout.setShop(this);
        newCheckout.setIsWorking(CheckoutState.NONWORKING_CHECKOUT);

        this.checkouts.add(newCheckout);
    }

    public void assignCashierToCheckout(Cashier cashier, Checkout checkout) {
        // We assume that when `cashier` is assigned to `checkout`,
        // the checkout is in working state
        checkout.setWorkingCashier(cashier);
        checkout.setIsWorking(CheckoutState.WORKING_CHECKOUT);
        cashier.setAssignedCheckout(checkout.getID());
    }

    public double calculateSalaryExpenses() {
        double totalExpenses = 0;
        for (Cashier worker : this.getCashiers()) {
            totalExpenses += worker.getSalary();
        }
        totalExpenses = Math.round(totalExpenses * 100.0) / 100.0;
        return totalExpenses;
    }

    public double calculateDeliveryExpenses() {
        double totalExpenses = 0;
        totalExpenses = this.getDeliveredProducts().calculateTotal();
        return Math.round(totalExpenses * 100.0) / 100.0;
    }

    public double calculateSoldProductsIncome() {
        double totalIncome = 0;
        totalIncome = this.getSoldProducts().calculateTotal();
        return Math.round(totalIncome * 100.0) / 100.0;
    }

    public double calculateTotalEarnings() {
        double totalEarning = this.calculateSoldProductsIncome() -
                this.calculateDeliveryExpenses() - this.calculateSalaryExpenses();
        return Math.round(totalEarning * 100.0) / 100.0;
        
    }

    public void startWork() {
        for (Checkout checkout : checkouts) {
            Thread thread = new Thread(new Service(this, checkout, checkout.getWorkingCashier()));
            thread.start();
        }
    }

    @Override
    public String toString() {
        return "\n \sShop{" +
                "\n \s \sname='" + name + '\'' +
                ",\n \s \sfoodMarkup=" + foodMarkup +
                ",\n \s \snonFoodMarkup=" + nonFoodMarkup +
                ",\n \s \sdaysCloseToExpire=" + daysCloseToExpire +
                ",\n \s \sexpireDiscount=" + expireDiscount +
                ",\n \s \sdeliveredProducts=" + deliveredProducts +
                ",\n \s \ssoldProducts=" + soldProducts +
                ",\n \s \sinventory=" + inventory +
                ",\n \s \scashiers=" + cashiers +
                '}';
    }
}
