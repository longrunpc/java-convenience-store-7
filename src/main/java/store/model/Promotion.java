package store.model;

public enum Promotion {
    탄산, MD추천상품, 반짝할인, NONE;

    public static Promotion fromString(String value) {
        if (value.equals("탄산2+1")){
            return 탄산;
        }
        if (value.equals("MD추천상품")){
            return MD추천상품;
        }
        if (value.equals("반짝할인")){
            return 반짝할인;
        }
        return NONE;
    }

    public static String promotionToString(Promotion promotion) {
        if (promotion == 탄산) {
            return "탄산2+1";
        }
        if (promotion == MD추천상품) {
            return "MD추천상품";
        }
        if (promotion == 반짝할인) {
            return "반짝할인";
        }
        return "null";
    }
}
