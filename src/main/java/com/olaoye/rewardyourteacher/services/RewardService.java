package com.olaoye.rewardyourteacher.services;

import com.olaoye.rewardyourteacher.dto.PaymentResponse;
import com.olaoye.rewardyourteacher.dto.SenderTransferDto;
import com.olaoye.rewardyourteacher.exceptions.WalletNotFoundException;

public interface RewardService {
    PaymentResponse rewardTeacher(Long receiverId, SenderTransferDto senderTransferDto)
        throws WalletNotFoundException;
}
