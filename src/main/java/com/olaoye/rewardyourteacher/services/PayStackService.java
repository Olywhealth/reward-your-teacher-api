package com.olaoye.rewardyourteacher.services;

import com.olaoye.rewardyourteacher.dto.PayStackPaymentRequest;
import com.olaoye.rewardyourteacher.dto.PayStackResponse;
import com.olaoye.rewardyourteacher.utils.VerifyTransactionResponse;

import java.io.IOException;
import java.security.Principal;

public interface PayStackService {

    PayStackResponse initTransaction(Principal principal, PayStackPaymentRequest request) throws Exception;

    VerifyTransactionResponse verifyPayment(String reference) throws IOException;
}
