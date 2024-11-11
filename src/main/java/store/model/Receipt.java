package store.model;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private final List<ReceiptDetail> purchasedProducts;
    private final List<ReceiptDetail> giveaway;
    public int totalQuantity;
    private int totalPrice;
    private int excludedPromotionPrice;
    public int eventDiscount;
    private int membershipDiscount;
    private int finalPrice;

    public Receipt() {
        this.purchasedProducts = new ArrayList<>();
        this.giveaway = new ArrayList<>();
        this.totalPrice = 0;
        this.totalQuantity = 0;
        this.eventDiscount = 0;
        this.membershipDiscount = 0;
        this.finalPrice = 0;
    }

    public void addPurchasedProduct(ReceiptDetail receiptDetail) {
        purchasedProducts.add(receiptDetail);
        totalPrice += (receiptDetail.getQuantity() * receiptDetail.getPrice());
        totalQuantity += receiptDetail.getQuantity();
    }

    public void addExcludedPromotionPrice(ReceiptDetail receiptDetail) {
        excludedPromotionPrice += (receiptDetail.getQuantity()*receiptDetail.getPrice());
    }

    public void addGiveaway(ReceiptDetail receiptDetail) {
        giveaway.add(receiptDetail);
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

    public List<ReceiptDetail> getGiveaway() {
        return giveaway;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}
