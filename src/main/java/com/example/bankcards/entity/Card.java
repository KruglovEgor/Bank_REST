package com.example.bankcards.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card {

    public enum Status { ACTIVE, BLOCKED, EXPIRED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "number_encrypted", nullable = false, length = 255)
    private String numberEncrypted;

    @Column(name = "last4", nullable = false, length = 4)
    private String last4;

    @Column(name = "holder", nullable = false)
    private String holder;

    @Column(name = "expiry_month", nullable = false)
    private Integer expiryMonth;

    @Column(name = "expiry_year", nullable = false)
    private Integer expiryYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Column(name = "balance_minor", nullable = false)
    private Long balanceMinor;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getNumberEncrypted() { return numberEncrypted; }
    public void setNumberEncrypted(String numberEncrypted) { this.numberEncrypted = numberEncrypted; }

    public String getLast4() { return last4; }
    public void setLast4(String last4) { this.last4 = last4; }

    public String getHolder() { return holder; }
    public void setHolder(String holder) { this.holder = holder; }

    public Integer getExpiryMonth() { return expiryMonth; }
    public void setExpiryMonth(Integer expiryMonth) { this.expiryMonth = expiryMonth; }

    public Integer getExpiryYear() { return expiryYear; }
    public void setExpiryYear(Integer expiryYear) { this.expiryYear = expiryYear; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Long getBalanceMinor() { return balanceMinor; }
    public void setBalanceMinor(Long balanceMinor) { this.balanceMinor = balanceMinor; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}


