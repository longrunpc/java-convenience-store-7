package store.controller;

import store.model.*;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;

public class StoreController {
    private InputView inputView;
    private OutputView outputView;
    private StoreService storeService;

    public StoreController(InputView inputView, OutputView outputView, StoreService storeService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.storeService = storeService;
    }

    public void run(){
        Store store = storeService.registrationStore();
        outputView.printProducts(store);
        buyProduct();
        outputView.printReceipt(storeService.getReceive());
        storeService.updateStore();
        if (inputView.readContinueShopping()) {
            storeService.initialize();
            run();
        }
    }

    public void buyProduct(){
        List<ReceiptDetail> purchaseProducts = inputView.readItems();
        storeService.checkProductName(purchaseProducts);
        for (ReceiptDetail receiptDetail : purchaseProducts) {
            receiptDetail = checkPromotion(receiptDetail);
            storeService.buyProduct(receiptDetail);
        }
        checkMembership();
    }

    public ReceiptDetail checkPromotion(ReceiptDetail receiptDetail){
        List<Product> productInfo = storeService.getProductInfo(receiptDetail.getName());
        if (productInfo.size() == 1) {
            return receiptDetail;
        }
        int promotionQuantity = productInfo.get(0).getQuantity();
        int nomalQuantity = productInfo.get(1).getQuantity();
        receiptDetail = checkPromotionAddition(receiptDetail, promotionQuantity);
        receiptDetail = checkPromotionOver(receiptDetail);
        return receiptDetail;
    }

    public ReceiptDetail checkPromotionAddition(ReceiptDetail receiptDetail, int promotionQuantity) {
        int availableAddQuantity = promotionQuantity - receiptDetail.getQuantity();
        if (availableAddQuantity <= 0) {
            return receiptDetail;
        }
        receiptDetail = printPromotionAddition(receiptDetail, storeService.additionProduct(receiptDetail));
        return receiptDetail;
    }

    public ReceiptDetail checkPromotionOver(ReceiptDetail receiptDetail) {
        if (storeService.overProduct(receiptDetail) == 0) {
            return receiptDetail;
        }
        receiptDetail = printPromotionOver(receiptDetail, storeService.overProduct(receiptDetail));
        return receiptDetail;
    }

    public void checkMembership() {
        if (inputView.readMembershipDiscount()) {
           storeService.applyMembership();
        }
    }

    public ReceiptDetail printPromotionAddition(ReceiptDetail receiptDetail, int addPromotionQuantity) {
        if (addPromotionQuantity == 0) {
            return receiptDetail;
        }
        if (inputView.readPromotionAddition(receiptDetail.getName(), addPromotionQuantity)){
            receiptDetail.addQuantity(addPromotionQuantity);
            return receiptDetail;
        }
        return receiptDetail;
    }

    public ReceiptDetail printPromotionOver(ReceiptDetail receiptDetail, int overQuantity) {
        if (inputView.readPromotionOverQuantity(receiptDetail.getName(), overQuantity)) {
            return receiptDetail;
        }
        receiptDetail.addQuantity(-overQuantity);
        return receiptDetail;
    }
}

