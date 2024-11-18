package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.model.Receipt;
import store.model.ReceiptDetail;

import java.util.ArrayList;
import java.util.List;


public class InputView {
    public List<ReceiptDetail> readItems() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        return parseReceiptDetail(input);
    }

    public String[] parseReceipt(String input){
        return input.trim().split(",");
    }

    public List<ReceiptDetail> parseReceiptDetail(String input){
        String[] items = parseReceipt(input);
        List<ReceiptDetail> details = new ArrayList<>();
        for (String item : items) {
            validateInputFormat(item.trim());
            details.add(createReceiptDetail(item.trim()));
        }
        return details;
    }

    private ReceiptDetail createReceiptDetail(String input) {
        String cleanedInput = input.substring(1, input.length() - 1); // Remove [ and ]
        String[] parts = cleanedInput.split("-");
        String name = parts[0].trim();
        int quantity = Integer.parseInt(parts[1].trim());
        return new ReceiptDetail(name, quantity, 0);
    }

    public void validateInputFormat(String input) {
        if (!input.matches("^\\[[가-힣\\s]+-\\d+]$")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    public boolean readPromotionAddition(String productName, int quantity) {
        System.out.println("현재 " + productName + "은(는) "+ quantity + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        return checkTrueFalse(Console.readLine());
    }

    public boolean readPromotionOverQuantity(String productName, int count) {
        System.out.println("현재 " + productName + " " + count + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        return checkTrueFalse(Console.readLine());
    }

    public boolean readContinueShopping() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return checkTrueFalse(Console.readLine());
    }

    public boolean readMembershipDiscount() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return checkTrueFalse(Console.readLine());
    }

    public boolean checkTrueFalse(String input) {
        if (input.equals("Y")) {
            return true;
        }
        if (input.equals("N")) {
            return false;
        }
        throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
    }

}
