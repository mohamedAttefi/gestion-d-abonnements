package com.abonnement.service;

import com.abonnement.DAO.AbonnementDAO;
import com.abonnement.DAO.PaiementDAO;
import com.abonnement.entity.Abonnement;
import com.abonnement.entity.AbonnementAvecEngagement;
import com.abonnement.entity.Paiement;
import com.abonnement.entity.StatutPaiement;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PaiementService {

    private PaiementDAO paiementDAO;
    private AbonnementDAO abonnementDAO;

    public PaiementService(PaiementDAO paiementDAO,
                           AbonnementDAO abonnementDAO) {
        this.paiementDAO = paiementDAO;
        this.abonnementDAO = abonnementDAO;
    }

    // =========================================================
    // AJOUTER UN PAIEMENT
    // =========================================================

    public void ajouter(Paiement paiement) {

        if (paiement.getIdAbonnement() == null ||
                paiement.getIdAbonnement().trim().isEmpty()) {

            System.out.println("L'ID de l'abonnement est obligatoire.");
            return;
        }

        Abonnement abonnement =
                abonnementDAO.findById(paiement.getIdAbonnement());

        if (abonnement == null) {
            System.out.println("Abonnement introuvable.");
            return;
        }

        if (paiement.getDateEcheance() == null) {
            System.out.println("La date d'échéance est obligatoire.");
            return;
        }

        paiementDAO.ajouter(paiement);

        System.out.println("Paiement enregistré avec succès.");
    }

    // =========================================================
    // MODIFIER UN PAIEMENT
    // =========================================================

    public void modifier(Paiement paiement) {

        Paiement existant =
                paiementDAO.findById(paiement.getIdPaiement());

        if (existant == null) {
            System.out.println("Paiement introuvable.");
            return;
        }

        paiementDAO.modifier(paiement);

        System.out.println("Paiement modifié avec succès.");
    }

    // =========================================================
    // SUPPRIMER UN PAIEMENT
    // =========================================================

    public void supprimer(String idPaiement) {

        Paiement paiement =
                paiementDAO.findById(idPaiement);

        if (paiement == null) {
            System.out.println("Paiement introuvable.");
            return;
        }

        paiementDAO.supprimer(idPaiement);

        System.out.println("Paiement supprimé avec succès.");
    }

    // =========================================================
    // TOUS LES PAIEMENTS
    // =========================================================

    public List<Paiement> findAll() {
        return paiementDAO.findAll();
    }

    // =========================================================
    // PAIEMENT PAR ID
    // =========================================================

    public Paiement findById(String id) {
        return paiementDAO.findById(id);
    }

    // =========================================================
    // PAIEMENTS D'UN ABONNEMENT
    // =========================================================

    public List<Paiement> findByAbonnementId(String idAbonnement) {

        return paiementDAO.findByAbonnementId(idAbonnement);
    }

    // =========================================================
    // PAIEMENTS NON PAYÉS
    // =========================================================

    public List<Paiement> findImpayes() {

        return paiementDAO.findAll()
                .stream()
                .filter(p ->
                        p.getStatut() == StatutPaiement.NON_PAYE
                                ||
                                p.getStatut() == StatutPaiement.EN_RETARD
                )
                .collect(Collectors.toList());
    }

    // =========================================================
    // PAIEMENTS PAYÉS
    // =========================================================

    public List<Paiement> findPayes() {

        return paiementDAO.findAll()
                .stream()
                .filter(p ->
                        p.getStatut() == StatutPaiement.PAYE
                )
                .collect(Collectors.toList());
    }

    // =========================================================
    // METTRE À JOUR LES PAIEMENTS EN RETARD
    // =========================================================

    public void verifierRetards() {

        List<Paiement> paiements = paiementDAO.findAll();

        LocalDate aujourdHui = LocalDate.now();

        for (Paiement paiement : paiements) {

            if (paiement.getStatut() == StatutPaiement.NON_PAYE
                    && paiement.getDateEcheance() != null
                    && paiement.getDateEcheance().isBefore(aujourdHui)) {

                paiement.setStatut(StatutPaiement.EN_RETARD);

                paiementDAO.modifier(paiement);
            }
        }

        System.out.println("Vérification des retards terminée.");
    }

    // =========================================================
    // 5 DERNIERS PAIEMENTS
    // =========================================================

    public List<Paiement> find5Derniers() {

        return paiementDAO.findAll()
                .stream()
                .sorted((p1, p2) -> {

                    if (p1.getDatePaiement() == null) {
                        return 1;
                    }

                    if (p2.getDatePaiement() == null) {
                        return -1;
                    }

                    return p2.getDatePaiement()
                            .compareTo(p1.getDatePaiement());
                })
                .limit(5)
                .collect(Collectors.toList());
    }

    // =========================================================
    // TOTAL IMPAYÉS
    // =========================================================
    /*
       Attention :
       Paiement ne possède PAS de montant.

       On utilise donc le montant mensuel de l'abonnement.
    */

    public double calculerTotalImpayes() {

        List<Paiement> impayes = findImpayes();

        double total = 0;

        for (Paiement paiement : impayes) {

            Abonnement abonnement =
                    abonnementDAO.findById(
                            paiement.getIdAbonnement()
                    );

            if (abonnement != null) {
                total += abonnement.getMontantMensuel();
            }
        }

        return total;
    }

    // =========================================================
    // TOTAL DES ABONNEMENTS AVEC ENGAGEMENT IMPAYÉS
    // =========================================================

    public double calculerTotalImpayesAvecEngagement() {

        List<Paiement> impayes = findImpayes();

        double total = 0;

        for (Paiement paiement : impayes) {

            Abonnement abonnement =
                    abonnementDAO.findById(
                            paiement.getIdAbonnement()
                    );

            if (abonnement instanceof AbonnementAvecEngagement) {

                total += abonnement.getMontantMensuel();
            }
        }

        return total;
    }
}