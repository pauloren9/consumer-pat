package br.com.alelo.consumer.consumerpat.controller;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.dto.BuyRequest;
import br.com.alelo.consumer.consumerpat.entity.dto.ConsumerRequest;
import br.com.alelo.consumer.consumerpat.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = ConsumerController.ROUTE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ConsumerController {
    public static final String ROUTE = "/consumer";
    private final ConsumerService consumerService;

    /* Listar todos os clientes (obs.: tabela possui cerca de 50.000 registros) */
    @GetMapping(path = "/consumer-list/{page}/{size}")
    public List<Consumer> listAllConsumers(@PathVariable("page") int page, @PathVariable("size") int size) {
        return this.consumerService.listAllConsumers(page, size);
    }

    /* Cadastrar novos clientes */
    @PostMapping(path = "/create-consumer")
    public ResponseEntity<Integer> createConsumer(@RequestBody ConsumerRequest consumer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.consumerService.createConsumer(consumer));
    }

    // Atualizar cliente, lembrando que não deve ser possível alterar o saldo do cartão
    @PatchMapping(path = "/update-consumer/{id}")
    public ResponseEntity<Consumer> updateConsumer(@PathVariable("id") Integer id, @RequestBody ConsumerRequest consumer) {
        return ResponseEntity.status(HttpStatus.OK).body(this.consumerService.updateConsumer(id, consumer));
    }

    /*
     * Credito de valor no cartão
     *
     * cardNumber: número do cartão
     * value: valor a ser creditado (adicionado ao saldo)
     */
    @PostMapping(path = "/set-card-balance/{card-number}/{value}")
    public ResponseEntity<String> setBalance(@Valid @PathVariable("card-number") int cardNumber, @PathVariable("value") @Min(0) double value) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.consumerService.setBalance(cardNumber, value));

        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * Débito de valor no cartão (compra)
     *
     * establishmentType: tipo do estabelecimento comercial
     * establishmentName: nome do estabelecimento comercial
     * cardNumber: número do cartão
     * productDescription: descrição do produto
     * value: valor a ser debitado (subtraído)
     */
    @PostMapping("/buy")
    public ResponseEntity<String> buy(@Valid @RequestBody BuyRequest buyRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.consumerService.buy(
                    buyRequest.getEstablishmentType(),
                    buyRequest.getEstablishmentName(),
                    buyRequest.getCardNumber(),
                    buyRequest.getProductDescription(),
                    buyRequest.getValue()));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
