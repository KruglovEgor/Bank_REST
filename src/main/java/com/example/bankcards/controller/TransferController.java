package com.example.bankcards.controller;

import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.TransferService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/transfers")
@Validated
@Tag(name = "Transfers")
public class TransferController {

    private final TransferService transferService;
    private final UserRepository userRepository;

    public TransferController(TransferService transferService, UserRepository userRepository) {
        this.transferService = transferService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody CreateTransferRequest request
    ) {
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        Transfer t = transferService.transferBetweenOwnCards(user, request.fromCardId(), request.toCardId(), request.amountMinor());
        return ResponseEntity.ok(Map.of("id", t.getId(), "amountMinor", t.getAmountMinor()));
    }

    @GetMapping
    public Page<Transfer> list(@AuthenticationPrincipal UserDetails principal, Pageable pageable) {
        User user = userRepository.findByUsername(principal.getUsername()).orElseThrow();
        return transferService.history(user, pageable);
    }

    public record CreateTransferRequest(
            @NotNull Long fromCardId,
            @NotNull Long toCardId,
            @Min(1) long amountMinor
    ) {}
}


