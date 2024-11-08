package ui.pom;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class ProductCard {

    private final SelenideElement product;

    public ProductCard(SelenideElement product) {
        this.product = product;
    }

    public void addToCart() {
        product.find(By.cssSelector(".btn_inventory")).click();
    }
}
