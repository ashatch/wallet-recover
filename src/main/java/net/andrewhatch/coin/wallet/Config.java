package net.andrewhatch.coin.wallet;

import net.andrewhatch.coin.wallet.exceptions.InsufficientArgumentsException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Config {
  private static final int WORDS_FILE_ARG_INDEX = 0;
  private static final int EXPECTED_ARGUMENTS = 1;
  private static final String DEFAULT_REQUESTS_PER_MINUTE = "30";
  private static final String DEFAULT_KEYS_TO_FIND = "100";

  private static final String OPTION_REQUESTS_PER_MINUTE = "requestsPerMinute";
  private static final String OPTION_OUTPUT_PRIVATE_KEYS = "privateKeys";
  private static final String OPTION_STORAGE_PURPOSE = "forStorage (default kind)";
  private static final String OPTION_REFUND_PURPOSE = "forRefunds";
  private static final String OPTION_CHANGE_PURPOSE = "forChange";
  private static final String OPTION_KEYS_TO_FIND = "keys";

  private final CommandLine commandLine;
  private final Options options;

  public Config(final String[] args) {
    try {
      this.options = makeOptions();
      final CommandLineParser parser = new DefaultParser();
      this.commandLine = parser.parse(this.options, args);
      validateArguments();
    } catch (final ParseException parseException) {
      throw new RuntimeException(parseException);
    }
  }

  private void validateArguments() {
    if (commandLine.getArgs().length != EXPECTED_ARGUMENTS) {
      usage();
      throw new InsufficientArgumentsException();
    }
  }

  private void usage() {
    final String header = "\nAttempt to recover wallet keys\n\n";
    final String footer = "\nTypical case:\n   recover /path/to/words.txt --keys 200 --privateKeys \n\n";

    final HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Recover <words file>", header, this. options, footer, true);
  }


  private Options makeOptions() {
    final Options options = new Options();
    options.addOption(Option.builder()
        .longOpt(OPTION_KEYS_TO_FIND)
        .hasArg()
        .desc(String.format("number of keys to output, default %s", DEFAULT_KEYS_TO_FIND))
        .build());
    options.addOption(Option.builder()
        .longOpt(OPTION_REQUESTS_PER_MINUTE)
        .hasArg()
        .desc(String.format("requests per minute to check balance, default %s", DEFAULT_REQUESTS_PER_MINUTE))
        .build());
    options.addOption(Option.builder()
        .longOpt(OPTION_OUTPUT_PRIVATE_KEYS)
        .desc("output private keys")
        .build());
    options.addOption(Option.builder()
        .longOpt(OPTION_REFUND_PURPOSE)
        .desc("process addresses used for refunds")
        .build());
    options.addOption(Option.builder()
        .longOpt(OPTION_CHANGE_PURPOSE)
        .desc("process addresses used for change")
        .build());
    return options;
  }

  public String wordsFilePath() {
    return commandLine.getArgs()[WORDS_FILE_ARG_INDEX];
  }

  public long requestsPerMinute() {
    return Long.valueOf(commandLine.getOptionValue(OPTION_REQUESTS_PER_MINUTE, DEFAULT_REQUESTS_PER_MINUTE));
  }

  public long requestDelay() {
    return (long)((requestsPerMinute() / requestsPerMinute()) * 1000f);
  }

  public boolean outputPrivateKeys() {
    return commandLine.hasOption(OPTION_OUTPUT_PRIVATE_KEYS);
  }

  public int keysToFind() {
    return Integer.valueOf(commandLine.getOptionValue(OPTION_KEYS_TO_FIND, DEFAULT_KEYS_TO_FIND));
  }

  public TransactionType purpose() {
    if (commandLine.hasOption(OPTION_REFUND_PURPOSE)) {
      return TransactionType.REFUNDS;
    } else if (commandLine.hasOption(OPTION_CHANGE_PURPOSE)) {
      return TransactionType.CHANGE;
    } else {
      return TransactionType.STORAGE;
    }
  }

  public static enum TransactionType {
    STORAGE,
    REFUNDS,
    CHANGE
  }
}
