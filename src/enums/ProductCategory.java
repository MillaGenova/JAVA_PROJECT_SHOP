package enums;

public enum ProductCategory {
    FOOD_PRODUCT(0), NONFOOD_PRODUCT(0);

    private double markup;

    ProductCategory(double markup) {
        this.markup = markup;
    }

    public double getMarkup() {
        return markup;
    }

    public void setMarkup(double markup) {
        this.markup = markup;
    }
}
