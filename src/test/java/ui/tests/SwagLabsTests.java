package ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ui.extendClasses.CartPageResolver;
import ui.extendClasses.CheckoutPageResolver;
import ui.extendClasses.LoginPageResolver;
import ui.extendClasses.MainPageResolver;
import ui.pom.CartPage;
import ui.pom.CheckoutPage;
import ui.pom.LoginPage;
import ui.pom.MainPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static io.qameta.allure.Allure.step;

@ExtendWith({LoginPageResolver.class, MainPageResolver.class, CartPageResolver.class, CheckoutPageResolver.class})

public class SwagLabsTests {

    static String standardUserLogin;
    static String lockedUserLogin;
    static String glitchUserLogin;
    static String usersPassword;

    static Properties properties;

    @BeforeAll
    public static void globalSetUp() throws IOException {

        String appConfigPath = "src/test/resources/env.properties";

        properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));

        standardUserLogin = properties.getProperty("ui.standard_user_login");
        lockedUserLogin = properties.getProperty("ui.locked_user_login");
        glitchUserLogin = properties.getProperty("ui.glitch_user_login");
        usersPassword = properties.getProperty("ui.users_password");

        Configuration.baseUrl = properties.getProperty("selenide.baseURI");
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.timeout = 10000L;
        Configuration.headless = true;
    }

    private static Stream<String> getLogins() {
        return Stream.of(standardUserLogin,glitchUserLogin);
    }

    @Test
    @DisplayName("Авторизация с валидным логином и паролем")
    public void validAuth(LoginPage loginPage, MainPage mainPage) {
        String textToBe = "Logout";

        loginPage.openShop();
        loginPage.auth(standardUserLogin, usersPassword);
        mainPage.openMenu();
       step("Проверить, что есть кнопака \"Logout\"", () -> mainPage.logoutButton.shouldBe(text(textToBe)));
    }

    @Test
    @DisplayName("Авторизация заблокированным пользователем")
    public void invalidAuth(LoginPage loginPage) {
        String textToBe = "Epic sadface: Sorry, this user has been locked out.";

        loginPage.openShop();
        loginPage.auth(lockedUserLogin, usersPassword);
        step("Проверить, что на странице отображен текст " + textToBe, () -> loginPage.headerH3.shouldHave(text(textToBe)));
    }

    @ParameterizedTest
    @MethodSource("getLogins")
    @DisplayName("Полный цикл заказа")
    public void fullOrderCycle(String logins, LoginPage loginPage, MainPage mainPage, CartPage cartPage, CheckoutPage checkoutPage) {
        String textToBe = "Thank you for your order!";
        String totalPriceToBe = "Total: $58.29";

        loginPage.openShop();
        loginPage.auth(logins, usersPassword);
        mainPage.addToBasket(new ArrayList<>(List.of("Sauce Labs Backpack", "Sauce Labs Bolt T-Shirt", "Sauce Labs Onesie")));
        mainPage.openBasket();
        step("Проверить, что в корзине добавлено 3 товара", () -> cartPage.cartItem.shouldHave(size(3)));
        cartPage.clickCheckoutButton();
        checkoutPage.fillingForm("Bob", "Bob","345636");
        checkoutPage.clickContinue();
        step("Проверить, что сумма заказа равна $58.29", () -> checkoutPage.totalLabel.shouldHave(text(totalPriceToBe)));
        checkoutPage.clickFinish();
        step("Проверить, что на странице отображен текст " + textToBe, () -> checkoutPage.completeHeader.shouldHave(text(textToBe)));
    }
}