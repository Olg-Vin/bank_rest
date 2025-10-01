package com.example.bankcards.dto;

import lombok.Data;

@Data
public class TransactionDto {
    public String fromCard;
    public String toCard;
    public String amount;
    public String status;
}
