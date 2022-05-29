package receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Receipt {
    private static long idCounter = 0;

    private String ID = "RECEIPT_XXXXX";
    private final String shopName;
    private final String cashierID;
    private final String cashierName;
    private final String date;
    private final String time;
    private final ArrayList<ProductInfoForReceipt> products;
    private final double totalPrice;

    public Receipt(String shopName, String cashierID, String cashierName, ArrayList<ProductInfoForReceipt> products, double totalPrice) {
        this.ID = this.ID + idCounter++;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        //get current date time with Date()
        Date curDate = new Date();
        Date curTime = new Date();
        this.date = dateFormat.format(curDate);
        this.time = timeFormat.format(curTime);
        this.shopName = shopName;
        this.cashierID = cashierID;
        this.cashierName = cashierName;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public String getID() {
        return ID;
    }

    public String printBill() {
        String space = " ";
        String totalPriceStr = "" + totalPrice;
        String shopNameBill =
                space.repeat((14 - shopName.length()) / 2) +
                        shopName +
                        space.repeat((14 - shopName.length()) / 2);

        String cashierNameBill = "Name:" + cashierName;
        if (cashierNameBill.length() > 20) {
            cashierNameBill = cashierNameBill.substring(0, 20) + "  ";
        } else {
            String repeated = space.repeat(20 - cashierNameBill.length());
            cashierNameBill = cashierNameBill + repeated;
        }
        String cashierIDBill = "ID:" + cashierID;
        if (cashierIDBill.length() <= 13) {
            String repeated = space.repeat(32 - cashierIDBill.length() - cashierNameBill.length());
            cashierIDBill = cashierIDBill + repeated;
        }

        String cashierInfo = cashierNameBill + cashierIDBill;
        String receiptId = "ID: " + ID;
        String receiptIdBill =
                space.repeat((22 - receiptId.length()) / 2) +
                        receiptId +
                        space.repeat((22 - receiptId.length()) / 2);

        String Header =
                "╔══════════════════════════════════════╗\n"
                        + "║       *****" + shopNameBill + "*****       ║ \n"
                        + "║  ----------------------------------  ║\n"
                        + "║   Date: " + date + "     Time: " + time + "   ║\n"
                        + "║  ----------------------------------  ║\n"
                        + "║             Cashier Info             ║ \n"
                        + "║   " + cashierInfo + "   ║\n"
                        + "║  ----------------------------------  ║\n"
                        + "║  Name         Amt    Price    Tot    ║\n"
                        + "║  ----------------------------------  ║\n";

        String amt =
                "║ " + space.repeat(36) + " ║\n" +
                        "║ " + space.repeat(36) + " ║\n" +
                        "║ " + space.repeat(36) + " ║\n" +
                        "║  Total Price = " + totalPrice + "$" + space.repeat(21 - totalPriceStr.length()) + "║\n" +
                        "║ " + space.repeat(36) + " ║\n" +
                        "║  ----------------------------------  ║\n" +
                        "║        " + receiptIdBill + "        ║ \n" +
                        "║  ----------------------------------  ║\n" +
                        "║  **********************************  ║\n" +
                        "║  Thank you." + space.repeat(25) + " ║\n" +
                        "╚══════════════════════════════════════╝";

        String bill = Header;
        for (ProductInfoForReceipt el : products) {

            String name = "║  " + el.getProductName();
            String amount = "" + el.getProductAmount();
            String price = "" + el.getProductPrice();
            String total = "" + el.getTotalProductPrice();

            if (name.length() > 15) {
                name = name.substring(0, 15) + "  ";
            } else {
                String repeated = space.repeat(15 - name.length());
                name = name + repeated;
            }

            if (amount.length() <= 7) {
                String repeated = space.repeat(7 - amount.length());
                amount = amount + repeated;
            }

            if (price.length() <= 9) {
                String repeated = space.repeat(9 - price.length());
                price = price + repeated;
            }

            if (total.length() <= 8) {
                String repeated = space.repeat(8 - total.length());
                total = total + repeated;
            }

            price = price;
            String items =
                    name + amount + price + total + "║\n";

            bill = bill + items;
        }

        bill = bill + amt;
        return bill;
    }

    @Override
    public String toString() {
        return this.printBill();
    }
}
