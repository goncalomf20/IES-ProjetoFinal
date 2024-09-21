package ies.sunday_crypto.Controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sound.sampled.Port;

import org.hibernate.mapping.Array;
import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ies.sunday_crypto.Models.CoinMoney;
import ies.sunday_crypto.Models.Crypto;
import ies.sunday_crypto.Models.Investor;
import ies.sunday_crypto.Models.Key;
import ies.sunday_crypto.Models.Name;
import ies.sunday_crypto.Models.Portfolio;
import ies.sunday_crypto.Models.Ticket;
import ies.sunday_crypto.Repository.CryptoRepository;
import ies.sunday_crypto.Repository.InvestorRepository;
import ies.sunday_crypto.Repository.PortfolioRepository;
import ies.sunday_crypto.Repository.TicketRepository;

@RestController
@RequestMapping("/ticket")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;


    @PostMapping("/add")
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket ticket) {
        try {
            Ticket _ticket = ticketRepository.save(ticket);
            return new ResponseEntity<>(_ticket, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getTickets/{adminId}")
    public List<Ticket> getTickets(@PathVariable Long adminId) {
        System.out.println(ticketRepository.findByAdminId(adminId));
        return ticketRepository.findByAdminId(adminId);
    }

    @GetMapping("/all")
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    @DeleteMapping("/del/{id}")
    public void deletebyId(@PathVariable Long id) {
        ticketRepository.deleteById(id);
    }

}
