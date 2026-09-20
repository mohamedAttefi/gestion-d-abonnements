package com.abonnement.DAO;

import com.abonnement.entity.Paiement;

import java.util.List;

public interface PaiementDAO {

    void ajouter(Paiement paiement);

    List<Paiement> findAll();

    Paiement findById(String idPaiement);

    List<Paiement> findByAbonnementId(String idAbonnement);

    void modifier(Paiement paiement);

    void supprimer(String idPaiement);
}