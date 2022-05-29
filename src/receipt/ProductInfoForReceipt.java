package receipt;

public class ProductInfoForReceipt {
    private String productName;
    private double productPrice;
    private int productAmount;
    private double totalProductPrice;

    public ProductInfoForReceipt(String productName, int productAmount, double productPrice, double totalProductPrice) {
        this.productName = productName;
        this.productAmount = productAmount;
        this.totalProductPrice = totalProductPrice;
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(double totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}
