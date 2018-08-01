package org.bitcoinj.core;

import java.math.BigInteger;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Hash Engineering Solutions
 * Date: 5/3/14
 * To change this template use File | Settings | File Templates.
 */
public class CoinDefinition {


    public static final String coinName = "Jadepay";
    public static final String coinTicker = "JADE";
    public static final String coinURIScheme = "jadepay";
    public static final String cryptsyMarketId = "155";
    public static final String cryptsyMarketCurrency = "BTC";
    public static final String PATTERN_PRIVATE_KEY_START_UNCOMPRESSED = "[5]";
    public static final String PATTERN_PRIVATE_KEY_START_COMPRESSED = "[L]";

    public enum CoinPrecision {
        Coins,
        Millicoins,
    }
    public static final CoinPrecision coinPrecision = CoinPrecision.Coins;

    public static final String UNSPENT_API_URL = "https://chainz.cryptoid.info/jadepay/api.dws?q=unspent";
    public enum UnspentAPIType {
        BitEasy,
        Blockr,
        Abe,
        Cryptoid,
    };
    public static final UnspentAPIType UnspentAPI = UnspentAPIType.Cryptoid;

    public static final String BLOCKEXPLORER_BASE_URL_PROD = "http://explorer.jadepay.org/";    //blockr.io
    public static final String BLOCKEXPLORER_ADDRESS_PATH = "address/";             //blockr.io path
    public static final String BLOCKEXPLORER_TRANSACTION_PATH = "tx/";              //blockr.io path
    public static final String BLOCKEXPLORER_BLOCK_PATH = "block/";                 //blockr.io path
    public static final String BLOCKEXPLORER_BASE_URL_TEST = "http://test.explorer.jadepay.org/";

    public static final String DONATION_ADDRESS = "JbujKSjAsPhFUEq4vz4RKd4Mb43EQW5Awu";  //Hash Engineering donation JADEPAY address

    enum CoinHash {
        SHA256,
        scrypt,
        jad8
    };
    public static final CoinHash coinPOWHash = CoinHash.jad8;

    public static boolean checkpointFileSupport = false;

    public static final int TARGET_TIMESPAN = (int)(3 * 60);  // 3 minutes per difficulty cycle, on average.
    public static final int TARGET_SPACING = (int)(3 * 60);  // 3 minutes per block.
    public static final int INTERVAL = TARGET_TIMESPAN / TARGET_SPACING;  //every blocks

    public static final int getInterval(int height, boolean testNet) {
            return INTERVAL;      //108
    }
    public static final int getIntervalCheckpoints() {
            return INTERVAL;

    }
    public static final int getTargetTimespan(int height, boolean testNet) {
            return TARGET_TIMESPAN;    //3 min
    }

    public static int spendableCoinbaseDepth = 100; //main.h: static const int COINBASE_MATURITY
    public static final long MAX_COINS = 25000000;                 //main.h:  MAX_MONEY


    public static final long DEFAULT_MIN_TX_FEE = 10000;   // MIN_TX_FEE
    public static final long DUST_LIMIT = 5460; //Transaction.h CTransaction::GetDustThreshold for 10000 MIN_TX_FEE
    public static final long INSTANTX_FEE = 100000; //0.001 JADEPAY (updated for 12.1)
    public static final boolean feeCanBeRaised = false;

    //
    // Jadepay 0.12.1.x
    //
    public static final int PROTOCOL_VERSION = 70001;          //version.h PROTOCOL_VERSION
    public static final int MIN_PROTOCOL_VERSION = 70001;        //version.h MIN_PROTO_VERSION

    public static final int BLOCK_CURRENTVERSION = 3;   //CBlock::CURRENT_VERSION
    public static final int MAX_BLOCK_SIZE = 2 * 1000 * 1000;


    public static final boolean supportsBloomFiltering = true; //Requires PROTOCOL_VERSION 70000 in the client

    public static final int Port    = 22155;       //protocol.h GetDefaultPort(testnet=false)
    public static final int TestPort = 12155;     //protocol.h GetDefaultPort(testnet=true)

    //
    //  Production
    //
    public static final int AddressHeader = 43;             //base58.h CBitcoinAddress::PUBKEY_ADDRESS
    public static final int p2shHeader = 5;             //base58.h CBitcoinAddress::SCRIPT_ADDRESS
    public static final int dumpedPrivateKeyHeader = 128;   // SECRET_KEY common
    public static final long oldPacketMagic = 0xefbeeeaf;      //0xfb, 0xc0, 0xb6, 0xdb
    public static final long PacketMagic = 0xf3a2daa4;

