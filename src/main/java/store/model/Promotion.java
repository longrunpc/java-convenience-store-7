package store.model;

public enum Promotion {
    탄산("탄산2+1"),
    MD추천상품("MD추천상품"),
    반짝할인("반짝할인"),
    NONE("null");

    private final String name;

    Promotion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Promotion fromName(String name) {
        for (Promotion promotion : values()) {
            if (promotion.name.equals(name)) {
                return promotion;
            }
        }
        return NONE;
    }
}
