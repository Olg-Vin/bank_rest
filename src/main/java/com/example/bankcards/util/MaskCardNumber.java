package com.example.bankcards.util;

import org.springframework.stereotype.Component;

@Component
public class MaskCardNumber {
    public String maskNumber(String last4) {
        if (last4 == null) return null;
        if (last4.length() < 4) return null;
        return "**** **** **** " + last4;
    }
}
