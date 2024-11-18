package store.view;

import store.model.Product;
import store.model.Receipt;
import store.model.ReceiptDetail;
import store.model.Store;

import java.text.NumberFormat;
import java.util.List;

public class OutputView {
    public void printProducts(Store store) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");

        List<Product> products = store.getStore();
        for (Product product : products) {
            System.out.println(formatProductInfo(product));
        }
    }

    private String formatProductInfo(Product product) {
        String promotion = getPromotionDisplay(product);
        String quantityInfo = getQuantityDisplay(product);

        return String.format("- %s %d원 %s %s",
                product.getName(),
                product.getPrice(),
                quantityInfo,
                promotion);
    }

    private String getPromotionDisplay(Product product) {
        if (product.getPromotion() == null || product.getPromotion().getName().equals("null")) {
            return "";
        }
        return product.getPromotion().getName();
    }

    private String getQuantityDisplay(Product product) {
        if (product.getQuantity() == 0) {
            return "재고 없음";
        }
        return product.getQuantity() + "개";
    }

    public void printReceipt(Receipt receipt) {
        // 헤더 출력
        System.out.println("==============W 편의점================");
        System.out.printf("%-10s %-5s %-10s%n", "상품명", "수량", "금액");

        // 구매 상품 내역 출력
        for (ReceiptDetail product : receipt.getPurchasedProducts()) {
            System.out.printf("%-10s %,-5d %,10d%n",
                    product.getName(),
                    product.getQuantity(),
                    product.getQuantity() * product.getPrice());
        }

        // 증정 상품 내역 출력
        System.out.println("==============증정===============");
        for (ReceiptDetail giveaway : receipt.getGiveaways()) {
            System.out.printf("%-10s %,-5d%n", giveaway.getName(), giveaway.getQuantity());
        }

        // 요약 정보 출력
        System.out.println("====================================");
        System.out.printf("%-10s %,-5d %,10d%n", "총구매액", receipt.getTotalQuantity(), receipt.getTotalPrice());
        System.out.printf("%-10s %,10d%n", "행사할인", -receipt.getEventDiscount());
        System.out.printf("%-10s %,10d%n", "멤버십할인", -receipt.getMembershipDiscount());
        System.out.printf("%-10s %,10d%n", "내실돈", receipt.getFinalPrice());
    }
}
