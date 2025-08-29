package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final byte[] encryptionKey;

    public CardService(CardRepository cardRepository, @Value("${card.encryption-secret}") String secret) {
        this.cardRepository = cardRepository;
        this.encryptionKey = secret.getBytes(StandardCharsets.UTF_8);
    }

    @Transactional
    public Card createCard(User owner, String number, String holder, int month, int year, String currency) {
        Card card = new Card();
        card.setUser(owner);
        card.setNumberEncrypted(CryptoUtil.encryptAesGcm(number, encryptionKey));
        card.setLast4(number.substring(number.length() - 4));
        card.setHolder(holder);
        card.setExpiryMonth(month);
        card.setExpiryYear(year);
        card.setStatus(Card.Status.ACTIVE);
        card.setBalanceMinor(0L);
        card.setCurrency(currency);
        return cardRepository.save(card);
    }

    public Page<Card> findUserCards(User owner, Pageable pageable) {
        return cardRepository.findAllByUser(owner, pageable);
    }

    @Transactional
    public void changeStatus(User owner, Long cardId, Card.Status status) {
        Card card = cardRepository.findByIdAndUser(cardId, owner).orElseThrow();
        card.setStatus(status);
    }

    @Transactional
    public void adminChangeStatus(Long cardId, Card.Status status) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setStatus(status);
    }
}


