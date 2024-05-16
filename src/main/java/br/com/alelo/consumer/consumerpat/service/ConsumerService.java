package br.com.alelo.consumer.consumerpat.service;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.Extract;
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
import java.util.Date;
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
        Page<Consumer> consumerPage = this.consumerRepository.findAll(pageRequest);
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
        Map<Consumer, String> consumers = findConsumer(cardNumber);
        if (consumers.isEmpty()) {
            throw new NotFoundException("Card not found for number: " + cardNumber);
        }

        String cardType = consumers.values().iterator().next();
        return updateBalance(cardNumber, value, consumers, cardType);
    }

    public String buy(int establishmentType, String establishmentName, int cardNumber, String productDescription, double value) {
        if (establishmentType < 1 || establishmentType > 3) {
            throw new IllegalArgumentException("Invalid establishment type");
        }

        Map<Consumer, String> consumers = findConsumer(cardNumber);

        if (consumers.isEmpty()) {
            throw new NotFoundException("Card not found for number: " + cardNumber);
        }

        String cardType = consumers.values().iterator().next();

        if (!establishmentTypeMatchesCardType(establishmentType, cardType)) {
            throw new IllegalArgumentException("Card type does not match establishment type");
        }

        value = applyDiscountOrTax(establishmentType, value);
        updateBalance(cardNumber, -value, consumers, cardType);
        saveTransaction(cardNumber, establishmentName, productDescription, value);

        return "Purchase successful for card number: " + cardNumber;
    }

    private void saveTransaction(int cardNumber, String establishmentName, String productDescription, double value) {
        Extract extract = Extract.builder()
                .establishmentName(establishmentName)
                .productDescription(productDescription)
                .cardNumber(cardNumber)
                .dateBuy(new Date())
                .amount(value)
                .build();
        this.extractRepository.save(extract);
    }

    public boolean establishmentTypeMatchesCardType(int establishmentType, String cardType) {
        return switch (establishmentType) {
            case 1 -> cardType.equals(FOOD);
            case 2 -> cardType.equals(DRUG);
            case 3 -> cardType.equals(FUEL);
            default -> false;
        };
    }


    public double applyDiscountOrTax(int establishmentType, double value) {
        return switch (establishmentType) {
            case 1 -> value * 0.9; // Cashback de 10% para FOOD
            case 3 -> value * 1.35; //Taxa de 35% para FUEL
            default -> value;
        };
    }

    private String updateBalance(int cardNumber, double value, Map<Consumer, String> consumers, String cardType) {
        consumers.forEach((consumer, type) -> {
            switch (type) {
                case DRUG -> consumer.setDrugCardBalance(consumer.getDrugCardBalance() + value);
                case FOOD -> consumer.setFoodCardBalance(consumer.getFoodCardBalance() + value);
                case FUEL -> consumer.setFuelCardBalance(consumer.getFuelCardBalance() + value);
            }
        });

        this.consumerRepository.saveAll(consumers.keySet());
        return "Balances and values updated for card: " + cardNumber + ", type: " + cardType;
    }

    public Map<Consumer, String> findConsumer(int cardNumber) {
        Map<Consumer, String> data = new HashMap<>();

        Consumer drugsCard = this.consumerRepository.findByDrugCardNumber(cardNumber);
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
