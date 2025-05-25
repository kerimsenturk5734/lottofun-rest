package com.lottofun.lottofunrest.repository;

import com.lottofun.lottofunrest.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Page<Ticket> findByUser_UsernameOrderByPurchaseTimeAsc(String username, Pageable pageable);
    List<Ticket> findByDrawId(Long drawId);
}
