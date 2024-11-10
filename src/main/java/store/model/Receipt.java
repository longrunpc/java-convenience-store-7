package store.model;

import java.util.HashMap;
import java.util.Map;

public class Receipt {
    private final Map<String, ReceiptDetail> purchasedProducts;
    private final Map<String, ReceiptDetail> giveaway;
    public int totalQuantity;
    private int totalPrice;
    public int eventDiscount;
    private int membershipDiscount;
    private int finalAmount;

    public Receipt() {
        this.purchasedProducts = new HashMap<>();
        this.giveaway = new HashMap<>();
        this.totalPrice = 0;
        this.totalQuantity = 0;
        this.eventDiscount = 0;
        this.membershipDiscount = 0;
        this.finalAmount = 0;
    }

    public void addPurchasedProduct(ReceiptDetail receiptDetail) {
        purchasedProducts.put(receiptDetail.getName(), receiptDetail);
        totalPrice += (receiptDetail.getQuantity()*receiptDetail.getPrice());
        totalQuantity += receiptDetail.getQuantity();
    }

    public void addGiveaway(ReceiptDetail receiptDetail) {
        giveaway.put(receiptDetail.getName(), receiptDetail);
        eventDiscount += (receiptDetail.getQuantity()*receiptDetail.getPrice());
    }

    public int getTotalQuantity(){
        return totalQuantity;
    }

    public int getTotalPrice(){
        return totalPrice;
    }

    public int getEventDiscount(){
        return eventDiscount;
    }

    public ReceiptDetail getGiveaway(String productName) {
        return giveaway.get(productName);
    }
}
