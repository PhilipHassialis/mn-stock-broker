package com.philiphassialis.broker.wallet;

import com.philiphassialis.broker.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record Wallet(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal available,
        BigDecimal locked
) {
}
