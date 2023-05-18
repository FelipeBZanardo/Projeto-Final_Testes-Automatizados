package tech.ada.livrosapi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        webDriver.quit();
    }

    @Test
    void clicarBotaoCadatrarDeveDirecionarParaPaginaDeCadastroSeleniumTest() {
        WebElement btnCadastrar = webDriver.findElement(By.linkText("Cadastrar Livro"));
        btnCadastrar.click();
        assertEquals("http://localhost:8080/livros/cadastrar", webDriver.getCurrentUrl());
    }

    @Test
    void cadastroDeLivroSeleniumTest() {
        WebElement btnCadastrar = webDriver.findElement(By.linkText("Cadastrar Livro"));
        btnCadastrar.click(); //entra na página de cadastro

        webDriver.findElement(By.id("titulo")).sendKeys("Título 1");
        webDriver.findElement(By.id("resumo")).sendKeys("Resumo 1");
        webDriver.findElement(By.id("sumario")).sendKeys("Sumário 1");
        webDriver.findElement(By.id("preco")).sendKeys("25,00");
        webDriver.findElement(By.id("numeroPaginas")).sendKeys("500");
        webDriver.findElement(By.id("isbn")).sendKeys("5451216565");
        webDriver.findElement(By.id("dataPublicacao")).sendKeys("02/05/2025");

        webDriver.findElement(By.xpath("/html/body/form/div[8]/button")).click(); //botão Criar

        List<WebElement> titulosCadastrados = webDriver.findElements(By
                .xpath("//tr[*]/td[1]"));
        assertFalse(titulosCadastrados.isEmpty()); //verifica se há itens cadastrados

        List<WebElement> webElements = titulosCadastrados
                .stream()
                .filter(webElement -> webElement.getText().equals("Título 1"))
                .toList();

        assertFalse(webElements.isEmpty()); //verifica se o Título 1 foi cadastrado
        assertEquals("http://localhost:8080/livros", webDriver.getCurrentUrl()); //verifica se retorna para página inicial

    }

    @Test
    void remocaoDeLivroCadastradoSeleniumTest() {
        cadastroDeLivroSeleniumTest();  //faz o cadastro para poder fazer o teste de remoção
        List<WebElement> titulosCadastrados = webDriver
                .findElements(By.xpath("//tr[*]/td[1]"));

        webDriver.findElement(By
                .xpath("//tr[1]/td[9]/form/button")).click(); //remove o primeiro item cadastrado
        List<WebElement> titulosCadastradosPosRemocao = webDriver
                .findElements(By.xpath("//tr[*]/td[1]"));

        assertEquals(titulosCadastrados.size() - 1, titulosCadastradosPosRemocao.size()); //verifica se um item foi removido
    }

    @Test
    void clicarBotaoEditarDeveDirecionarParaPaginaDeEdicaoSeleniumTest() {
        cadastroDeLivroSeleniumTest();  //faz o cadastro para poder fazer o teste de edição
        webDriver.findElement(By
                .xpath("//tr[1]/td[8]/form/button")).click(); //clica no botão Editar

        assertTrue(webDriver.getCurrentUrl().contains("http://localhost:8080/livros/editar/")); //verifica se direciona para página de edição
    }

    @Test
    void editarLivroCadastradoSeleniumTest() {
        clicarBotaoEditarDeveDirecionarParaPaginaDeEdicaoSeleniumTest(); //faz o cadastro e testa se está na página de edição
        webDriver.findElement(By.id("dataPublicacao")).sendKeys("05/05/2050");   //atualiza a data de publicacao
        webDriver.findElement(By.xpath("/html/body/form/div[8]/button")).click(); //botão Editar

        assertEquals("http://localhost:8080/livros", webDriver.getCurrentUrl()); //verifica se retorna para página inicial

        List<WebElement> datasCadastradas = webDriver
                .findElements(By.xpath("//tr[3]/td[7]"));

        List<WebElement> webElements = datasCadastradas
                .stream()
                .filter(webElement -> webElement.getText().equals("05/05/2050"))
                .toList();

        assertTrue(webElements.isEmpty()); //verifica se a data de publicação foi modificada

    }
}
