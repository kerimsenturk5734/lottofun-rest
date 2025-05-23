package com.lottofun.lottofunrest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    private LocalDateTime drawDate;

    @ElementCollection
    @CollectionTable(name = "winning_numbers", joinColumns = @JoinColumn(name = "draw_id"))
    @Column(name = "number")
    private Set<Integer> winningNumbers;

    @Enumerated(EnumType.STRING)
    private DrawStatus status;
}
