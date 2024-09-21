package ies.sunday_crypto.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admintkn")
public class Admin_Token {

    @Id
    @Column(name = "tkn", nullable = false, unique = true)
    private String tkn;


    public void setTkn(String key) {
        this.tkn = key;
    }


    public String getTkn() {
        return tkn;
    }

}
