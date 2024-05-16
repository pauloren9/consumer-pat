package br.com.alelo.consumer.consumerpat.controller;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.dto.ConsumerRequest;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ConsumerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ConsumerRepository consumerRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateConsumer() throws Exception {
        ConsumerRequest consumerRequest = ConsumerRequest.builder()
                .name("John Doe")
                .documentNumber(123456789)
                .birthDate(new Date())
                .mobilePhoneNumber(123456789)
                .residencePhoneNumber(987654321)
                .phoneNumber(999888777)
                .email("john.doe@example.com")
                .street("Main Street")
                .number(123)
                .city("City")
                .country("Country")
                .postalCode(12345)
                .foodCardNumber(1111)
                .foodCardBalance(100.0)
                .fuelCardNumber(2222)
                .fuelCardBalance(200.0)
                .drugCardNumber(3334)
                .drugCardBalance(300.0)
                .build();


        var mvcResult = mockMvc.perform(post("/consumer/create-consumer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(consumerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        Long returnedId = Long.parseLong(responseBody);

        assertEquals(1, returnedId);
    }

    @Test
    @DirtiesContext
    @Sql("insertConsumer3.sql")
    void testListAllConsumers() throws Exception {
        MvcResult result = mockMvc.perform(get("/consumer/consumer-list/0/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<Consumer> consumers = this.objectMapper.readValue(responseBody, new TypeReference<List<Consumer>>() {
        });

        assertNotNull(consumers);
        assertEquals(1, consumers.size());

        Consumer consumer = consumers.get(0);
        assertEquals("Jhon Snow", consumer.getName());
        assertEquals(133456789, consumer.getDocumentNumber());
    }

    @Test
    @Transactional
    @DirtiesContext
    @Sql("insertConsumer3.sql")
    void testUpdateConsumer() throws Exception {
        ConsumerRequest consumerRequest = ConsumerRequest.builder()
                .name("Ferreira Renan")
                .documentNumber(7777).build();

        MvcResult result = mockMvc.perform(patch("/consumer/update-consumer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumerRequest)))
                .andExpect(status().isOk())
                .andReturn();


        String responseBody = result.getResponse().getContentAsString();
        Consumer consumer = this.objectMapper.readValue(responseBody, new TypeReference<Consumer>() {
        });

        assertEquals("Ferreira Renan", consumer.getName());
        assertEquals(7777, consumer.getDocumentNumber());
    }

}
