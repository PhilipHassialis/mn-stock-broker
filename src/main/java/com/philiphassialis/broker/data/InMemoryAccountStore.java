package com.philiphassialis.broker.data;

import java.math.BigDecimal;
import java.util.*;

import com.philiphassialis.broker.wallet.DepositFiatMoney;
import com.philiphassialis.broker.wallet.Wallet;
import com.philiphassialis.broker.watchlist.WatchList;

import jakarta.inject.Singleton;

@Singleton
public class InMemoryAccountStore {
  public static final UUID ACCOUNT_ID = UUID.fromString("d27fe93c-0066-49ac-9f56-4980ade9e6e3");
  private final HashMap<UUID, WatchList> watchListsPerAccount = new HashMap<>();
  private final Map<UUID, Map<UUID, Wallet>> walletsPerAccount = new HashMap<>();

  public WatchList getWatchList(final UUID accountId) {
    return watchListsPerAccount.getOrDefault(accountId, new WatchList());
  }

  public WatchList updateWatchList(final UUID accountId, final WatchList watchList) {
    watchListsPerAccount.put(accountId, watchList);
    return getWatchList(accountId);
  }
  public void deleteWatchList(final UUID accountId) {
    watchListsPerAccount.remove(accountId);
  }

  public Collection<Wallet> getWallets(final UUID accountId) {
    return Optional.ofNullable(walletsPerAccount.get(accountId)).orElse(new HashMap<>()).values();
  }


  public Wallet depositToWallet(DepositFiatMoney deposit) {
    final var wallets = Optional.ofNullable(walletsPerAccount.get(deposit.accountId()))
            .orElse(new HashMap<>());
    var oldWallet = Optional.ofNullable(wallets.get(deposit.walletId()))
            .orElse(new Wallet(ACCOUNT_ID, deposit.walletId(), deposit.symbol(), BigDecimal.ZERO, BigDecimal.ZERO));
    var newWallet = oldWallet.addAvailable(deposit.amount());
    // update wallet in memory store
    wallets.put(deposit.walletId(), newWallet);
    walletsPerAccount.put(deposit.accountId(), wallets);
    return newWallet;
  }
}