    //Genesis Block Information from main.cpp: LoadBlockIndex
    static public long genesisBlockDifficultyTarget = (0x1e0ffff0L);         //main.cpp: LoadBlockIndex
    static public long genesisBlockTime = 1533097800L;                       //main.cpp: LoadBlockIndex
    static public long genesisBlockNonce = (105162);                         //main.cpp: LoadBlockIndex
    static public String genesisHash = "0000071d877e71cdd4072a6fca7e2fece5d6c902ba33671a83e658299a2c1b3c"; //main.cpp: hashGenesisBlock
    static public String genesisMerkleRoot = "63482500d4814d581d1d485a3c9e70399c72e496c79ec437b20223ec21b87b30";
    static public int genesisBlockValue = 10;                                                              //main.cpp: LoadBlockIndex

    //taken from the raw data of the block explorer
    static public String genesisTxInBytes = "04ffff001d01042a4368616c6c656e67657220617070726f616368696e67204e6576657220676f6e6e612067697665207570";   //"jadepay Challenger approaching Never gonna give up"
    static public String genesisTxOutBytes = "0484d7b993acfc9686b20dabeb4d8acfe8cc2c0bdab119fc141a1a56581115f3f8fd9e6aaf4141d189a9fc8814585999bf47cfa0fb56340dccf3cbffc6e0cf394e";

    //net.cpp strDNSSeed
    static public String[] dnsSeeds = new String[] {
            "node01.jadepay.org",
            "node02.jadepay.org",
            "explorer.jadepay.org"
    };


    public static int minBroadcastConnections = 0;   //0 for default; Using 3 like BreadWallet.

    //
    // TestNet - JADEPAY
    //
    public static final boolean supportsTestNet = false;
    public static final int testnetAddressHeader = 139;             //base58.h CBitcoinAddress::PUBKEY_ADDRESS_TEST
    public static final int testnetp2shHeader = 19;             //base58.h CBitcoinAddress::SCRIPT_ADDRESS_TEST
    public static final long testnetPacketMagic = 0x45761eba;      //
    public static final String testnetGenesisHash = "000002163d875acf11fa454873a0ae337abf56f08574971d7ab7be06aa062469";
    static public long testnetGenesisBlockDifficultyTarget = (0x1e0ffff0L);         //main.cpp: LoadBlockIndex
    static public long testnetGenesisBlockTime = 1530940000L;                       //main.cpp: LoadBlockIndex
    static public long testnetGenesisBlockNonce = (2157120);                         //main.cpp: LoadBlockIndex





    //main.cpp GetBlockValue(height, fee)
    public static final Coin GetBlockReward(int height)
    {
        int COIN = 1;
        Coin nSubsidy = Coin.valueOf(10, 0);
        if (height == 1)
            nSubsidy = Coin.valueOf(400000, 0);
        return nSubsidy;
    }

    public static int subsidyDecreaseBlockCount = 3000000;     //main.cpp GetBlockValue(height, fee)

    public static BigInteger proofOfWorkLimit = Utils.decodeCompactBits(0x1e0fffffL);  //main.cpp bnProofOfWorkLimit (~uint256(0) >> 20); // jadepay: starting difficulty is 1 / 2^12

    static public String[] testnetDnsSeeds = new String[] {
            "testnet-seed.jadepay.org",
            "test.explorer.jadepay.org",
    };
    //from main.h: CAlert::CheckSignature
    public static final String SATOSHI_KEY = "04e7635d1cac6db47309ea4fcc3d846258fbbff3944f56f1fd1ff89e93f2edf691d10de3fc58e01fa3f0463e4fa1d2cfd2ca036cdef074050329eb965c3017eb45";
    public static final String TESTNET_SATOSHI_KEY = "04e7635d1cac6db47309ea4fcc3d846258fbbff3944f56f1fd1ff89e93f2edf691d10de3fc58e01fa3f0463e4fa1d2cfd2ca036cdef074050329eb965c3017eb45";

    /** The string returned by getId() for the main, production network where people trade things. */
    public static final String ID_MAINNET = "org.darkcoin.production";
    /** The string returned by getId() for the testnet. */
    public static final String ID_TESTNET = "org.darkcoin.test";
    /** Unit test network. */
    public static final String ID_UNITTESTNET = "com.google.darkcoin.unittest";

    //checkpoints.cpp Checkpoints::mapCheckpoints
    public static void initCheckpoints(Map<Integer, Sha256Hash> checkpoints)
    {

        checkpoints.put(  0, Sha256Hash.wrap("0000071d877e71cdd4072a6fca7e2fece5d6c902ba33671a83e658299a2c1b3c"));

    }

    //Unit Test Information
    public static final String UNITTEST_ADDRESS = "Jg9LW2wJy1p8W5F2qxSeV9Qg73XyrPBHFo";
    public static final String UNITTEST_ADDRESS_PRIVATE_KEY = "5JjVr7SWyy8wP1shV6j1Qrzr1mtNY7xvBr3RLTwaQB3TyWpbyZd";

}
