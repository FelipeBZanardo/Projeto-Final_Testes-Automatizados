package tech.ada.livrosapi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeleniumTest {
    private static WebDriver webDriver;
    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(chromeOptions);
        webDriver.get("http://localhost:8080/livros");
    }

    @AfterEach
    void tearDown() {
        //webDriver.quit();
    }

    @Test
    void clicarBotaoCadatrarDeveRedirecionarParaPaginaDeCadastroTest() {
        WebElement btnCadastrar = webDriver.findElement(By.xpath("/html/body/div[1]/nav/div/ul/li[2]/a"));
        btnCadastrar.click();
        assertEquals("http://localhost:8080/livros/cadastrar", webDriver.getCurrentUrl());
    }
}
