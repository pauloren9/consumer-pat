package br.com.alelo.consumer.consumerpat.service;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.dto.ConsumerRequest;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.respository.ExtractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {
    public static final String FUEL = "fuel";
    public static final String FOOD = "food";
    public static final String DRUG = "drug";
    private final ConsumerRepository consumerRepository;
    private final ExtractRepository extractRepository;

    public Integer createConsumer(ConsumerRequest consumer) {

        Consumer consumerToSave = Consumer.builder()
                .name(consumer.getName())
                .documentNumber(consumer.getDocumentNumber())
                .birthDate(consumer.getBirthDate())
                .mobilePhoneNumber(consumer.getMobilePhoneNumber())
                .residencePhoneNumber(consumer.getResidencePhoneNumber())
                .phoneNumber(consumer.getPhoneNumber())
                .email(consumer.getEmail())
                .street(consumer.getStreet())
                .number(consumer.getNumber())
                .city(consumer.getCity())
                .country(consumer.getCountry())
                .postalCode(consumer.getPostalCode())
                .foodCardNumber(consumer.getFoodCardNumber())
                .foodCardBalance(consumer.getFoodCardBalance())
                .fuelCardNumber(consumer.getFuelCardNumber())
                .fuelCardBalance(consumer.getFuelCardBalance())
                .drugCardNumber(consumer.getDrugCardNumber())
                .drugCardBalance(consumer.getDrugCardBalance())
                .build();
        Consumer consumerSave = this.consumerRepository.save(consumerToSave);
        return consumerSave.getId();
    }

    public List<Consumer> listAllConsumers(int page, int size) {
        log.debug("Getting page {} of consumers with size {}", page, size);
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Consumer> consumerPage = consumerRepository.findAll(pageRequest);
        if (consumerPage.isEmpty()) {
            log.warn("The returned list of consumers is empty. Returning an empty list.");
            return Collections.emptyList();
        }
        return consumerPage.getContent();
    }

    @Transactional
    public Consumer updateConsumer(Integer id, ConsumerRequest consumer) {

        Optional<Consumer> optionalConsumer = this.consumerRepository.findById(id);

        if (optionalConsumer.isPresent()) {
            Consumer existingConsumer = optionalConsumer.get();

            existingConsumer.setName(consumer.getName());
            existingConsumer.setDocumentNumber(consumer.getDocumentNumber());
            existingConsumer.setBirthDate(consumer.getBirthDate());
            existingConsumer.setMobilePhoneNumber(consumer.getMobilePhoneNumber());
            existingConsumer.setResidencePhoneNumber(consumer.getResidencePhoneNumber());
            existingConsumer.setPhoneNumber(consumer.getPhoneNumber());
            existingConsumer.setEmail(consumer.getEmail());
            existingConsumer.setStreet(consumer.getStreet());
            existingConsumer.setNumber(consumer.getNumber());
            existingConsumer.setCity(consumer.getCity());
            existingConsumer.setCountry(consumer.getCountry());
            existingConsumer.setPostalCode(consumer.getPostalCode());

            return existingConsumer;
        } else {
            throw new NotFoundException("Consumer not found for id: " + id);
        }

    }

    public String setBalance(int cardNumber, double value) {
        Map<Consumer, String> consumers = this.findConsumer(cardNumber);
        return updateBalance(cardNumber, value, consumers);
    }

    private String updateBalance(int cardNumber, double value, Map<Consumer, String> consumers) {
        if (!consumers.isEmpty()) {
            String cardType = consumers.values().iterator().next();

            consumers.forEach((consumer, type) -> {
                switch (type) {
                    case DRUG:
                        consumer.setDrugCardBalance(consumer.getDrugCardBalance() + value);
                        break;
                    case FOOD:
                        consumer.setFoodCardBalance(consumer.getFoodCardBalance() + value);
                        break;
                    case FUEL:
                        consumer.setFuelCardBalance(consumer.getFuelCardBalance() + value);
                        break;
                }
            });

            this.consumerRepository.saveAll(consumers.keySet());
            return "Balances and values updated for card: " + cardNumber + ", type: " + cardType;
        } else {
            throw new NotFoundException("Type of card not found for number: " + cardNumber);
        }
    }

    public Map<Consumer, String> findConsumer(int cardNumber) {
        Map<Consumer, String> data = new HashMap<>();

        Consumer drugsCard = this.consumerRepository.findByDrugstoreNumber(cardNumber);
        if (drugsCard != null) {
            data.put(drugsCard, DRUG);
        }

        Consumer foodCard = this.consumerRepository.findByFoodCardNumber(cardNumber);
        if (foodCard != null) {
            data.put(foodCard, FOOD);
        }

        Consumer fuelCard = this.consumerRepository.findByFuelCardNumber(cardNumber);
        if (fuelCard != null) {
            data.put(fuelCard, FUEL);
        }

        return data;
    }
}
