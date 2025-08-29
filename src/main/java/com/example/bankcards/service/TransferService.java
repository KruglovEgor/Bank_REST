package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {

    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;

    public TransferService(CardRepository cardRepository, TransferRepository transferRepository) {
        this.cardRepository = cardRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public Transfer transferBetweenOwnCards(User user, Long fromCardId, Long toCardId, long amountMinor) {
        if (amountMinor <= 0) throw new IllegalArgumentException("Amount must be positive");
        Card from = cardRepository.findByIdAndUser(fromCardId, user).orElseThrow();
        Card to = cardRepository.findByIdAndUser(toCardId, user).orElseThrow();
        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        if (from.getStatus() != Card.Status.ACTIVE || to.getStatus() != Card.Status.ACTIVE) {
            throw new IllegalStateException("Cards must be ACTIVE");
        }
        if (from.getBalanceMinor() < amountMinor) {
            throw new IllegalStateException("Insufficient funds");
        }
        from.setBalanceMinor(from.getBalanceMinor() - amountMinor);
        to.setBalanceMinor(to.getBalanceMinor() + amountMinor);
        Transfer t = new Transfer();
        t.setUser(user);
        t.setFromCard(from);
        t.setToCard(to);
        t.setAmountMinor(amountMinor);
        return transferRepository.save(t);
    }

    public Page<Transfer> history(User user, Pageable pageable) {
        return transferRepository.findAllByUser(user, pageable);
    }
}


