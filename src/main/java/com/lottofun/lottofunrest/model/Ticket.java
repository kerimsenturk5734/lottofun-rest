package com.lottofun.lottofunrest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long drawId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ticket_numbers", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "number")
    private Set<Integer> numbers;

    private Instant purchaseTime;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private Integer matchedCount;

    private String prize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
