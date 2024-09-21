package ies.sunday_crypto.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import ies.sunday_crypto.Models.TicketStatus;

@Entity
@Table(name = "ticket")
public class Ticket {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long investorId;
    
    @Column(nullable = false)
    private Long adminId;

    @Column(nullable = false, length = 64)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private TicketStatus status;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String reply;


    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
    
    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Long getInvestorId() {
        return investorId;
    }

    public String getReply() {
        return reply;
    }

    public Long getId() {
        return id;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }



  
}
