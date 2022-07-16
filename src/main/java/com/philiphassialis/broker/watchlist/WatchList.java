package com.philiphassialis.broker.watchlist;

import java.util.ArrayList;
import java.util.List;

import com.philiphassialis.broker.Symbol;

public record WatchList(List<Symbol> symbols) {

  public WatchList() {
    this(new ArrayList<>());
  }
}
