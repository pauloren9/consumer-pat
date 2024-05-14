package br.com.alelo.consumer.consumerpat.respository;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsumerRepository extends JpaRepository<Consumer, Integer> {

    @Query(nativeQuery = true, value = "select * from Consumer")
    List<Consumer> getAllConsumersList();

    @Query(nativeQuery = true, value = "select * from Consumer where FOOD_CARD_NUMBER = :cardNumber ")
    Consumer findByFoodCardNumber(@Param("cardNumber") int cardNumber);

    @Query(nativeQuery = true, value = "select * from Consumer where FUEL_CARD_NUMBER = :cardNumber ")
    Consumer findByFuelCardNumber(@Param("cardNumber") int cardNumber);

    @Query(nativeQuery = true, value = "select * from Consumer where DRUG_CARD_NUMBER = :cardNumber ")
    Consumer findByDrugstoreNumber(@Param("cardNumber") int cardNumber);

}
