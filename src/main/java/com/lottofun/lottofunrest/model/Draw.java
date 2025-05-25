package com.lottofun.lottofunrest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "draws")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Draw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant drawDate;

    private Instant statusUpdatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "winning_numbers", joinColumns = @JoinColumn(name = "draw_id"))
    @Column(name = "number")
    private Set<Integer> winningNumbers;

    @Enumerated(EnumType.STRING)
    private DrawStatus status;
}
