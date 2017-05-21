package net.andrewhatch.coin.wallet;

import com.google.common.base.Joiner;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.inject.Named;

public class Module extends AbstractModule {

  private final Config config;

  public Module(final Config config) {
    this.config = config;
  }

  @Override
  protected void configure() {}

  @Provides
  @Singleton
  public Config config() {
    return this.config;
  }

  @Provides
  @Named("wallet.words")
  public String words() {
    try {
      return Joiner.on(" ")
          .skipNulls()
          .join(Files.readAllLines(Paths.get(this.config.wordsFilePath())));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
