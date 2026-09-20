package com.abonnement.service;

import com.abonnement.DAO.AbonnementDAO;
import com.abonnement.DAO.PaiementDAO;
import com.abonnement.entity.Abonnement;
import com.abonnement.entity.Paiement;
import com.abonnement.entity.StatutAbonnement;
import com.abonnement.entity.StatutPaiement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class RapportService {

    private AbonnementDAO abonnementDAO;
    private PaiementDAO paiementDAO;

    public RapportService(AbonnementDAO abonnementDAO,
                          PaiementDAO paiementDAO) {

        this.abonnementDAO = abonnementDAO;
        this.paiementDAO = paiementDAO;
    }

    // =========================================================
    // RAPPORT MENSUEL
    // =========================================================

    public void rapportMensuel(int mois, int annee) {

        YearMonth moisRecherche =
                YearMonth.of(annee, mois);

        List<Paiement> paiements =
                paiementDAO.findAll()
                        .stream()
                        .filter(p -> p.getDatePaiement() != null)
                        .filter(p ->
                                YearMonth.from(
                                        p.getDatePaiement()
                                ).equals(moisRecherche)
                        )
                        .filter(p ->
                                p.getStatut() == StatutPaiement.PAYE
                        )
                        .collect(Collectors.toList());

        double total = calculerMontant(paiements);

        System.out.println();
        System.out.println("======================================");
        System.out.println("          RAPPORT MENSUEL");
        System.out.println("======================================");
        System.out.println("Mois : " + mois + "/" + annee);
        System.out.println("Nombre de paiements : "
                + paiements.size());
        System.out.println("Montant total estimé : "
                + total + " DH");
        System.out.println("======================================");
    }

    // =========================================================
    // RAPPORT ANNUEL
    // =========================================================

    public void rapportAnnuel(int annee) {

        List<Paiement> paiements =
                paiementDAO.findAll()
                        .stream()
                        .filter(p -> p.getDatePaiement() != null)
                        .filter(p ->
                                p.getDatePaiement().getYear()
                                        == annee
                        )
                        .filter(p ->
                                p.getStatut() == StatutPaiement.PAYE
                        )
                        .collect(Collectors.toList());

        double total = calculerMontant(paiements);

        System.out.println();
        System.out.println("======================================");
        System.out.println("           RAPPORT ANNUEL");
        System.out.println("======================================");
        System.out.println("Année : " + annee);
        System.out.println("Nombre de paiements : "
                + paiements.size());
        System.out.println("Montant total estimé : "
                + total + " DH");
        System.out.println("======================================");
    }

    // =========================================================
    // RAPPORT IMPAYES
    // =========================================================

    public void rapportImpayes() {

        LocalDate aujourdHui = LocalDate.now();

        List<Paiement> impayes =
                paiementDAO.findAll()
                        .stream()
                        .filter(p ->
                                p.getStatut() == StatutPaiement.NON_PAYE
                                        ||
                                        p.getStatut() == StatutPaiement.EN_RETARD
                        )
                        .collect(Collectors.toList());

        double total = calculerMontant(impayes);

        System.out.println();
        System.out.println("======================================");
        System.out.println("           RAPPORT IMPAYÉS");
        System.out.println("======================================");

        System.out.println("Date : " + aujourdHui);

        System.out.println("Nombre d'impayés : "
                + impayes.size());

        System.out.println("Montant total impayé estimé : "
                + total + " DH");

        System.out.println();
        System.out.println("Détail :");

        for (Paiement paiement : impayes) {

            Abonnement abonnement =
                    abonnementDAO.findById(
                            paiement.getIdAbonnement()
                    );

            System.out.println("--------------------------------------");

            System.out.println(
                    "Paiement : "
                            + paiement.getIdPaiement()
            );

            if (abonnement != null) {

                System.out.println(
                        "Service : "
                                + abonnement.getNomService()
                );

                System.out.println(
                        "Montant mensuel : "
                                + abonnement.getMontantMensuel()
                                + " DH"
                );
            }

            System.out.println(
                    "Échéance : "
                            + paiement.getDateEcheance()
            );

            System.out.println(
                    "Statut : "
                            + paiement.getStatut()
            );
        }

        System.out.println("======================================");
    }

    // =========================================================
    // CALCULER LE MONTANT
    // =========================================================

    private double calculerMontant(List<Paiement> paiements) {

        double total = 0;

        for (Paiement paiement : paiements) {

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
    // NOMBRE D'ABONNEMENTS ACTIFS
    // =========================================================

    public long nombreAbonnementsActifs() {

        return abonnementDAO.findAll()
                .stream()
                .filter(a ->
                        a.getStatut()
                                == StatutAbonnement.ACTIVE
                )
                .count();
    }

    // =========================================================
    // REVENU MENSUEL THEORIQUE
    // =========================================================

    public double revenuMensuelTheorique() {

        return abonnementDAO.findAll()
                .stream()
                .filter(a ->
                        a.getStatut()
                                == StatutAbonnement.ACTIVE
                )
                .mapToDouble(
                        Abonnement::getMontantMensuel
                )
                .sum();
    }
}