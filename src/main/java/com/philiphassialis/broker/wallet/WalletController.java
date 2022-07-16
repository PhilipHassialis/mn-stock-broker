package com.philiphassialis.broker.wallet;

import com.philiphassialis.broker.data.InMemoryAccountStore;
import com.philiphassialis.broker.wallet.error.CustomError;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import java.util.Collection;
import java.util.List;

import static com.philiphassialis.broker.data.InMemoryAccountStore.ACCOUNT_ID;

@Controller("/account/wallets")
public record WalletController(InMemoryAccountStore store) {
    public static final List<String> SUPPORTED_FIAT_CURRENCIES = List.of("EUR", "USD", "GBP", "CHF");

    @Get(produces = MediaType.APPLICATION_JSON)
    public Collection<Wallet> get() {
        return store.getWallets(ACCOUNT_ID);
    }

    @Post(value = "/deposit", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<CustomError> depositFiatMoney(@Body DepositFiatMoney deposit) {
        if (!SUPPORTED_FIAT_CURRENCIES.contains(deposit.symbol().value()))
            return HttpResponse.badRequest().body(new CustomError(HttpStatus.BAD_REQUEST.getCode(), "UNSUPPORTED_FIAT_CURRENCY", String.format("Supported currencies %s", SUPPORTED_FIAT_CURRENCIES)));

        return HttpResponse.ok();
    }

    @Post(value = "/withdraw", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public void withdrawFiatMoney(@Body WithdrawFiatMoney withdraw) {
    }

}
