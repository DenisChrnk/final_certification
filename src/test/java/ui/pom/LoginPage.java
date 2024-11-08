package ui.pom;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final SelenideElement login = $("#user-name");
    private final SelenideElement password = $("#password");
    private final SelenideElement loginButton = $("#login-button");

    @Step("Открыть магазин \"Swag Labs\"")
    public void openShop(){
        open("/");
    }

    @Step("Авторизоваться на \"Swag Labs\" под пользователем логин: {log} , пароль: {pass}")
    public void auth(String log, String pass) {
        login.setValue(log);
        password.setValue(pass);
        loginButton.click();
    }
}
