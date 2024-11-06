package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {
    Product product;

    @BeforeEach
    void setUp(){
        product = new Product("콜라",1000,10,"탄산2+1");
    }

    @DisplayName("상품 등록 성공")
    @Test
    void productTest_Success(){
        //given

        //when&then
        assertThat(product.getName()).isEqualTo("콜라");
        assertThat(product.getPrice()).isEqualTo(1000);
        assertThat(product.getQuantity()).isEqualTo(10);
        assertThat(product.getPromotion()).isEqualTo(Promotion.탄산);
    }

    @DisplayName("상품 판매")
    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9,10})
    void sellingProductTest(int sellQuantity){
        //given

        //when
        product.sale(sellQuantity);

        //then
        assertThat(product.getQuentity()).isEqualTo(10-sellQuantity);
    }

    @DisplayName("상품 판매 수량 초과")
    @ParameterizedTest
    @ValueSource(ints = {11,12,13,14})
    void sellingProductTest(int sellQuantity){
        //given

        //when & then
        assertThat(product.checkQuantity(sellQuantity)).isFalse();
    }
}
