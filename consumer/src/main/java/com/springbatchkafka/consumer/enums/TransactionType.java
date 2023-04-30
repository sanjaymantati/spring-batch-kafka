package com.springbatchkafka.consumer.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionType {
    RTGS("E1455", 1.15f),
    DD("P7656", 1.05f),
    NEFT("Q6756", 1.21f),
    B5677("B5677", 1.3f),
    CHEQUE("V56856", 1.25f),
    ATM("S5776", 1.03f)
    ;


    private String value;
    private Float percentage ;


    public Float getPercentage() {
        return percentage;
    }

    public String getValue() {
        return value;
    }
}
