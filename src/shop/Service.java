package shop;

import client.Basket;
import client.Client;
import enums.ProductsDatabaseCategory;
import exceptions.CheckoutException;
import exceptions.ExpiredDateException;
import exceptions.MoneyException;
import exceptions.ProductsDatabaseException;
import products.Product;
import receipt.ProductInfoForReceipt;
import receipt.Receipt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

public class Service implements Runnable {
    private final Shop shop;
    private final Checkout checkout;
    private final Cashier cashier;

    public Service(Shop shop, Checkout checkout, Cashier cashier) {
        this.shop = shop;
        this.checkout = checkout;
        this.cashier = cashier;
    }

    public ProductsDatabase checkoutProducts(Basket basket) {
        ProductsDatabase clientsProducts = new ProductsDatabase(ProductsDatabaseCategory.DATABASE_SOLD);
        for (Entry<Product, Integer> entry : basket.getProducts().entrySet()) {
            try {
                clientsProducts.checkoutAvailable(shop.getInventory(),
                        entry.getKey(),
                        entry.getValue().intValue());
            } catch (ProductsDatabaseException exception) {
                exception.printStackTrace();
            } catch (ExpiredDateException exception) {
                exception.printStackTrace();
            }
        }
        return clientsProducts;
    }

    public double calculateTotal(ProductsDatabase clientsProducts) {
        double total = 0;
        for (Entry<Product, Integer> entry : clientsProducts.getProducts().entrySet()) {
            total += entry.getKey().getFinalPrice() * entry.getValue().intValue();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public synchronized void sellProducts(ProductsDatabase clientsProducts) {
        for (Entry<Product, Integer> entry : clientsProducts.getProducts().entrySet()) {
            shop.sellProduct(entry.getKey(), entry.getValue());
        }
    }

    public void printCheckoutCurrInfo(ProductsDatabase clientsProducts, Receipt clientReceipt) {
        System.out.println(
                "Checkout: " + checkout.getID() + "\n"
                        + "Working cashier: " + cashier.getName() + "\n"
                        + "Total price: " + clientsProducts.calculateTotal() + "\n"
                        + "Receipt:" + "\n"
                        + clientReceipt.toString() + "\n"
                        + "\n");
    }

    public void createReceipt(ProductsDatabase clientsProducts) throws IOException {
        ArrayList<ProductInfoForReceipt> productsInfo = new ArrayList();
        for (Entry<Product, Integer> entry : clientsProducts.getProducts().entrySet()) {
            double totalProductPrice = Math.round(entry.getValue() * entry.getKey().getFinalPrice() * 100.0) / 100.0;
            productsInfo.add(new ProductInfoForReceipt(entry.getKey().getName(), entry.getValue(),
                    entry.getKey().getFinalPrice(), totalProductPrice));
        }
        double total = calculateTotal(clientsProducts);
        Receipt clientReceipt = new Receipt(shop.getName(), cashier.getID(),
                cashier.getName(), productsInfo, total);
        printCheckoutCurrInfo(clientsProducts, clientReceipt);

        try {
            String receiptFile = System.getProperty("user.dir") + "/receipts/" + clientReceipt.getID() + ".txt";
            FileWriter myWriter = new FileWriter(receiptFile);
            myWriter.write(clientReceipt.printBill());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Failed to create receipt file!");
            e.printStackTrace();
        }
    }


    public synchronized ProductsDatabase checkoutAndSellProducts(Client client) throws MoneyException {
        ProductsDatabase checkoutProducts = checkoutProducts(client.getBasket());
        double total = checkoutProducts.calculateTotal();
        if (total <= client.getBalance()) {
            sellProducts(checkoutProducts);
            cashier.chargeClient(client, total);
            shop.setReceiptsCount(
                    shop.getReceiptsCount() + 1);
            shop.setReceiptTotalIncome(
                    shop.getReceiptTotalIncome() + checkoutProducts.calculateTotal());

            return checkoutProducts;
        } else {
            throw new MoneyException("Client does not have enough money! Service denied!");
        }
    }

    public synchronized void serveClient(Client client) {
        // Products of client's basked are being scanned.
        // Available items are added to new list. Final price is being
        // calculated so the client can know how much have to pay.
        // If client has enough money items are sold.
        // Receipt is printed.
        ProductsDatabase checkoutProducts = new ProductsDatabase((ProductsDatabaseCategory.DATABASE_SOLD));
        synchronized (shop) {
            try {
                checkoutProducts = checkoutAndSellProducts(client);
            } catch (MoneyException moneyException) {
                moneyException.printStackTrace();
                return;
            }
        }
        try {
            createReceipt(checkoutProducts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            checkout.checkIfWorking();
            Iterator<Client> itr = checkout.getClients().iterator();
            while (itr.hasNext()) {
                serveClient(itr.next());
            }
        } catch (CheckoutException exception) {
            System.out.println("Sorry, Checkout \"" + checkout.getID() + "\" is not working right now!");
        }

    }
}
