package net.andrewhatch.coin.wallet.blockexplorer;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import info.blockchain.api.blockexplorer.BlockExplorer;

public class BlockExplorerModule extends AbstractModule {
  @Override
  protected void configure() {}

  @Provides
  @Singleton
  public BlockExplorer blockExplorer() {
    return new BlockExplorer();
  }
}
