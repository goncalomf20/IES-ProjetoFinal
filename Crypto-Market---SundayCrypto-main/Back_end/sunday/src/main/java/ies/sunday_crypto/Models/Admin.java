package ies.sunday_crypto.Models;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fname;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String lname;
    
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = true,unique = true)
    private String token;

    @Column(nullable = true)
    private List<Long> ticketsAssigned;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
    
    public void setTicketsAssigned(List<Long> ticketsAssigned) {
        this.ticketsAssigned = ticketsAssigned;
    }

    public List<Long> getTicketsAssigned() {
        return ticketsAssigned;
    }

    public static Object builder() {
        return null;
    }

}
