package com.equalexperts.bookstorewithcucumbertests.cucumber.steps;

import com.equalexperts.bookstorewithcucumbertests.cucumber.CucumberSpringConfiguration;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import java.nio.file.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@CucumberContextConfiguration
public class PurchaseBooksByNameStepDefinition extends CucumberSpringConfiguration {

    private ResponseEntity<String> response;
    private String requestBody;

    @Given("I have the request body from file {string}")
    public void i_have_the_request_body_from_file(String fileName) throws Exception {
        ClassPathResource resource = new ClassPathResource("data/" + fileName);
        requestBody = Files.readString(resource.getFile().toPath(), UTF_8);
    }

    @When("I POST to {string}")
    public void i_post_to(String uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        response = testRestTemplate.postForEntity(uri, entity, String.class);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int statusCode) {
        assertEquals(statusCode, response.getStatusCode().value());
    }

    @Then("the response should match the content of {string}")
    public void the_response_should_match(String fileName) throws Exception {
        ClassPathResource resource = new ClassPathResource("data/" + fileName);
        String expectedResponse = Files.readString(resource.getFile().toPath(), UTF_8);
        assert response.getBody() != null;
        assertEquals(expectedResponse.trim(), response.getBody().trim());
    }
}
