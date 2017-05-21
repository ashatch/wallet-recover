# Wallet Recover

A tool to help recover Multibit HD corrupted wallets.

# How it works

Multibit HD uses BIP39, so it is possible to know what addresses/keys are generated for a
particular wallet.

From these words, this tool generates addresses and then checks the current balance for that
address.

Place the words, separated by spaces in a text file. Be careful with this - be sure you remove
this file properly afterwards. Don't _just_ delete it. Wipe it properly using secure wiping tools.

# Prerequisites

You'll need a JVM to run this and the ability to run _maven_ as per below.

# How to use it


        $ mvn package
        $ java -jar target/recover-1.0.0-SNAPSHOT.jar /Users/ashatch/Desktop/words.txt --keys 10
        EMPTY,1P2amf9oQSdZYE92PrDMUgzNtTWHpvtsSE,0
        EMPTY,1HgTs5ZHLCtqjVqWiozz8CXRzmzwfNKT72,0
        EMPTY,19kgc1m4BoXLG48KxDWfT3YF1sgMCQxKxx,0
        EMPTY,12YhjVUXrj1TjMu2hhDYaj31GctubjrV5j,0
        EMPTY,1BCqXuZKdUVDtTTCs4hwHJaEn9DVEEgEed,0
        EMPTY,1Grtg6L4T6gGQbchFVaWV1Qwxy4cEX1gfQ,0
        EMPTY,15n57hnu5ZbVY8rvKKQ1bDdEVSeZTYV6ZH,0
        EMPTY,1LBv59ahsvoE9DXmpbK46q23aDZm5T6m2Y,0
        EMPTY,1EwH3RqrNTq218wGWBdfTxDhPTk4CRxx5K,0
        EMPTY,13h6TbhcL8HsxdHUAPWCgMeRrZSNa68XHz,0

If a particular address _does_ happen to have some coin, you'll get, e.g:

        FOUND,1P2amf9oQSdZYE92PrDMUgzNtTWHpvtsSE,100000
        
Running the same command with the additional `--privateKeys` switch, then the private keys are
also listed, so that you can then add them to your wallet.

# Usage

        usage: Recover <words file> [--forChange] [--forRefunds] [--keys <arg>]
               [--privateKeys] [--requestsPerMinute <arg>]
        
        Attempt to recover wallet keys
        
            --forChange                 process addresses used for change
            --forRefunds                process addresses used for refunds
            --keys <arg>                number of keys to output, default 100
            --privateKeys               output private keys
            --requestsPerMinute <arg>   requests per minute to check balance,
                                        default 30
        
        Typical case:
           recover /path/to/words.txt --keys 200 --privateKeys
