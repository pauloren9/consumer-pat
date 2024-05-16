package br.com.alelo.consumer.consumerpat.service;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.Extract;
import br.com.alelo.consumer.consumerpat.entity.dto.ConsumerRequest;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.respository.ExtractRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ConsumerServiceIntegrationTest {

    public static final String FOOD = "food";
    public static final String DRUG = "drug";
    public static final String FUEL = "fuel";
    @Autowired
    private ConsumerRepository consumerRepository;
    @Autowired
    private ExtractRepository extractRepository;
    @Autowired
    private ConsumerService consumerService;

    @Test
    @DirtiesContext
    @Sql({"insertConsumer1.sql", "insertConsumer2.sql"})
    void testGetAllConsumerWithSuccessfully() throws IOException {
        List<Consumer> response = this.consumerService.listAllConsumers(0, 5);
        assertNotNull(response);
        assertEquals(1, response.get(0).getId());
        assertEquals("Jhon Snow", response.get(0).getName());
        assertEquals(2, response.get(1).getId());
        assertEquals("Maria Joaquina", response.get(1).getName());
    }

    @Test
    @DirtiesContext
    @Sql({"insertConsumer1.sql", "insertConsumer2.sql"})
    void testSetBalanceOnFoodCardConsumerWithSuccessfully() throws IOException {
        String response = this.consumerService.setBalance(1111, 100.0);
        assertNotNull(response);
        assertEquals("Balances and values updated for card: " + "1111" + ", type: " + FOOD, response);
    }

    @Test
    @DirtiesContext
    @Sql({"insertConsumer1.sql", "insertConsumer2.sql"})
    void testSetBalanceOnDrugCardConsumerWithSuccessfully() throws IOException {
        String response = this.consumerService.setBalance(3333, 100.0);
        assertNotNull(response);
        assertEquals("Balances and values updated for card: " + "3333" + ", type: " + DRUG, response);
    }

    @Test
    @DirtiesContext
    @Sql({"insertConsumer1.sql", "insertConsumer2.sql"})
    void testSetBalanceOnFuelCardConsumerWithSuccessfully() throws IOException {
        String response = this.consumerService.setBalance(2145, 100.0);
        assertNotNull(response);
        assertEquals("Balances and values updated for card: " + "2145" + ", type: " + FUEL, response);
    }

    @Test
    @DirtiesContext
    @Sql({"insertConsumer1.sql", "insertConsumer2.sql"})
    void testNotFoundCardConsumerWithSuccessfully() throws IOException {
        var response = assertThrows(NotFoundException.class, () -> this.consumerService.setBalance(123, 100.0));
        assertEquals("Card not found for number: 123", response.getMessage());
    }


    @Test
    @DirtiesContext
    void testSaveConsumerWithSuccessfully() {
        ConsumerRequest consumerRequest = new ConsumerRequest();
        consumerRequest.setName("John Snow");
        consumerRequest.setDocumentNumber(5478);
        consumerRequest.setBirthDate(new Date());
        consumerRequest.setMobilePhoneNumber(55555);
        consumerRequest.setResidencePhoneNumber(999888777);
        consumerRequest.setPhoneNumber(999888777);
        consumerRequest.setEmail("john.doe@example.com");
        consumerRequest.setStreet("Main Street");
        consumerRequest.setNumber(123);
        consumerRequest.setCity("City");
        consumerRequest.setCountry("Country");
        consumerRequest.setPostalCode(12345);
        consumerRequest.setFoodCardNumber(1111);
        consumerRequest.setFoodCardBalance(250.0);
        consumerRequest.setFuelCardNumber(2222);

        this.consumerService.createConsumer(consumerRequest);
        Consumer response = this.consumerService.listAllConsumers(0, 5).get(0);

        assertEquals(1, response.getId());
        assertEquals(consumerRequest.getName(), response.getName());
        assertEquals(consumerRequest.getDocumentNumber(), response.getDocumentNumber());
        assertEquals(consumerRequest.getMobilePhoneNumber(), response.getMobilePhoneNumber());
        assertEquals(consumerRequest.getEmail(), response.getEmail());
        assertEquals(consumerRequest.getDrugCardNumber(), response.getDrugCardNumber());
    }

    @Test
    @DirtiesContext
    @Sql("insertConsumer1.sql")
    void testUpdateConsumerWithSuccessfully() {
        ConsumerRequest consumerRequest = new ConsumerRequest();
        consumerRequest.setName("John Snow");
        consumerRequest.setDocumentNumber(5478);
        consumerRequest.setBirthDate(new Date());
        consumerRequest.setMobilePhoneNumber(55555);
        consumerRequest.setResidencePhoneNumber(999888777);
        consumerRequest.setPhoneNumber(999888777);
        consumerRequest.setEmail("john.doe@example.com");
        consumerRequest.setStreet("Main Street");
        consumerRequest.setNumber(123);
        consumerRequest.setCity("City");
        consumerRequest.setCountry("Country");
        consumerRequest.setPostalCode(12345);
        consumerRequest.setFoodCardNumber(1111);
        consumerRequest.setFuelCardNumber(2222);
        consumerRequest.setDrugCardNumber(3333);


        this.consumerService.updateConsumer(1, consumerRequest);
        Consumer response = this.consumerService.listAllConsumers(0, 5).get(0);

        assertEquals(consumerRequest.getName(), response.getName());
        assertEquals(consumerRequest.getDocumentNumber(), response.getDocumentNumber());
        assertEquals(consumerRequest.getMobilePhoneNumber(), response.getMobilePhoneNumber());
        assertEquals(consumerRequest.getEmail(), response.getEmail());

    }

    @Test
    @DirtiesContext
    @Sql("insertConsumer1.sql")
    void testBuySuccessFoodEstablishment() {
        int establishmentType = 1;
        String establishmentName = "Supermarket";
        int cardNumber = 77777;
        String productDescription = "Groceries";
        double value = 100.0;

        Consumer consumer = new Consumer();
        consumer.setFoodCardNumber(cardNumber);
        consumer.setFoodCardBalance(500.0);

        Map<Consumer, String> consumers = new HashMap<>();
        consumers.put(consumer, "FOOD");

        String result = consumerService.buy(establishmentType, establishmentName, cardNumber, productDescription, value);

        List<Extract> extractSave = this.extractRepository.findAll();

        assertEquals("Purchase successful for card number: " + cardNumber, result);
        assertNotNull(extractSave);
        assertEquals(establishmentName, extractSave.get(0).getEstablishmentName());
        assertEquals(value - ((double) 100 / 10), extractSave.get(0).getAmount());
        assertEquals(cardNumber, extractSave.get(0).getCardNumber());
        assertEquals(productDescription, extractSave.get(0).getProductDescription());
    }

}