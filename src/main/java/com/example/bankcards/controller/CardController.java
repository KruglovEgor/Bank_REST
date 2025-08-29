package com.example.bankcards.controller;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CryptoUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cards")
@Validated
@Tag(name = "Cards")
public class CardController {

    private final CardService cardService;
    private final UserRepository userRepository;

    public CardController(CardService cardService, UserRepository userRepository) {
        this.cardService = cardService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody CreateCardRequest request
    ) {
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        Card card = cardService.createCard(user, request.number(), request.holder(), request.expiryMonth(), request.expiryYear(), request.currency());
        return ResponseEntity.ok(Map.of(
                "id", card.getId(),
                "mask", CryptoUtil.maskCardNumber("000000000000" + card.getLast4())
        ));
    }

    @GetMapping
    public Page<Card> list(@AuthenticationPrincipal UserDetails principal, Pageable pageable) {
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        return cardService.findUserCards(user, pageable);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id,
            @RequestParam("status") Card.Status status
    ) {
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        cardService.changeStatus(user, id, status);
        return ResponseEntity.ok(Map.of("status", status.name()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/{id}/status")
    public ResponseEntity<?> adminChangeStatus(@PathVariable Long id, @RequestParam("status") Card.Status status) {
        cardService.adminChangeStatus(id, status);
        return ResponseEntity.ok(Map.of("status", status.name()));
    }

    public record CreateCardRequest(
            @NotBlank String number,
            @NotBlank String holder,
            @NotNull Integer expiryMonth,
            @NotNull Integer expiryYear,
            @NotBlank String currency
    ) {}
}


