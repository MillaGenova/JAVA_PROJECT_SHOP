package products;
import enums.ProductCategory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Product {
    private static long idCounter = 0;

    private long ID = 0;
    private String name;
    private double deliveryPrice;
    private ProductCategory category;
    private LocalDate expireDate;

    private double finalPrice;

    public Product(String name, double deliveryPrice, ProductCategory category, LocalDate expireDate) {
        this.ID = idCounter++;
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.category = category;
        this.expireDate = expireDate;
    }
    
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double newPrice) {
        this.finalPrice = newPrice;
    }

    public void setFinalPrice(double foodMarkup, double nonFoodMarkup, int daysCloseToExpire, double expireDiscount) {
        double priceWithoutDiscount, finalPriceNotFormatted;

        if(this.category == ProductCategory.FOOD_PRODUCT){
            priceWithoutDiscount = this.deliveryPrice + this.deliveryPrice * foodMarkup / 100;
        }
        else{
            priceWithoutDiscount = this.deliveryPrice + this.deliveryPrice * nonFoodMarkup / 100;
        }
        if(this.category == ProductCategory.FOOD_PRODUCT){
            long leftDays =  ChronoUnit.DAYS.between(LocalDate.now(), this.expireDate);
            if (daysCloseToExpire >= leftDays){
                finalPriceNotFormatted = priceWithoutDiscount - priceWithoutDiscount * expireDiscount / 100;
                this.finalPrice = Math.round(finalPriceNotFormatted * 100.0) / 100.0;
            }
            else{
                this.finalPrice = Math.round(priceWithoutDiscount * 100.0) / 100.0;
            }
        }
        else{
            this.finalPrice = Math.round(priceWithoutDiscount * 100.0) / 100.0;
        }
        
 
    }


    @Override
    public String toString() {
        return "\n \s Product{" +
                " \n \s \s name = '" + name + '\'' +
                ", \n \s \s category = " + category +
                ", \n \s \s expire date = " + expireDate +
                ", \n \s \s delivery price = " + deliveryPrice + "$" +
                ", \n \s \s final price = " + finalPrice + "$" +

                "}";
    }
}
