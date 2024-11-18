package store.model;

import java.util.List;

public class StoreService {
    private static final String PRODUCTS_FILE_PATH = "products.md";
    private static final String PROMOTIONS_FILE_PATH = "promotions.md";
    private Store store = new Store();
    private Receipt receipt = new Receipt();

    public StoreService() {
        initialize(); // 생성 시 초기화
    }

    public void initialize() {
        this.store = new Store();
        this.receipt = new Receipt();
    }

    public Store registrationStore() {
        store.readStore(PRODUCTS_FILE_PATH);
        store.readPromotions(PROMOTIONS_FILE_PATH);

        return store;
    }

    public void updateStore() {
        store.updateStore(PRODUCTS_FILE_PATH);
    }

    public void buyProduct(ReceiptDetail purchaseProduct) {
        List<Product> productList = store.getProduct(purchaseProduct.getName());
        purchaseProduct.setPrice(productList.get(0).getPrice());

        if (productList.size() == 1) {
            Product product = productList.get(0);
            product.sale(purchaseProduct.getQuantity());
            receipt.addPurchasedProduct(purchaseProduct);
            receipt.addExcludedPromotionPrice(purchaseProduct.getPrice() * purchaseProduct.getQuantity());
            return;
        }

        Product promotionProduct = productList.get(0);

        if (purchaseProduct.getQuantity() > promotionProduct.getQuantity()) {
            int overQuantity = overProduct(purchaseProduct);
            int promotionQuantity = promotionProduct.getQuantity() - overQuantity;
            int noPromotionProductQuantity = purchaseProduct.getQuantity() - promotionQuantity;

            receipt.addGiveaway(transformGiveaway(purchaseProduct, promotionProduct.getQuantity(), promotionProduct.getPromotion()));
            receipt.addExcludedPromotionPrice(noPromotionProductQuantity * promotionProduct.getPrice());
            store.subPromotionQuantity(purchaseProduct, promotionProduct.getQuantity());
            store.subNoPromotionQuantity(purchaseProduct, purchaseProduct.getQuantity() - promotionProduct.getQuantity());
            receipt.addPurchasedProduct(purchaseProduct);
            return;
        }

        if (overProduct(purchaseProduct) == 0) {
            store.subPromotionQuantity(purchaseProduct, purchaseProduct.getQuantity());
            receipt.addPurchasedProduct(purchaseProduct);
            receipt.addGiveaway(transformGiveaway(purchaseProduct, purchaseProduct.getQuantity(), promotionProduct.getPromotion()));
            return;
        }

        store.subPromotionQuantity(purchaseProduct, purchaseProduct.getQuantity());
        store.subNoPromotionQuantity(purchaseProduct, overProduct(purchaseProduct));
        receipt.addPurchasedProduct(purchaseProduct);
        receipt.addGiveaway(transformGiveaway(purchaseProduct, purchaseProduct.getQuantity(), promotionProduct.getPromotion()));
        receipt.addExcludedPromotionPrice(overProduct(purchaseProduct) * promotionProduct.getPrice());
    }

    public List<Product> getProductInfo(String name) {
        return store.getProduct(name);
    }

    public int getQuantity(List<Product> productList) {
        return productList.stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public int getPromotionQuantity(List<Product> productList) {
        return productList.get(0).getQuantity();
    }

    public boolean isValidPromotion(Promotion promotion) {
        return store.getPromotionDetail(promotion).isValidPeriod();
    }

    public ReceiptDetail transformGiveaway(ReceiptDetail receiptDetail, int quantity, Promotion promotion) {
        return new ReceiptDetail(
                receiptDetail.getName(),
                applyPromotion(quantity, promotion),
                receiptDetail.getPrice()
        );
    }

    public int getPromotionBuy(Promotion promotion) {
        return store.getPromotionDetail(promotion).getBuy();
    }

    public int getPromotionGet(Promotion promotion) {
        return store.getPromotionDetail(promotion).getGet();
    }

    public int additionProduct(ReceiptDetail receiptDetail) {
        List<Product> products= store.getProduct(receiptDetail.getName());
        PromotionDetail promotionDetail = store.getPromotionDetail(products.getFirst().getPromotion());
        if (promotionDetail.getBuy() == checkPromotion(receiptDetail.getQuantity(), promotionDetail.getPromotion())) {
            return promotionDetail.getGet();
        }
        return 0;
    }

    public int overProduct(ReceiptDetail receiptDetail) {
        List<Product> products= store.getProduct(receiptDetail.getName());
        PromotionDetail promotionDetail = store.getPromotionDetail(products.getFirst().getPromotion());
        int subQuantity = receiptDetail.getQuantity() - products.get(0).getQuantity();
        if (subQuantity < 0) {
            return checkPromotion(receiptDetail.getQuantity(), promotionDetail.getPromotion());
        }
        return subQuantity + checkPromotion(products.get(0).getQuantity(), promotionDetail.getPromotion());
    }

    public int applyPromotion(int quantity, Promotion promotion) {
        int buy = store.getPromotionDetail(promotion).getBuy();
        int get = store.getPromotionDetail(promotion).getGet();

        return quantity / (buy + get);
    }

    public void applyMembership() {
        receipt.calculateMembershipDiscount();
    }

    public int checkPromotion(int quantity, Promotion promotion) {
        int buy = store.getPromotionDetail(promotion).getBuy();
        int get = store.getPromotionDetail(promotion).getGet();

        return quantity % (buy + get);
    }

    public void checkProductName(List<ReceiptDetail> products) {
        for (ReceiptDetail receiptDetail : products) {
            store.getProduct(receiptDetail.getName());
        }
    }

    public Receipt getReceive() {
        receipt.calculateFinalPrice(); // Ensure final price is calculated before returning
        return receipt;
    }
}
