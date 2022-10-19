package com.olaoye.rewardyourteacher.services;

import com.olaoye.rewardyourteacher.dto.CurrentBalanceResponse;
import com.olaoye.rewardyourteacher.dto.WalletRequest;
import com.olaoye.rewardyourteacher.dto.WalletResponse;
import com.olaoye.rewardyourteacher.entity.User;
import com.olaoye.rewardyourteacher.entity.Wallet;
import com.olaoye.rewardyourteacher.utils.VerifyTransactionResponse;

import java.util.List;

public interface WalletService {
    WalletResponse createOrUpdateWallet(WalletRequest walletRequest, String email);

    CurrentBalanceResponse currentUserWalletBalance(String email);

    void fundWallet(String email, VerifyTransactionResponse payStackResponse, String walletName, String description);

    WalletResponse getWallet(String email);

    List<WalletResponse> getAllWallet();

    Wallet createWallet(User user, String walletName, String description, WalletRequest... walletRequests);
}
