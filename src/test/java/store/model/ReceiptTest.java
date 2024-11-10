package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReceiptTest {
    Receipt receipt;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
    }

    @DisplayName("상품 구매 테스트")
    @Test
    void addPurchasedProductTest() {
        //given
        ReceiptDetail receiptDetail1 = new ReceiptDetail("콜라", 3, 1000);
        ReceiptDetail receiptDetail2 = new ReceiptDetail("에너지바", 5, 2000);

        //when
        receipt.addPurchasedProduct(receiptDetail1);
        receipt.addPurchasedProduct(receiptDetail2);

        //then
        assertThat(receipt.getTotalPrice()).isEqualTo(3000+10000);
        assertThat(receipt.getTotalQuantity()).isEqualTo(3+5);
    }

    @DisplayName("증정품 추가 테스트")
    @Test
    void addGiveawayTest() {
        //given
        ReceiptDetail testGiveaway = new ReceiptDetail("콜라", 1, 1000);

        //when
        receipt.addGiveaway(testGiveaway);

        //then
        assertThat(receipt.getEventDiscount()).isEqualTo(1000);
    }

    @DisplayName("맴버십 할인 테스트")
    @Test
    void membershipDiscountTest() {
        //given
        ReceiptDetail receiptDetail = new ReceiptDetail("에너지바", 5, 2000);
        receipt.addExcludedPromotionPrice(receiptDetail);

        //when
        receipt.calculateMembershipDiscount();

        //then
        assertThat(receipt.getMembershipDiscount()).isEqualTo(3000);
    }

    @DisplayName("최종 구매 금액 반환 테스트")
    @Test
    void finalPriceTest(){
        //given
        ReceiptDetail receiptDetail1 = new ReceiptDetail("콜라", 3, 1000);
        ReceiptDetail receiptDetail2 = new ReceiptDetail("에너지바", 5, 2000);
        ReceiptDetail testGiveaway = new ReceiptDetail("콜라", 1, 1000);
        receipt.addPurchasedProduct(receiptDetail1);
        receipt.addPurchasedProduct(receiptDetail2);
        receipt.addGiveaway(testGiveaway);
        receipt.addExcludedPromotionPrice(receiptDetail2);
        receipt.calculateMembershipDiscount();

        //when
        receipt.calculateFinalPrice();

        //then
        assertThat(receipt.getFinalPrice()).isEqualTo(9000);

    }
}
