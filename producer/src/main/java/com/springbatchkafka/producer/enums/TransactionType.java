package com.springbatchkafka.producer.enums;

public enum TransactionType {
    RTGS("E1455"),
    DD("P7656"),
    NEFT("Q6756"),
    IMPS("B5677"),
    CHEQUE("V56856"),
    ATM("S5776")
    ;


    private String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
