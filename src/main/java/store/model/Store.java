package store.model;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Store {
    private final List<Product> products;
    private final Map<Promotion, PromotionDetail> promotions;

    public Store() {
        this.products = new ArrayList<>();
        this.promotions = new HashMap<>();
    }

    public List<Product> getStore() {
        return products;
    }

    public List<Product> getProduct(String name) {
        try {
            return products.stream()
                    .filter(product -> product.getName().equals(name))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }

    public PromotionDetail getPromotionDetail(Promotion promotion) {
        return promotions.get(promotion);
    }

    public void subPromotionQuantity(ReceiptDetail receiptDetail, int quantity) {
        getProduct(receiptDetail.getName()).get(0).sale(quantity);
    }

    public void subNoPromotionQuantity(ReceiptDetail receiptDetail, int quantity) {
        getProduct(receiptDetail.getName()).get(1).sale(quantity);
    }

    public void readStore(String filePath) {
        try (InputStream inputStream = getClassLoaderResource(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            Product lastProduct = null;
            String line = reader.readLine(); // 헤더 넘김
            while ((line = reader.readLine()) != null) {
                Product product = parseProduct(line);
                checkAndAddProduct(product, lastProduct);
                lastProduct = product;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] Failed to read store file.", e);
        }
    }

    public void readPromotions(String filePath) {
        try (InputStream inputStream = getClassLoaderResource(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine(); // 헤더 넘김
            while ((line = reader.readLine()) != null) {
                PromotionDetail promotionDetail = parsePromotions(line);
                promotions.put(promotionDetail.getPromotion(), promotionDetail);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] Failed to read promotion file.", e);
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
        try (OutputStream outputStream = getClassLoaderResourceForWriting(filePath);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            writer.write("name,price,quantity,promotion\n");
            for (Product product : products) {
                writer.write(formatProduct(product));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] Failed to update store file.", e);
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

    private InputStream getClassLoaderResource(String filePath) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("[ERROR] File not found: " + filePath);
        }
        return inputStream;
    }

    private OutputStream getClassLoaderResourceForWriting(String filePath) {
        try {
            File file = new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                    .getResource(filePath)).toURI());
            return new FileOutputStream(file);
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] Failed to write to file: " + filePath, e);
        }
    }

    private void checkAndAddProduct(Product product, Product lastProduct) {
        if (lastProduct == null) {
            products.add(product);
            return;
        }
        if (!(lastProduct.getName().equals(product.getName())) && !(lastProduct.getPromotion().equals(Promotion.NONE))) {
            products.add(createNullPromotionProduct(lastProduct));
            products.add(product);
            return;
        }
        products.add(product);
    }

    private Product createNullPromotionProduct(Product product) {
        return new Product(
                product.getName(),
                product.getPrice(),
                0,      // 수량을 0으로 설정
                "null"  // 프로모션을 null로 설정
        );
    }
}
