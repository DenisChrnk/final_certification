package ui.pom;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage {
    private final SelenideElement checkoutButton = $("#checkout");
    public final ElementsCollection cartItem = $$(".cart_item");

    @Step("Нажать на конпку \"Checkout\"")
    public void clickCheckoutButton() {
        checkoutButton.click();
    }
}