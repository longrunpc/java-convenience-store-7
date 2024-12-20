package store;

import store.controller.StoreController;
import store.model.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        StoreService storeService = new StoreService();
        StoreController storeController = new StoreController(inputView, outputView, storeService);

        storeController.run();
    }
}
