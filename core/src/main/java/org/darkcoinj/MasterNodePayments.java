package org.darkcoinj;

import org.bitcoinj.core.MasterNodePaymentWinner;

import java.util.ArrayList;

/**
 * Created by Eric on 2/8/2015.
 */

//
// Masternode Payments Class
// Keeps track of who should get paid for which blocks
//
public class MasterNodePayments {
    ArrayList<MasterNodePaymentWinner> vWinning;
    int nSyncedFromPeer;
    String strMasterPrivKey;
    String strTestPubKey;
    String strMainPubKey;
    boolean enabled;

    MasterNodePayments() {
        strMainPubKey = "04e125f52309d61d353c50914435c52c18a0b0c06cf770da68e11e8a5f440629e7e0b95de70b117aff78e3bae9727cec786c9e719bb8be098aab9d35f6c81c61fa";
        strTestPubKey = "04e125f52309d61d353c50914435c52c18a0b0c06cf770da68e11e8a5f440629e7e0b95de70b117aff78e3bae9727cec786c9e719bb8be098aab9d35f6c81c61fa";
        enabled = false;
    }
    /*
    bool SetPrivKey(std::string strPrivKey);
    bool CheckSignature(CMasternodePaymentWinner& winner);
    bool Sign(CMasternodePaymentWinner& winner);

    // Deterministically calculate a given "score" for a masternode depending on how close it's hash is
    // to the blockHeight. The further away they are the better, the furthest will win the election
    // and get paid this block
    //

    uint64_t CalculateScore(Sha256Hash blockHash, CTxIn& vin);
    bool GetWinningMasternode(int nBlockHeight, CTxIn& vinOut);
    bool AddWinningMasternode(CMasternodePaymentWinner& winner);
    bool ProcessBlock(int nBlockHeight);
    void Relay(CMasternodePaymentWinner& winner);
    void Sync(CNode* node);
    void CleanPaymentList();
    int LastPayment(CMasterNode& mn);

    //slow
    bool GetBlockPayee(int nBlockHeight, CScript& payee);
    */
}
