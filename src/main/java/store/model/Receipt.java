package store.model;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private final List<ReceiptDetail> purchasedProducts;
    private final List<ReceiptDetail> giveaways; // 이름 변경: 복수형으로 표현
    private int totalQuantity;
    private int totalPrice;
    private int excludedPromotionPrice;
    private int eventDiscount;
    private int membershipDiscount;
    private int finalPrice;

    public Receipt() {
        this.purchasedProducts = new ArrayList<>();
        this.giveaways = new ArrayList<>();
        this.totalPrice = 0;
        this.totalQuantity = 0;
        this.eventDiscount = 0;
        this.membershipDiscount = 0;
        this.finalPrice = 0;
        this.excludedPromotionPrice = 0;
    }

    public void addPurchasedProduct(ReceiptDetail receiptDetail) {
        purchasedProducts.add(receiptDetail);
        totalPrice += receiptDetail.getQuantity() * receiptDetail.getPrice();
        totalQuantity += receiptDetail.getQuantity();
    }

    public void addExcludedPromotionPrice(int price) {
        excludedPromotionPrice += price;
    }

    public void addGiveaway(ReceiptDetail receiptDetail) {
        giveaways.add(receiptDetail);
        eventDiscount += receiptDetail.getQuantity() * receiptDetail.getPrice();
    }

    public void calculateMembershipDiscount() {
        membershipDiscount = (int) (excludedPromotionPrice * 0.3);
        membershipDiscount = Math.min(membershipDiscount, 8000);
    }

    public void calculateFinalPrice() {
        finalPrice = totalPrice - eventDiscount - membershipDiscount;
        if (finalPrice < 0) {
            finalPrice = 0;
        }
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getEventDiscount() {
        return eventDiscount;
    }

    public List<ReceiptDetail> getPurchasedProducts() {
        return new ArrayList<>(purchasedProducts);
    }

    public List<ReceiptDetail> getGiveaways() {
        return new ArrayList<>(giveaways);
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}
