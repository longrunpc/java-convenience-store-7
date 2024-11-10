package store.model;

public class ReceiptDetail {
    private String name;
    private int quantity;
    private int price;

    public ReceiptDetail(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
