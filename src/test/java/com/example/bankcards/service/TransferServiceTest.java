package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

public class TransferServiceTest {

    @Test
    void transfer_success_updates_balances() {
        CardRepository cardRepo = Mockito.mock(CardRepository.class);
        TransferRepository transferRepo = Mockito.mock(TransferRepository.class);
        TransferService service = new TransferService(cardRepo, transferRepo);

        User user = new User();
        user.setId(1L);

        Card from = new Card();
        from.setId(10L);
        from.setUser(user);
        from.setCurrency("RUB");
        from.setStatus(Card.Status.ACTIVE);
        from.setBalanceMinor(10_000L);

        Card to = new Card();
        to.setId(20L);
        to.setUser(user);
        to.setCurrency("RUB");
        to.setStatus(Card.Status.ACTIVE);
        to.setBalanceMinor(1_000L);

        when(cardRepo.findByIdAndUser(10L, user)).thenReturn(Optional.of(from));
        when(cardRepo.findByIdAndUser(20L, user)).thenReturn(Optional.of(to));
        when(transferRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.transferBetweenOwnCards(user, 10L, 20L, 2_500L);

        assertEquals(7_500L, from.getBalanceMinor());
        assertEquals(3_500L, to.getBalanceMinor());
    }
}


