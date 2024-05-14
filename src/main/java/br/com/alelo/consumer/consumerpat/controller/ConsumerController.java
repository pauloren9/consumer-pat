package br.com.alelo.consumer.consumerpat.controller;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.Extract;
import br.com.alelo.consumer.consumerpat.entity.dto.ConsumerRequest;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.respository.ExtractRepository;
import br.com.alelo.consumer.consumerpat.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ConsumerController.ROUTE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ConsumerController {
    public static final String ROUTE = "/consumer";
    private final ConsumerService consumerService;


    /* Listar todos os clientes (obs.: tabela possui cerca de 50.000 registros) */
    @GetMapping(path = "/consumerList")
    public List<Consumer> listAllConsumers() {
        return this.consumerService.listAllConsumers();
    }

    /* Cadastrar novos clientes */
    @PostMapping(path = "/createConsumer")
    public ResponseEntity<Integer> createConsumer(@RequestBody ConsumerRequest consumer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.consumerService.createConsumer(consumer));
    }

    // Atualizar cliente, lembrando que não deve ser possível alterar o saldo do cartão
    @PatchMapping(path = "/updateConsumer")
    public ResponseEntity<Consumer> updateConsumer(@PathVariable Integer id, @RequestBody ConsumerRequest consumer) {
        return ResponseEntity.status(HttpStatus.OK).body(this.consumerService.updateConsumer(id, consumer));
    }

    /*
     * Credito de valor no cartão
     *
     * cardNumber: número do cartão
     * value: valor a ser creditado (adicionado ao saldo)
     */
//    @RequestMapping(value = "/setcardbalance", method = RequestMethod.GET)
//    public ResponseEntity<String>  setBalance(int cardNumber, double value) {
////        return ResponseEntity.status(HttpStatus.OK).body(this.consumerService.setBalance(cardNumber, value));
//    }
//
//    /*
//     * Débito de valor no cartão (compra)
//     *
//     * establishmentType: tipo do estabelecimento comercial
//     * establishmentName: nome do estabelecimento comercial
//     * cardNumber: número do cartão
//     * productDescription: descrição do produto
//     * value: valor a ser debitado (subtraído)
//     */
//    @ResponseBody
//    @RequestMapping(value = "/buy", method = RequestMethod.GET)
//    public void buy(int establishmentType, String establishmentName, int cardNumber, String productDescription, double value) {
//        Consumer consumer = null;
//        /* O valor só podem ser debitado do catão com o tipo correspondente ao tipo do estabelecimento da compra.
//
//        *  Exemplo: Se a compra é em um estabelecimeto de Alimentação (food) então o valor só pode ser debitado do cartão alimentação
//        *
//        * Tipos dos estabelcimentos:
//        *    1) Alimentação (Food)
//        *    2) Farmácia (DrugStore)
//        *    3) Posto de combustivel (Fuel)
//        */
//
//        if (establishmentType == 1) {
//            // Para compras no cartão de alimentação o cliente recebe um desconto de 10%
//            Double cashback  = (value / 100) * 10;
//            value = value - cashback;
//
//            consumer = repository.findByFoodCardNumber(cardNumber);
//            consumer.setFoodCardBalance(consumer.getFoodCardBalance() - value);
//            repository.save(consumer);
//
//        }else if(establishmentType == 2) {
//            consumer = repository.findByDrugstoreNumber(cardNumber);
//            consumer.setDrugstoreCardBalance(consumer.getDrugstoreCardBalance() - value);
//            repository.save(consumer);
//
//        } else {
//            // Nas compras com o cartão de combustivel existe um acrescimo de 35%;
//            Double tax  = (value / 100) * 35;
//            value = value + tax;
//
//            consumer = repository.findByFuelCardNumber(cardNumber);
//            consumer.setFuelCardBalance(consumer.getFuelCardBalance() - value);
//            repository.save(consumer);
//        }
//
//        Extract extract = new Extract(establishmentName, productDescription, new Date(), cardNumber, value);
//        extractRepository.save(extract);
//    }

}
