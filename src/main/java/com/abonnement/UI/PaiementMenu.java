package com.abonnement.UI;

import com.abonnement.DAO.AbonnementDAO;
import com.abonnement.DAO.AbonnementDAOImpl;
import com.abonnement.DAO.PaiementDAO;
import com.abonnement.DAO.PaiementDAOImpl;
import com.abonnement.entity.Abonnement;
import com.abonnement.entity.Paiement;
import com.abonnement.entity.StatutPaiement;
import com.abonnement.entity.TypePaiement;
import com.abonnement.service.PaiementService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class PaiementMenu {

    private Scanner scanner;
    private PaiementService paiementService;

    public PaiementMenu(Scanner scanner) {

        this.scanner = scanner;

        PaiementDAO paiementDAO = new PaiementDAOImpl();
        AbonnementDAO abonnementDAO = new AbonnementDAOImpl();

        this.paiementService =
                new PaiementService(paiementDAO, abonnementDAO);
    }

    public void afficher() {

        int choix;

        do {

            System.out.println();
            System.out.println("======================================");
            System.out.println("          GESTION DES PAIEMENTS");
            System.out.println("======================================");
            System.out.println("1. Enregistrer un paiement");
            System.out.println("2. Modifier un paiement");
            System.out.println("3. Supprimer un paiement");
            System.out.println("4. Afficher paiements d'un abonnement");
            System.out.println("5. Afficher paiements impayés");
            System.out.println("6. Afficher paiements réglés");
            System.out.println("7. Afficher les 5 derniers paiements");
            System.out.println("8. Vérifier les paiements en retard");
            System.out.println("0. Retour");
            System.out.println("======================================");
            System.out.print("Votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {

                case 1:
                    enregistrerPaiement();
                    break;

                case 2:
                    modifierPaiement();
                    break;

                case 3:
                    supprimerPaiement();
                    break;

                case 4:
                    afficherPaiementsAbonnement();
                    break;

                case 5:
                    afficherImpayes();
                    break;

                case 6:
                    afficherPayes();
                    break;

                case 7:
                    afficher5Derniers();
                    break;

                case 8:
                    paiementService.verifierRetards();
                    break;

                case 0:
                    System.out.println("Retour...");
                    break;

                default:
                    System.out.println("Choix invalide.");
            }

        } while (choix != 0);
    }

    // =========================================================
    // 1. ENREGISTRER
    // =========================================================

    private void enregistrerPaiement() {

        System.out.println();
        System.out.println("========== ENREGISTRER UN PAIEMENT ==========");

        System.out.print("ID abonnement : ");
        String idAbonnement = scanner.nextLine();

        System.out.print("Date échéance (YYYY-MM-DD) : ");
        LocalDate dateEcheance =
                LocalDate.parse(scanner.nextLine());

        System.out.print("Date paiement (YYYY-MM-DD, vide si non payé) : ");
        String datePaiementInput = scanner.nextLine();

        LocalDate datePaiement = null;

        if (!datePaiementInput.trim().isEmpty()) {
            datePaiement =
                    LocalDate.parse(datePaiementInput);
        }

        TypePaiement typePaiement = null;

        if (datePaiement != null) {

            System.out.println();
            System.out.println("Type de paiement :");
            System.out.println("1. CARTE");
            System.out.println("2. VIREMENT");
            System.out.println("3. ESPECES");
            System.out.print("Votre choix : ");

            int choixType = scanner.nextInt();
            scanner.nextLine();

            switch (choixType) {

                case 1:
                    typePaiement = TypePaiement.CARTE;
                    break;

                case 2:
                    typePaiement = TypePaiement.VIREMENT;
                    break;

                case 3:
                    typePaiement = TypePaiement.ESPECES;
                    break;

                default:
                    System.out.println("Type invalide.");
                    return;
            }
        }

        StatutPaiement statut;

        if (datePaiement == null) {
            statut = StatutPaiement.NON_PAYE;
        } else if (datePaiement.isAfter(dateEcheance)) {
            statut = StatutPaiement.EN_RETARD;
        } else {
            statut = StatutPaiement.PAYE;
        }

        Paiement paiement = new Paiement(
                idAbonnement,
                dateEcheance,
                datePaiement,
                typePaiement,
                statut
        );

        paiementService.ajouter(paiement);

        System.out.println("ID paiement : "
                + paiement.getIdPaiement());
    }

    // =========================================================
    // 2. MODIFIER
    // =========================================================

    private void modifierPaiement() {

        System.out.println();
        System.out.println("========== MODIFIER UN PAIEMENT ==========");

        System.out.print("ID paiement : ");
        String id = scanner.nextLine();

        Paiement paiement =
                paiementService.findById(id);

        if (paiement == null) {
            System.out.println("Paiement introuvable.");
            return;
        }

        afficherPaiement(paiement);

        System.out.print("Nouvelle date échéance (YYYY-MM-DD) : ");
        LocalDate dateEcheance =
                LocalDate.parse(scanner.nextLine());

        System.out.print("Nouvelle date paiement (YYYY-MM-DD, vide si non payé) : ");
        String datePaiementInput = scanner.nextLine();

        LocalDate datePaiement = null;

        if (!datePaiementInput.trim().isEmpty()) {
            datePaiement =
                    LocalDate.parse(datePaiementInput);
        }

        TypePaiement typePaiement = null;

        if (datePaiement != null) {

            System.out.println("1. CARTE");
            System.out.println("2. VIREMENT");
            System.out.println("3. ESPECES");
            System.out.print("Type : ");

            int choixType = scanner.nextInt();
            scanner.nextLine();

            switch (choixType) {

                case 1:
                    typePaiement = TypePaiement.CARTE;
                    break;

                case 2:
                    typePaiement = TypePaiement.VIREMENT;
                    break;

                case 3:
                    typePaiement = TypePaiement.ESPECES;
                    break;

                default:
                    System.out.println("Type invalide.");
                    return;
            }
        }

        StatutPaiement statut;

        if (datePaiement == null) {
            statut = StatutPaiement.NON_PAYE;
        } else if (datePaiement.isAfter(dateEcheance)) {
            statut = StatutPaiement.EN_RETARD;
        } else {
            statut = StatutPaiement.PAYE;
        }

        paiement.setDateEcheance(dateEcheance);
        paiement.setDatePaiement(datePaiement);
        paiement.setTypePaiement(typePaiement);
        paiement.setStatut(statut);

        paiementService.modifier(paiement);
    }

    // =========================================================
    // 3. SUPPRIMER
    // =========================================================

    private void supprimerPaiement() {

        System.out.println();
        System.out.println("========== SUPPRIMER UN PAIEMENT ==========");

        System.out.print("ID paiement : ");
        String id = scanner.nextLine();

        Paiement paiement =
                paiementService.findById(id);

        if (paiement == null) {
            System.out.println("Paiement introuvable.");
            return;
        }

        afficherPaiement(paiement);

        System.out.print("Confirmer ? (oui/non) : ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("oui")) {
            paiementService.supprimer(id);
        } else {
            System.out.println("Suppression annulée.");
        }
    }

    // =========================================================
    // 4. PAIEMENTS D'UN ABONNEMENT
    // =========================================================

    private void afficherPaiementsAbonnement() {

        System.out.println();
        System.out.println("========== PAIEMENTS D'UN ABONNEMENT ==========");

        System.out.print("ID abonnement : ");
        String id = scanner.nextLine();

        List<Paiement> paiements =
                paiementService.findByAbonnementId(id);

        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement trouvé.");
            return;
        }

        for (Paiement paiement : paiements) {
            afficherPaiement(paiement);
            System.out.println("--------------------------------------");
        }
    }

    // =========================================================
    // 5. IMPAYES
    // =========================================================

    private void afficherImpayes() {

        System.out.println();
        System.out.println("========== PAIEMENTS IMPAYÉS ==========");

        List<Paiement> paiements =
                paiementService.findImpayes();

        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement impayé.");
            return;
        }

        for (Paiement paiement : paiements) {
            afficherPaiement(paiement);
            System.out.println("--------------------------------------");
        }

        System.out.println(
                "Total impayé estimé : "
                        + paiementService.calculerTotalImpayes()
                        + " DH"
        );
    }

    // =========================================================
    // 6. PAYES
    // =========================================================

    private void afficherPayes() {

        System.out.println();
        System.out.println("========== PAIEMENTS RÉGLÉS ==========");

        List<Paiement> paiements =
                paiementService.findPayes();

        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement réglé.");
            return;
        }

        for (Paiement paiement : paiements) {
            afficherPaiement(paiement);
            System.out.println("--------------------------------------");
        }
    }

    // =========================================================
    // 7. 5 DERNIERS
    // =========================================================

    private void afficher5Derniers() {

        System.out.println();
        System.out.println("========== 5 DERNIERS PAIEMENTS ==========");

        List<Paiement> paiements =
                paiementService.find5Derniers();

        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement.");
            return;
        }

        for (Paiement paiement : paiements) {
            afficherPaiement(paiement);
            System.out.println("--------------------------------------");
        }
    }

    // =========================================================
    // AFFICHER PAIEMENT
    // =========================================================

    private void afficherPaiement(Paiement paiement) {

        System.out.println();
        System.out.println("ID paiement : "
                + paiement.getIdPaiement());

        System.out.println("ID abonnement : "
                + paiement.getIdAbonnement());

        System.out.println("Date échéance : "
                + paiement.getDateEcheance());

        System.out.println("Date paiement : "
                + (paiement.getDatePaiement() != null
                ? paiement.getDatePaiement()
                : "Non payé"));

        System.out.println("Type paiement : "
                + (paiement.getTypePaiement() != null
                ? paiement.getTypePaiement()
                : "Aucun"));

        System.out.println("Statut : "
                + paiement.getStatut());
    }
}