package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StoreTest {
    Store store;
    private static final String TEST_FILE_PATH = "src/main/resources/products.md";

    @BeforeEach
    void setUp() {
        store = new Store();
    }

    @DisplayName("전체 상품 정보 읽어오기 성공")
    @Test
    void readStoreTest_Success() throws Exception {
        // when
        store.readStore(TEST_FILE_PATH);
        List<Product> products = store.getProducts();

        // then
        assertThat(products).isNotEmpty(); // 상품 리스트가 비어 있지 않은지 확인
        assertThat(products.size()).isEqualTo(16); // 상품 개수 확인

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
        // when & then
        assertThatThrownBy(() -> store.readStore("invalid_file_path.md"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] Failed to read store file.");
    }
}
