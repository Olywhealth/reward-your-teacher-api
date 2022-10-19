package com.olaoye.rewardyourteacher.services.servicesImpl;

import com.olaoye.rewardyourteacher.dto.PaymentResponse;
import com.olaoye.rewardyourteacher.dto.SenderTransferDto;
import com.olaoye.rewardyourteacher.entity.Notification;
import com.olaoye.rewardyourteacher.entity.User;
import com.olaoye.rewardyourteacher.entity.Wallet;
import com.olaoye.rewardyourteacher.enums.TransactionType;
import com.olaoye.rewardyourteacher.exceptions.CustomException;
import com.olaoye.rewardyourteacher.exceptions.UserNotFoundException;
import com.olaoye.rewardyourteacher.exceptions.WalletNotFoundException;
import com.olaoye.rewardyourteacher.repository.NotificationRepository;
import com.olaoye.rewardyourteacher.repository.UserRepository;
import com.olaoye.rewardyourteacher.repository.WalletRepository;
import com.olaoye.rewardyourteacher.services.RewardService;
import com.olaoye.rewardyourteacher.services.TransactionService;
import com.olaoye.rewardyourteacher.services.WalletService;
import com.olaoye.rewardyourteacher.utils.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RewardServiceImpl implements RewardService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final NotificationRepository notificationRepository;
    private final AuthDetails authDetails;
    private final WalletService walletService;

    private final TransactionService transactionService;


    @Override
    public PaymentResponse rewardTeacher(Long receiverId, SenderTransferDto senderTransferDto) throws WalletNotFoundException {
        User sender = authDetails.getAuthorizedUser();
        User receiver = userRepository.findById(receiverId).orElseThrow(UserNotFoundException::new);
        Wallet senderWallet = sender.getWallet();
        Wallet receiverWallet = receiver.getWallet();
        if (receiverWallet == null) {
            receiverWallet =  walletService.createWallet(receiver, "New wallet", "Wallet created due to payment received");
            receiver.setWallet(receiverWallet);
        }
        BigDecimal senderWalletBal = senderWallet.getCurrentBalance();
        BigDecimal receiverWalletBal = receiverWallet.getCurrentBalance();

        PaymentResponse response = new PaymentResponse();
        BigDecimal amount = senderTransferDto.getAmountToSend();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException("Amount is not valid");
        } else if (senderWalletBal.compareTo(senderTransferDto.getAmountToSend()) >= 0) {
            senderWallet.setCurrentBalance(senderWalletBal.subtract(senderTransferDto.getAmountToSend()));
            receiverWallet.setCurrentBalance(receiverWalletBal.add(senderTransferDto.getAmountToSend()));
            walletRepository.save(senderWallet);
            walletRepository.save(receiverWallet);

            String descriptionForSender = "You sent " + amount + " to " + receiver.getFirstName() + " " + receiver.getLastName();
            String descriptionToReceiver = sender.getFirstName() + " sent you N" + amount;

            transactionService.updateUserTransactionList(sender, sender.getId(), amount, descriptionForSender, TransactionType.DEBIT);
            transactionService.updateUserTransactionList(receiver, sender.getId(), amount, descriptionToReceiver, TransactionType.CREDIT);

            userRepository.save(sender);
            userRepository.save(receiver);

            String message = "You sent money to " + receiver.getFirstName();
            String message2 = sender.getFirstName() + " sent you N" + senderTransferDto.getAmountToSend();

            notificationRepository.save(notification(sender, receiverWallet, message, TransactionType.CREDIT));
            notificationRepository.save(notification(receiver, senderWallet, message2, TransactionType.DEBIT));

            response.setMessage("A Reward of N" + senderTransferDto.getAmountToSend()
                    + " was sent successfully to " + receiver.getFirstName());
            response.setCurrentBalance(senderWallet.getCurrentBalance());
            return response;
        } else {
            throw new CustomException("Insufficient fund, amount in wallet is N" + senderWallet.getCurrentBalance());
        }
    }

    private Notification notification(User user, Wallet receiverWallet, String message, TransactionType transactionType) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUser(user);
        notification.setNotificationBody(receiverWallet.getDescription());
        notification.setTransactionType(transactionType);
        notification.setCreatedAt(LocalDateTime.now());
        return notification;
    }
}








