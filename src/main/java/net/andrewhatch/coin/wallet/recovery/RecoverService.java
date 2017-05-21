package net.andrewhatch.coin.wallet.recovery;

import info.blockchain.api.APIException;
import info.blockchain.api.blockexplorer.Address;
import info.blockchain.api.blockexplorer.BlockExplorer;

import net.andrewhatch.coin.wallet.Config;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChain;
import org.bitcoinj.wallet.UnreadableWalletException;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

public class RecoverService {
  private final Config config;
  private final BlockExplorer blockExplorer;
  private final String words;

  @Inject
  public RecoverService(final Config config,
                        final BlockExplorer blockExplorer,
                        final @Named("wallet.words") String walletWords) {
    this.config = config;
    this.blockExplorer = blockExplorer;
    this.words = walletWords;
  }

  public void recover() {
    final NetworkParameters publicNetwork = NetworkParameters.fromID(NetworkParameters.ID_MAINNET);

    try {
      final DeterministicKeyChain keyChain = getKeyChain();
      findKeysWithCoinInKeyChain(keyChain, publicNetwork, 100);
    } catch (UnreadableWalletException | APIException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void findKeysWithCoinInKeyChain(final DeterministicKeyChain keyChain,
                                          final NetworkParameters network,
                                          long number) throws APIException, IOException, InterruptedException {
    final KeyChain.KeyPurpose keyPurpose = keyPurpose();

    for (int i = 0; i < config.keysToFind(); i++) {

      final DeterministicKey nextKey = keyChain.getKey(keyPurpose);
      final DumpedPrivateKey privateKey = nextKey.getPrivateKeyEncoded(network);

      final String address = nextKey.toAddress(network).toString();
      final long balance = getBalanceForAddress(address);

      final String status = balance > 0 ? "FOUND" : "EMPTY";
      if (config.outputPrivateKeys()) {
        System.out.println(String.format("%s,%s,%s,%d", status, address, privateKey, balance));
      } else {
        System.out.println(String.format("%s,%s,%d", status, address, balance));
      }

      if (!isLastKey(i)) {
        Thread.sleep(config.requestDelay());
      }
    }
  }

  private boolean isLastKey(int i) {
    return i == config.keysToFind() - 1;
  }

  private KeyChain.KeyPurpose keyPurpose() {
    switch (config.purpose()) {
      case REFUNDS:
        return KeyChain.KeyPurpose.REFUND;
      case CHANGE:
        return KeyChain.KeyPurpose.CHANGE;
      case STORAGE:
      default:
        return KeyChain.KeyPurpose.RECEIVE_FUNDS;
    }
  }

  private long getBalanceForAddress(String addressStr) throws APIException, IOException {
    final Address address = this.blockExplorer.getAddress(addressStr);
    return address.getFinalBalance();
  }

  private DeterministicKeyChain getKeyChain() throws UnreadableWalletException {
    final DeterministicSeed deterministicSeed = new DeterministicSeed(words, null, "", 0);
    return DeterministicKeyChain.builder()
        .seed(deterministicSeed)
        .build();
  }
}
