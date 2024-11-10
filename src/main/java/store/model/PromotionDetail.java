package store.model;

import java.time.LocalDate;

public class PromotionDetail {
    private final Promotion promotion;
    private final int buy;
    private final int get;
    private final LocalDate start_date;
    private final LocalDate end_date;

    public PromotionDetail(Promotion promotion, int buy, int get, LocalDate start_date, LocalDate end_date) {
        this.promotion = promotion;
        this.buy = buy;
        this.get = get;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public boolean isValidPeriod(){
        LocalDate today = LocalDate.now(); // 현재 날짜 가져오기
        return !today.isBefore(start_date) && !today.isAfter(end_date);
    }
}
