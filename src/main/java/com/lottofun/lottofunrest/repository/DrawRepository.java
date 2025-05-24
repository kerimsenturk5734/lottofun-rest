package com.lottofun.lottofunrest.repository;

import com.lottofun.lottofunrest.model.Draw;
import com.lottofun.lottofunrest.model.DrawStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrawRepository extends JpaRepository<Draw, Integer> {
    Page<Draw> findAllByStatusInOrderByDrawDate(List<DrawStatus> statusList, Pageable pageable);
    Optional<Draw> findFirstByStatusOrderByDrawDateAsc(DrawStatus status);
    boolean existsByStatus(DrawStatus status);
}
