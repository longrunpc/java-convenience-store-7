package store.model;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StoreTest {
    Store store;
    private static final String PRODUCTS_FILE_PATH = "products.md";
    private static final String PROMOTIONS_FILE_PATH = "promotions.md";

    @BeforeEach
    void setUp() {
        store = new Store();
    }

    @DisplayName("전체 상품 정보 읽어오기 성공")
    @Test
    void readStoreTest_Success() throws Exception {
        // when
        store.readStore(PRODUCTS_FILE_PATH);
        List<Product> products = store.getStore();

        // then
        assertThat(products).isNotEmpty(); // 상품 리스트가 비어 있지 않은지 확인
        assertThat(products).hasSize(18);

        // 첫 번째 상품 데이터 확인
        Product firstProduct = products.get(0);
        assertThat(firstProduct.getName()).isEqualTo("콜라");
        assertThat(firstProduct.getPrice()).isEqualTo(1000);
        assertThat(firstProduct.getQuantity()).isEqualTo(10);
        assertThat(firstProduct.getPromotion()).isEqualTo(Promotion.탄산);
    }

    @DisplayName("전체 상품 읽어오기 실패")
    @Test
    void readStoreTest_Fail() {
        // given
        String invalidPath = "invalid_products.md";

        // when & then
        assertThatThrownBy(() -> store.readStore(invalidPath))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 상품 업데이트 테스트")
    @Nested
    class updateStoreTest {
        @DisplayName("업데이트 테스트 성공")
        @Test
        void updateStoreTest_Success() throws Exception {
            // given
            store.readStore(PRODUCTS_FILE_PATH);
            Product product = store.getStore().get(0);
            product.sale(3); // 콜라 수량을 10 -> 7로 감소

            // when
            store.updateStore(PRODUCTS_FILE_PATH);

            // then
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClassLoaderResource(PRODUCTS_FILE_PATH)));
            List<String> updatedLines = reader.lines().toList();
            assertThat(updatedLines).hasSize(19);
            assertThat(updatedLines.get(0)).isEqualTo("name,price,quantity,promotion");
            assertThat(updatedLines.get(1)).isEqualTo("콜라,1000,7,탄산2+1");
            assertThat(updatedLines.get(2)).isEqualTo("콜라,1000,10,null");
        }

        @DisplayName("업데이트 실패 테스트")
        @Test
        void updateStoreTest_Fail() {
            // given
            String invalidPath = "invalid_products.md";

            // when & then
            assertThatThrownBy(() -> store.updateStore(invalidPath))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("프로모션 읽어오기 성공")
    @Test
    void readPromotion_Success() {
        // when
        store.readPromotions(PROMOTIONS_FILE_PATH);
        PromotionDetail promotionDetail = store.getPromotionDetail(Promotion.탄산);

        // then
        assertThat(promotionDetail.getBuy()).isEqualTo(2);
        assertThat(promotionDetail.getGet()).isEqualTo(1);
    }

    @DisplayName("프로모션 읽어오기 실패")
    @Test
    void readPromotion_Failure() {
        // given
        String invalidPath = "invalid_promotions.md";

        // when & then
        assertThatThrownBy(() -> store.readPromotions(invalidPath))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("프로모션 유효성 검증")
    @Test
    void promotionValidTest_Success() {
        // given
        store.readPromotions(PROMOTIONS_FILE_PATH);

        // when
        PromotionDetail promotionDetail = store.getPromotionDetail(Promotion.탄산);

        // then
        assertThat(promotionDetail.getBuy()).isEqualTo(2);
        assertThat(promotionDetail.getGet()).isEqualTo(1);
    }

    private InputStream getClassLoaderResource(String filePath) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("[ERROR] File not found: " + filePath);
        }
        return inputStream;
    }
}
