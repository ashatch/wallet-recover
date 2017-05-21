package net.andrewhatch.coin.wallet;

import com.google.inject.Guice;

import net.andrewhatch.coin.wallet.blockexplorer.BlockExplorerModule;
import net.andrewhatch.coin.wallet.exceptions.InsufficientArgumentsException;
import net.andrewhatch.coin.wallet.recovery.RecoverService;

public class Recover {
  public static void main(String[] args) {
    try {
      final Config config = new Config(args);
      Guice.createInjector(
          new BlockExplorerModule(),
          new Module(config)
      ).getInstance(RecoverService.class).recover();
    } catch (InsufficientArgumentsException e) {}
  }
}
