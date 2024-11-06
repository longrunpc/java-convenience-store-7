package store.model;

public class Product {
    private final String name;
    private int price;
    private int quantity;
    private Promotion promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = Promotion.fromString(promotion);
    }

    public String getName() {
        return name;
    }

    public int getPrice(){
        return price;
    }

    public int getQuantity(){
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void sale(int quantity) {
        this.quantity -= quantity;
    }

    public boolean checkQuantity(int quantity) {
        return this.quantity >= quantity;
    }
}
