package ui.pom;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class CartPage {
    private final SelenideElement checkoutButton = $("#checkout");

    @Step("Нажать на конпку \"Checkout\"")
    public void clickCheckoutButton() {
        checkoutButton.click();
    }
}
