package store.model;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static store.model.Promotion.promotionToString;

public class Store {
    private final List<Product> products;

    public Store() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
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

    private Product parseProduct(String line) {
        String[] data = line.split(",");
        String name = data[0];
        int price = Integer.parseInt(data[1]);
        int quantity = Integer.parseInt(data[2]);
        String promotion = data[3];

        return new Product(name, price, quantity, promotion);
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
                promotionToString(product.getPromotion())
        ) + "\n";
    }
}
