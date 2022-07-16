package com.philiphassialis.broker.wallet;

import com.philiphassialis.broker.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawFiatMoney(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal amount){
}
