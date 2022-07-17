package com.philiphassialis.broker.wallet.error;

import com.philiphassialis.broker.api.RestApiResponse;

public record CustomError (
        int status,
        String error,
        String message
) implements RestApiResponse {
}
