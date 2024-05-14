package br.com.alelo.consumer.consumerpat.service;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.dto.ConsumerRequest;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.respository.ExtractRepository;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConsumerServiceTest {
    @Mock
    private ConsumerRepository consumerRepository;
    @Mock
    private ExtractRepository extractRepository;

    @Test
    void saveConsumerWithSuccessfuly() {
        ConsumerService consumerService = new ConsumerService(this.consumerRepository, this.extractRepository);
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
                .drugCardNumber(3333)
                .drugCardBalance(300.0)
                .build();

        Consumer consumerToSave = Consumer.builder()
                .id(123)
                .name(consumerRequest.getName())
                .documentNumber(consumerRequest.getDocumentNumber())
                .birthDate(consumerRequest.getBirthDate())
                .mobilePhoneNumber(consumerRequest.getMobilePhoneNumber())
                .residencePhoneNumber(consumerRequest.getResidencePhoneNumber())
                .phoneNumber(consumerRequest.getPhoneNumber())
                .email(consumerRequest.getEmail())
                .street(consumerRequest.getStreet())
                .number(consumerRequest.getNumber())
                .city(consumerRequest.getCity())
                .country(consumerRequest.getCountry())
                .postalCode(consumerRequest.getPostalCode())
                .foodCardNumber(consumerRequest.getFoodCardNumber())
                .foodCardBalance(consumerRequest.getFoodCardBalance())
                .fuelCardNumber(consumerRequest.getFuelCardNumber())
                .fuelCardBalance(consumerRequest.getFuelCardBalance())
                .drugCardNumber(consumerRequest.getDrugCardNumber())
                .drugCardBalance(consumerRequest.getDrugCardBalance())
                .build();

        doReturn(consumerToSave).when(this.consumerRepository).save(any(Consumer.class));

        Integer result = consumerService.createConsumer(consumerRequest);

        verify(this.consumerRepository).save(any(Consumer.class));
        assertEquals(consumerToSave.getId(), result);
    }

    @Test
    void testListAllConsumersSuccess() {
        ConsumerService consumerService = new ConsumerService(this.consumerRepository, this.extractRepository);

        List<Consumer> mockConsumers = new ArrayList<>();
        mockConsumers.add(new Consumer());
        mockConsumers.add(new Consumer());
        doReturn(mockConsumers).when(this.consumerRepository).findAll();

        List<Consumer> result = consumerService.listAllConsumers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void testListAllConsumersReturnsNull() {
        ConsumerService consumerService = new ConsumerService(this.consumerRepository, this.extractRepository);

        doReturn(null).when(this.consumerRepository).findAll();

        List<Consumer> result = consumerService.listAllConsumers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListAllConsumersException() {
        ConsumerService consumerService = new ConsumerService(this.consumerRepository, this.extractRepository);

        doThrow(new RuntimeException()).when(this.consumerRepository).findAll();

        var result = assertThrows(ServiceException.class, () -> consumerService.listAllConsumers());
        assertEquals("Erro ao buscar consumidores", result.getMessage());
    }

}
