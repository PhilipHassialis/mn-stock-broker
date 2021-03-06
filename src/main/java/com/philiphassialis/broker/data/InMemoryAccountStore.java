package com.philiphassialis.broker.data;

import com.philiphassialis.broker.Symbol;
import com.philiphassialis.broker.wallet.DepositFiatMoney;
import com.philiphassialis.broker.wallet.Wallet;
import com.philiphassialis.broker.wallet.WithdrawFiatMoney;
import com.philiphassialis.broker.watchlist.WatchList;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.*;

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
    return addAvailableToWallet(deposit.accountId(), deposit.walletId(), deposit.symbol(), deposit.amount());
  }
  
  public Wallet withdrawFromWallet(WithdrawFiatMoney withdraw) {
    return addAvailableToWallet(withdraw.accountId(), withdraw.walletId(), withdraw.symbol(), withdraw.amount());
  }
  
  private Wallet addAvailableToWallet(UUID accountId, UUID walletId, Symbol symbol, BigDecimal changeAmount) {
    final var wallets = Optional.ofNullable(walletsPerAccount.get(accountId))
      .orElse(new HashMap<>());
    var oldWallet = Optional.ofNullable(wallets.get(walletId))
      .orElse(new Wallet(ACCOUNT_ID, walletId, symbol, BigDecimal.ZERO, BigDecimal.ZERO));
    var newWallet = oldWallet.addAvailable(changeAmount);
    // update wallet in memory store
    wallets.put(walletId, newWallet);
    walletsPerAccount.put(accountId, wallets);
    return newWallet;
  }
}
