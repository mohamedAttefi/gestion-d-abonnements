package com.abonnement.DAO;

import com.abonnement.entity.Abonnement;

import java.util.List;

public interface AbonnementDAO {

    void ajouter(Abonnement abonnement);

    List<Abonnement> findAll();

    Abonnement findById(String id);

    void modifier(Abonnement abonnement);

    void supprimer(String id);
}