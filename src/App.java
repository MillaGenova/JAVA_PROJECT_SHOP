import client.Client;
import enums.ProductCategory;
import enums.ProductsDatabaseCategory;
import exceptions.ProductsDatabaseException;
import person.Person;
import products.Product;
import shop.Cashier;
import shop.ProductsDatabase;
import shop.Shop;

import java.time.LocalDate;


public class App {
    public static void main(String[] args) throws Exception {
        //  Create some products
        Product apple = new Product("apple", 0.5, ProductCategory.FOOD_PRODUCT, LocalDate.of(2022, 12, 5));
        Product chocolate = new Product("chocolate", 3.65, ProductCategory.FOOD_PRODUCT, LocalDate.of(2022, 12, 5));
        Product toy = new Product("toy", 19, ProductCategory.NONFOOD_PRODUCT, null);
        Product phone = new Product("phone", 999, ProductCategory.NONFOOD_PRODUCT, null);
        Product battery = new Product("battery", 5, ProductCategory.NONFOOD_PRODUCT, null);
        Product toothPaste = new Product("toothPaste", 8, ProductCategory.NONFOOD_PRODUCT, null);
        // Expired food products
        Product tomato = new Product("tomato", 2.46, ProductCategory.FOOD_PRODUCT, LocalDate.of(2022, 2, 5));

        //  Create some people
        Person bradGray = new Person("Brad Gray");
        Person joeDove = new Person("Joe Dove");
        Person drewBlack = new Person("Drew Black");


    //  Now lets create real shop
        // Creating a shop with:
        // foodMarkup: 15%, nonFoodMarkup: 22.5%, daysCloseToExpire: 30, expireDiscount: 30%
        Shop shop = new Shop("Wallmart", 15, 22.5, 30, 30);
        // Delivering come products
        shop.deliverProduct(apple, 10);
        shop.deliverProduct(chocolate, 10);
        shop.deliverProduct(phone, 3);
        shop.deliverProduct(toy, 30);
        shop.deliverProduct(battery, 15);
        shop.deliverProduct(tomato, 25);
        //Deliver little amount of tooth paste
        shop.deliverProduct(toothPaste, 4);

        // Hire 2 cashiers:
        shop.hireCashier(joeDove, 650);
        shop.hireCashier(bradGray, 965);
        // Add 2 checkouts and assign cashiers to them:
        shop.createNewCheckout();
        shop.createNewCheckout();
        shop.assignCashierToCheckout(shop.getCashiers().get(0), shop.getCheckouts().get(0));
        shop.assignCashierToCheckout(shop.getCashiers().get(1), shop.getCheckouts().get(1));

        // Add clients
        Client client_BradCooper = new Client("Brad Cooper", 2560);
        try {
            client_BradCooper.getBasket().addProducts(apple, 7);
            client_BradCooper.getBasket().addProducts(phone, 2);
            client_BradCooper.getBasket().addProducts(toothPaste, 1);
        } catch (ProductsDatabaseException exception) {
            exception.printStackTrace();
        }

        Client client_JessyDime = new Client("Jessy Dime", 1685);
        try {
            client_JessyDime.getBasket().addProducts(toy, 11);
            client_JessyDime.getBasket().addProducts(phone, 1);
            client_JessyDime.getBasket().addProducts(toothPaste, 1);

        } catch (ProductsDatabaseException exception) {
            exception.printStackTrace();
        }

        Client client_EmilFracues = new Client("Emil Fracues", 2120);
        try {
            client_EmilFracues.getBasket().addProducts(toy, 11);
        } catch (ProductsDatabaseException exception) {
            exception.printStackTrace();
        }
        
        // client `Sam Wiki` will want to buy from the expired tomatoes
        Client client_SamWiki = new Client("Sam Wiki", 1600);
        try {
            client_SamWiki.getBasket().addProducts(tomato, 11);
            client_SamWiki.getBasket().addProducts(chocolate, 5);

        } catch (ProductsDatabaseException exception) {
            exception.printStackTrace();
        }

        // client `Ivan Chorgs` have too little budget
        Client client_IvanChorgs = new Client("Ivan Chorgs", 60);
        try {
            client_IvanChorgs.getBasket().addProducts(battery, 11);

        } catch (ProductsDatabaseException exception) {
            exception.printStackTrace();
        }

        // client `Janna Dark` want a lot of tooth paste
        Client client_JannaDark = new Client("Janna Dark", 3140);
        try {
            client_JannaDark.getBasket().addProducts(toothPaste, 5);
            client_JannaDark.getBasket().addProducts(apple, 3);
            client_JannaDark.getBasket().addProducts(phone, 1);


        } catch (ProductsDatabaseException exception) {
            exception.printStackTrace();
        }

        shop.getCheckouts().get(0).getClients().add(client_JessyDime);
        shop.getCheckouts().get(1).getClients().add(client_EmilFracues);
        shop.getCheckouts().get(0).getClients().add(client_BradCooper);
        shop.getCheckouts().get(0).getClients().add(client_SamWiki);
        shop.getCheckouts().get(1).getClients().add(client_IvanChorgs);
        shop.getCheckouts().get(1).getClients().add(client_JannaDark);


        shop.startWork();
        
        Thread.sleep(2000);
        System.out.println("Salary expenses: " + shop.calculateSalaryExpenses());
        System.out.println("Delivered products expenses: " + shop.calculateDeliveryExpenses());
        System.out.println("Sold products income: " + shop.calculateSoldProductsIncome());
        System.out.println("Total earning: " + shop.calculateTotalEarnings());
        System.out.println("Total count of receipts: " + shop.getReceiptsCount());
        System.out.println("Total income according to receipts: " + shop.getReceiptTotalIncome());
    }
}
