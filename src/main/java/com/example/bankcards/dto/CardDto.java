package com.example.bankcards.dto;

import lombok.Data;

@Data
public class CardDto {
    public String cardNumber;
    public String owner;
    public String validityPeriod;
    public String status;
//    public String balance;
}
