package com.olaoye.rewardyourteacher.services.servicesImpl;

import com.olaoye.rewardyourteacher.dto.CurrentBalanceResponse;
import com.olaoye.rewardyourteacher.dto.WalletRequest;
import com.olaoye.rewardyourteacher.dto.WalletResponse;
import com.olaoye.rewardyourteacher.entity.Notification;
import com.olaoye.rewardyourteacher.entity.User;
import com.olaoye.rewardyourteacher.entity.Wallet;
import com.olaoye.rewardyourteacher.enums.TransactionType;
import com.olaoye.rewardyourteacher.enums.UserRole;
import com.olaoye.rewardyourteacher.exceptions.CustomException;
import com.olaoye.rewardyourteacher.exceptions.UserNotFoundException;
import com.olaoye.rewardyourteacher.repository.WalletRepository;
import com.olaoye.rewardyourteacher.services.NotificationService;
import com.olaoye.rewardyourteacher.services.StudentService;
import com.olaoye.rewardyourteacher.services.TransactionService;
import com.olaoye.rewardyourteacher.services.WalletService;
import com.olaoye.rewardyourteacher.utils.MapStructMapper;
import com.olaoye.rewardyourteacher.utils.VerifyTransactionResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final StudentService userService;
    private final TransactionService transactionService;
    private final NotificationService notificationService;
    private final MapStructMapper mapstructMapper;

    public WalletResponse createOrUpdateWallet(WalletRequest walletRequest, String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User does not exist");
        }
        Wallet wallet = user.getWallet();
        if (wallet == null) {
            wallet = createWallet(user, walletRequest.getName(), walletRequest.getDescription(), walletRequest);
       wallet.setCurrentBalance(BigDecimal.ZERO);
        } else {
            wallet = mapstructMapper.updateWalletFromWalletRequest(walletRequest, wallet);
        }

        Wallet savedWallet = walletRepository.save(wallet);
        user.setWallet(savedWallet);

        return mapstructMapper.walletToWalletResponse(savedWallet);
    }


    @Override
    public void fundWallet(String email, VerifyTransactionResponse payStackResponse, String walletName, String description) {
        User user = userService.findUserByEmail(email);
        if (user == null || UserRole.TEACHER.equals(user.getRole())) {
            throw new CustomException("User does not exist");
        }

        Wallet wallet = user.getWallet();
        if (wallet == null) {
            wallet = createWallet(user, walletName, description);
        }
        BigDecimal amount = payStackResponse.getData().getAmount();
        wallet.setCurrentBalance(wallet.getCurrentBalance().add(amount));
        Wallet savedWallet = walletRepository.save(wallet);

        user.setWallet(savedWallet);

        transactionService.updateUserTransactionList(user, user.getId(), amount, description, TransactionType.CREDIT);

        Notification notification = notificationService.saveNotification(user, description, "You funded your wallet with N" + amount);
        List<Notification> notifications = user.getNotificationList();
        notifications.add(notification);
        user.setNotificationList(notifications);

        userService.saveUser(user);
    }

    @Override
    public CurrentBalanceResponse currentUserWalletBalance(String email) {

        WalletResponse walletResponse = getWallet(email);
        CurrentBalanceResponse currentBalanceResponse = new CurrentBalanceResponse();
        currentBalanceResponse.setCurrentBalance(walletResponse.getCurrentBalance());
        return currentBalanceResponse;
    }

    @Override
    public WalletResponse getWallet(String email) {

        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User does not exist");
        }

        Wallet wallet = user.getWallet();
        if (wallet == null) {
            wallet = createWallet(user, "New Wallet", "New Wallet");
        }

        return mapstructMapper.walletToWalletResponse(wallet);
    }

    @Override
    public List<WalletResponse> getAllWallet() {
        List<Wallet> wallets = walletRepository.findAll();
        List<WalletResponse> walletResponseList = new ArrayList<>();
        for (Wallet wallet : wallets) {
            walletResponseList.add(mapstructMapper.walletToWalletResponse(wallet));
        }
        return walletResponseList;
    }

    @Override
    public Wallet createWallet(User user, String walletName, String description, WalletRequest... walletRequests) {
        Wallet wallet;
        if (walletRequests.length != 0) {
            wallet = mapstructMapper.walletRequestToWallet(walletRequests[0]);
        } else {
            wallet = new Wallet();
            wallet.setDescription(description);
            wallet.setPriority(2);
            wallet.setName(walletName);
        }
        String acctNo = RandomStringUtils.randomNumeric(10);
        wallet.setAccountNumber(acctNo);
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setUser(user);
        wallet.setCurrentBalance(BigDecimal.ZERO);
        return wallet;
    }
}
