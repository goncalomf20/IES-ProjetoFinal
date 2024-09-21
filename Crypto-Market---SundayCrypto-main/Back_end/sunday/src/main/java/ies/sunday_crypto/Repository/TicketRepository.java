package ies.sunday_crypto.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ies.sunday_crypto.Models.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByAdminId(Long adminId);
    
}