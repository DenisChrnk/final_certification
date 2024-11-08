package ui.pom;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class CheckoutPage {
    private final SelenideElement firstNameInput = $("#first-name");
    private final SelenideElement lastNameInput = $("#last-name");
    private final SelenideElement postalCodeInput = $("#postal-code");
    private final SelenideElement continueButton = $("#continue");
    private final SelenideElement finishButton = $("#finish");

    @Step("Заполнить форму с личной информацией: имя {firstName}, фамилия {lastName}, почтовый индекс {postalCode}")
    public void fillingForm(String firstName, String lastName, String postalCode) {
        firstNameInput.setValue(firstName);
        lastNameInput.setValue(lastName);
        postalCodeInput.setValue(postalCode);
    }

    @Step("Нажать \"Continue\"")
    public void clickContinue() {
        continueButton.click();
    }

    @Step("Нажать \"Finish\"")
    public void clickFinish() {
        finishButton.click();
    }
}
