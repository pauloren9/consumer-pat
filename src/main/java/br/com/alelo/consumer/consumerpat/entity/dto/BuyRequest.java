package br.com.alelo.consumer.consumerpat.entity.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class BuyRequest {
    private int establishmentType;
    private String establishmentName;
    private int cardNumber;
    private String productDescription;
    @Min(0)
    private double value;
}