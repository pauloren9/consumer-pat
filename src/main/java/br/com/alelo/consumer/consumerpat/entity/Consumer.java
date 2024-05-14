package br.com.alelo.consumer.consumerpat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consumer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotBlank
    String name;
    @NotNull
//    @CPF
    int documentNumber;
    @NotNull
    Date birthDate;

    //contacts
    @NotNull
    int mobilePhoneNumber;
    @NotNull
    int residencePhoneNumber;
    int phoneNumber;
    //    @Email
    String email;

    //Address
    @NotBlank
    String street;
    @NotNull
    int number;
    @NotBlank
    String city;
    @NotBlank
    String country;
    @NotNull
    int postalCode;

    //cards
    @NotNull
    int foodCardNumber;

    double foodCardBalance;

    @NotNull
    int fuelCardNumber;
    double fuelCardBalance;

    @NotNull
    int drugCardNumber;
    double drugCardBalance;

}


