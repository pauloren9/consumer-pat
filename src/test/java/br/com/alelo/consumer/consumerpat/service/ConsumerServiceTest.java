package br.com.alelo.consumer.consumerpat.service;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.dto.ConsumerRequest;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.respository.ExtractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.webjars.NotFoundException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsumerServiceTest {
    public static final String FOOD = "food";
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
    void testListAllConsumers() {
        ConsumerService consumerService = new ConsumerService(this.consumerRepository, this.extractRepository);

        Page<Consumer> emptyPage = new PageImpl<>(Collections.emptyList());
        when(this.consumerRepository.findAll(PageRequest.of(0, 5))).thenReturn(emptyPage);

        List<Consumer> consumers = consumerService.listAllConsumers(0, 5);

        assertTrue(consumers.isEmpty());
    }


    @Test
    void testThrowNotFoundExceptionWhenTryUpdateConsumer() {
        ConsumerService consumerService = new ConsumerService(this.consumerRepository, this.extractRepository);

        doReturn(Optional.empty()).when(this.consumerRepository).findById(anyInt());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            consumerService.updateConsumer(1, new ConsumerRequest());
        });

        assertEquals("Consumer not found for id: 1", exception.getMessage());
    }

    @Test
    void testFindConsumerWithSuccessfully(){
        ConsumerService consumerService = new ConsumerService(this.consumerRepository, this.extractRepository);

        Consumer consumerToSave = Consumer.builder()
                .id(123)
                .name("João")
                .documentNumber(112)
                .birthDate(new Date())
                .mobilePhoneNumber(123)
                .residencePhoneNumber(321)
                .phoneNumber(456)
                .email("email")
                .street("street")
                .number(123)
                .city("city")
                .country("coutry")
                .postalCode(1)
                .foodCardNumber(123)
                .foodCardBalance(345)
                .fuelCardNumber(876)
                .fuelCardBalance(654)
                .drugCardNumber(456)
                .drugCardBalance(324)
                .build();

        doReturn(consumerToSave).when(this.consumerRepository).findByFoodCardNumber(consumerToSave.getFoodCardNumber());

        Map<Consumer, String> result = consumerService.findConsumer(consumerToSave.getFoodCardNumber());

        verify(this.consumerRepository).findByDrugstoreNumber(consumerToSave.getFoodCardNumber());
        verify(this.consumerRepository).findByFoodCardNumber(consumerToSave.getFoodCardNumber());
        verify(this.consumerRepository).findByFuelCardNumber(consumerToSave.getFoodCardNumber());

        assertEquals(FOOD, result.entrySet().iterator().next().getValue());
    }

}
