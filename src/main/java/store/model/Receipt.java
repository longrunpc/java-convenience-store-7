package store.model;

import java.util.HashMap;
import java.util.Map;

public class Receipt {
    private final Map<String, ReceiptDetail> purchasedProducts;
    private final Map<String, ReceiptDetail> giveaway;
    public int totalQuantity;
    private int totalPrice;
    private int excludedPromotionPrice;
    public int eventDiscount;
    private int membershipDiscount;
    private int finalPrice;

    public Receipt() {
        this.purchasedProducts = new HashMap<>();
        this.giveaway = new HashMap<>();
        this.totalPrice = 0;
        this.totalQuantity = 0;
        this.eventDiscount = 0;
        this.membershipDiscount = 0;
        this.finalPrice = 0;
    }

    public void addPurchasedProduct(ReceiptDetail receiptDetail) {
        purchasedProducts.put(receiptDetail.getName(), receiptDetail);
        totalPrice += (receiptDetail.getQuantity() * receiptDetail.getPrice());
        totalQuantity += receiptDetail.getQuantity();
    }

    public void addExcludedPromotionPrice(ReceiptDetail receiptDetail) {
        excludedPromotionPrice += (receiptDetail.getQuantity()*receiptDetail.getPrice());
    }

    public void addGiveaway(ReceiptDetail receiptDetail) {
        giveaway.put(receiptDetail.getName(), receiptDetail);
        eventDiscount += (receiptDetail.getQuantity() * receiptDetail.getPrice());
    }

    public void calculateMembershipDiscount() {
        membershipDiscount = (int) (excludedPromotionPrice * 0.3); // 30% 계산
        if (membershipDiscount > 8000) {
            membershipDiscount = 8000;
        }
    }

    public void calculateFinalPrice() {
        finalPrice = totalPrice - eventDiscount - membershipDiscount;
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

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}
