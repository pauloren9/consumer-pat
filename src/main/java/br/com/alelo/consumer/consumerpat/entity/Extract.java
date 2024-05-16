package br.com.alelo.consumer.consumerpat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Extract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column
    int establishmentNameId;

    @Column
    String establishmentName;

    @Column
    String productDescription;

    @Column
    Date dateBuy;

    @Column
    int cardNumber;

    @Column
    double amount;
}
