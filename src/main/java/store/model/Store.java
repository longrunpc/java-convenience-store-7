package store.model;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store {
    private final List<Product> products;
    private final Map<Promotion, PromotionDetail> promotions;

    public Store() {
        this.products = new ArrayList<>();
        this.promotions = new HashMap<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public PromotionDetail getPromotion(Promotion promotion) {
        return promotions.get(promotion);
    }

    public void readStore(String filePath) {
        Path path = Paths.get(filePath);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine(); // 헤더 넘김
            while ((line = reader.readLine()) != null) {
                Product product = parseProduct(line);
                products.add(product);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] Failed to read store file.");
        }
    }

    public void readPromotions(String filePath) {
        Path path = Paths.get(filePath);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine(); // 헤더 넘김
            while ((line = reader.readLine()) != null) {
                PromotionDetail promotionDetail = parsePromotions(line);
                promotions.put(promotionDetail.getPromotion(), promotionDetail);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] Failed to read promotion file.");
        }
    }

    private Product parseProduct(String line) {
        String[] data = line.split(",");
        String name = data[0];
        int price = Integer.parseInt(data[1]);
        int quantity = Integer.parseInt(data[2]);
        String promotion = data[3];

        return new Product(name, price, quantity, promotion);
    }

    private PromotionDetail parsePromotions(String line) {
        String[] data = line.split(",");
        Promotion promotion = Promotion.fromName(data[0]);
        int buy = Integer.parseInt(data[1]);
        int get = Integer.parseInt(data[2]);
        LocalDate startDate = LocalDate.parse(data[3]);
        LocalDate endData = LocalDate.parse(data[4]);

        return new PromotionDetail(promotion, buy, get, startDate, endData);
    }

    public void updateStore(String filePath) {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("name,price,quantity,promotion\n");
            for (Product product : products) {
                writer.write(formatProduct(product));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] Failed to update store file.");
        }
    }

    private String formatProduct(Product product) {
        return String.join(",",
                product.getName(),
                String.valueOf(product.getPrice()),
                String.valueOf(product.getQuantity()),
                product.getPromotion().getName()
        ) + "\n";
    }
}
