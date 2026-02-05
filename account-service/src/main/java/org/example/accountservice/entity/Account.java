package org.example.accountservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private Integer solde;

    /**
     * Nombre total de cartes associées à ce compte,
     * mis à jour en temps réel via Kafka.
     */
    private Integer totalCards = 0;

    /**
     * Nombre total de prêts associés à ce compte,
     * mis à jour en temps réel via Kafka.
     */
    private Integer totalLoans = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSolde() {
        return solde;
    }

    public void setSolde(Integer solde) {
        this.solde = solde;
    }

    public Integer getTotalCards() {
        return totalCards;
    }

    public void setTotalCards(Integer totalCards) {
        this.totalCards = totalCards;
    }

    public Integer getTotalLoans() {
        return totalLoans;
    }

    public void setTotalLoans(Integer totalLoans) {
        this.totalLoans = totalLoans;
    }
}

