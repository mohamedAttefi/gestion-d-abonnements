package com.abonnement.service;

import com.abonnement.DAO.AbonnementDAO;
import com.abonnement.entity.Abonnement;
import com.abonnement.entity.AbonnementAvecEngagement;
import com.abonnement.entity.StatutAbonnement;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AbonnementService {

    private AbonnementDAO abonnementDAO;

    public AbonnementService(AbonnementDAO abonnementDAO) {
        this.abonnementDAO = abonnementDAO;
    }

    // ==============================
    // AJOUTER
    // ==============================

    public void ajouter(Abonnement abonnement) {

        if (abonnement.getNomService() == null ||
                abonnement.getNomService().trim().isEmpty()) {

            System.out.println("Le nom du service est obligatoire.");
            return;
        }

        if (abonnement.getMontantMensuel() <= 0) {

            System.out.println(
                    "Le montant mensuel doit être supérieur à 0."
            );
            return;
        }

        if (abonnement.getDateDebut() == null) {

            System.out.println(
                    "La date de début est obligatoire."
            );
            return;
        }

        if (abonnement.getDateFin() != null &&
                abonnement.getDateFin().isBefore(abonnement.getDateDebut())) {

            System.out.println(
                    "La date de fin doit être après la date de début."
            );
            return;
        }

        abonnementDAO.ajouter(abonnement);
    }

    // ==============================
    // MODIFIER
    // ==============================

    public void modifier(Abonnement abonnement) {

        Abonnement existant =
                abonnementDAO.findById(abonnement.getId());

        if (existant == null) {

            System.out.println(
                    "Abonnement introuvable."
            );
            return;
        }

        abonnementDAO.modifier(abonnement);
    }

    // ==============================
    // SUPPRIMER
    // ==============================

    public void supprimer(String id) {

        Abonnement abonnement =
                abonnementDAO.findById(id);

        if (abonnement == null) {

            System.out.println(
                    "Abonnement introuvable."
            );
            return;
        }

        abonnementDAO.supprimer(id);
    }

    // ==============================
    // RÉSILIER
    // ==============================

    public void resilier(String id) {

        Abonnement abonnement =
                abonnementDAO.findById(id);

        if (abonnement == null) {

            System.out.println(
                    "Abonnement introuvable."
            );
            return;
        }

        abonnement.setStatut(
                StatutAbonnement.RESILIE
        );

        abonnementDAO.modifier(abonnement);

        System.out.println(
                "Abonnement résilié avec succès."
        );
    }

    // ==============================
    // FIND ALL
    // ==============================

    public List<Abonnement> findAll() {

        return abonnementDAO.findAll();
    }

    // ==============================
    // ABONNEMENTS ACTIFS
    // ==============================

    public List<Abonnement> findActifs() {

        return abonnementDAO.findAll()
                .stream()
                .filter(a ->
                        a.getStatut() == StatutAbonnement.ACTIVE
                )
                .collect(Collectors.toList());
    }

    // ==============================
    // FIND BY ID
    // ==============================

    public Abonnement findById(String id) {

        return abonnementDAO.findById(id);
    }

    // ==============================
    // GÉNÉRER LES ÉCHÉANCES
    // ==============================

    public void genererEcheances(String id) {

        Abonnement abonnement =
                abonnementDAO.findById(id);

        if (abonnement == null) {

            System.out.println(
                    "Abonnement introuvable."
            );
            return;
        }

        LocalDate dateDebut =
                abonnement.getDateDebut();

        LocalDate dateFin =
                abonnement.getDateFin();

        if (dateFin == null) {

            System.out.println(
                    "Impossible de générer les échéances " +
                            "sans date de fin."
            );
            return;
        }

        LocalDate date = dateDebut;

        System.out.println();
        System.out.println(
                "Échéances pour : "
                        + abonnement.getNomService()
        );

        while (!date.isAfter(dateFin)) {

            System.out.println(
                    "Échéance : " + date
            );

            date = date.plusMonths(1);
        }
    }
}