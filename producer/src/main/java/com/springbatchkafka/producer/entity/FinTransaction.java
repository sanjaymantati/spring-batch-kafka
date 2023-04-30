package com.springbatchkafka.producer.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "transaction_master")
@Data
@ToString
@EntityListeners({AuditingEntityListener.class})
public class FinTransaction implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sequence")
    private Long sequence;
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private Date createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private Date modifiedDate;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "type")
    private String type;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "tx_charge")
    private Double txCharge;

    @Column(name = "tx_charge_calculated_at")
    private Date txCalculatedAt;

}
