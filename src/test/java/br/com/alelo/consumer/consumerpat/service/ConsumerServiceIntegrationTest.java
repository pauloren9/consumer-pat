package br.com.alelo.consumer.consumerpat.service;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.dto.ConsumerRequest;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class ConsumerServiceIntegrationTest {

    @Autowired
    private ConsumerRepository consumerRepository;
    @Autowired
    private ConsumerService consumerService;

    @Test
    @DirtiesContext
    @Sql({"insertConsumer1.sql", "insertConsumer2.sql"})
    void testGetAllConsumerWithSuccessfully() throws IOException {
        List<Consumer> response = this.consumerService.listAllConsumers();
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
        assertEquals("Saldo e valores atualizados para o cartão: " + "1111" + " tipo: " + "food", response);
    }
    @Test
    @DirtiesContext
    @Sql({"insertConsumer1.sql", "insertConsumer2.sql"})
    void testSetBalanceOnDrugCardConsumerWithSuccessfully() throws IOException {
        String response = this.consumerService.setBalance(3333, 100.0);
        assertNotNull(response);
        assertEquals("Saldo e valores atualizados para o cartão: " + "3333" + " tipo: " + "drug", response);
    }

    @Test
    @DirtiesContext
    @Sql({"insertConsumer1.sql", "insertConsumer2.sql"})
    void testSetBalanceOnFuelCardConsumerWithSuccessfully() throws IOException {
        String response = this.consumerService.setBalance(2145, 100.0);
        assertNotNull(response);
        assertEquals("Saldo e valores atualizados para o cartão: " + "2145" + " tipo: " + "fuel", response);
    }

    @Test
    @DirtiesContext
    @Sql({"insertConsumer1.sql", "insertConsumer2.sql"})
    void testNotFoundCardCardConsumerWithSuccessfully() throws IOException {
        var response = assertThrows(NotFoundException.class, () -> this.consumerService.setBalance(123, 100.0));
        assertEquals("Type of card not found to number: 123", response.getMessage());
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
        consumerRequest.setFuelCardBalance(200.0);
        consumerRequest.setDrugCardNumber(3333);
        consumerRequest.setDrugCardBalance(300.0);

        this.consumerService.createConsumer(consumerRequest);
        Consumer response = this.consumerService.listAllConsumers().get(0);

        assertEquals(1, response.getId());
        assertEquals(consumerRequest.getName(), response.getName());
        assertEquals(consumerRequest.getDocumentNumber(), response.getDocumentNumber());
        assertEquals(consumerRequest.getMobilePhoneNumber(), response.getMobilePhoneNumber());
        assertEquals(consumerRequest.getEmail(), response.getEmail());
        assertEquals(consumerRequest.getFoodCardBalance(), response.getFoodCardBalance());
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
        consumerRequest.setFoodCardBalance(250.0);
        consumerRequest.setFuelCardNumber(2222);
        consumerRequest.setFuelCardBalance(200.0);
        consumerRequest.setDrugCardNumber(3333);
        consumerRequest.setDrugCardBalance(300.0);

        this.consumerService.updateConsumer(1, consumerRequest);
        Consumer response = this.consumerService.listAllConsumers().get(0);

        assertEquals(consumerRequest.getName(), response.getName());
        assertEquals(consumerRequest.getDocumentNumber(), response.getDocumentNumber());
        assertEquals(consumerRequest.getMobilePhoneNumber(), response.getMobilePhoneNumber());
        assertEquals(consumerRequest.getEmail(), response.getEmail());
        assertEquals(consumerRequest.getFoodCardBalance(), response.getFoodCardBalance());


    }
}