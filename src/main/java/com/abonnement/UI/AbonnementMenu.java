package com.abonnement.UI;

import com.abonnement.DAO.AbonnementDAO;
import com.abonnement.DAO.AbonnementDAOImpl;
import com.abonnement.entity.Abonnement;
import com.abonnement.entity.AbonnementAvecEngagement;
import com.abonnement.entity.AbonnementSansEngagement;
import com.abonnement.entity.StatutAbonnement;
import com.abonnement.service.AbonnementService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AbonnementMenu {

    private Scanner scanner;
    private AbonnementService abonnementService;

    public AbonnementMenu(Scanner scanner) {
        this.scanner = scanner;

        AbonnementDAO abonnementDAO = new AbonnementDAOImpl();
        this.abonnementService = new AbonnementService(abonnementDAO);
    }

    public void afficher() {

        int choix;

        do {

            System.out.println();
            System.out.println("======================================");
            System.out.println("       GESTION DES ABONNEMENTS");
            System.out.println("======================================");
            System.out.println("1. Créer un abonnements");
            System.out.println("2. Modifier un abonnement");
            System.out.println("3. Supprimer un abonnement");
            System.out.println("4. Résilier un abonnement");
            System.out.println("5. Lister tous les abonnements");
            System.out.println("6. Lister les abonnements actifs");
            System.out.println("7. Générer les échéances");
            System.out.println("0. Retour");
            System.out.println("======================================");
            System.out.print("Votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {

                case 1:
                    creerAbonnement();
                    break;

                case 2:
                    modifierAbonnement();
                    break;

                case 3:
                    supprimerAbonnement();
                    break;

                case 4:
                    resilierAbonnement();
                    break;

                case 5:
                    listerAbonnements();
                    break;

                case 6:
                    listerAbonnementsActifs();
                    break;

                case 7:
                    genererEcheances();
                    break;

                case 0:
                    System.out.println("Retour au menu principal...");
                    break;

                default:
                    System.out.println("Choix invalide.");
            }

        } while (choix != 0);
    }

    // =========================================================
    // 1. CREER UN ABONNEMENT
    // =========================================================

    private void creerAbonnement() {

        System.out.println();
        System.out.println("========== CREER UN ABONNEMENT ==========");

        System.out.print("Nom du service : ");
        String nomService = scanner.nextLine();

        System.out.print("Montant mensuel : ");
        double montantMensuel = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Date de début (YYYY-MM-DD) : ");
        LocalDate dateDebut = LocalDate.parse(scanner.nextLine());

        System.out.print("Date de fin (YYYY-MM-DD, vide si aucune) : ");
        String dateFinInput = scanner.nextLine();

        LocalDate dateFin = null;

        if (!dateFinInput.trim().isEmpty()) {
            dateFin = LocalDate.parse(dateFinInput);
        }

        System.out.println();
        System.out.println("Type d'abonnement :");
        System.out.println("1. Avec engagement");
        System.out.println("2. Sans engagement");
        System.out.print("Votre choix : ");

        int type = scanner.nextInt();
        scanner.nextLine();

        Abonnement abonnement;

        if (type == 1) {

            System.out.print("Durée d'engagement (mois) : ");
            int duree = scanner.nextInt();
            scanner.nextLine();

            abonnement = new AbonnementAvecEngagement(
                    nomService,
                    montantMensuel,
                    dateDebut,
                    dateFin,
                    StatutAbonnement.ACTIVE,
                    duree
            );

        } else if (type == 2) {

            abonnement = new AbonnementSansEngagement(
                    nomService,
                    montantMensuel,
                    dateDebut,
                    dateFin,
                    StatutAbonnement.ACTIVE
            );

        } else {

            System.out.println("Type d'abonnement invalide.");
            return;
        }

        abonnementService.ajouter(abonnement);

        System.out.println("Abonnement créé avec succès.");
        System.out.println("ID : " + abonnement.getId());
    }

    // =========================================================
    // 2. MODIFIER UN ABONNEMENT
    // =========================================================

    private void modifierAbonnement() {

        System.out.println();
        System.out.println("========== MODIFIER UN ABONNEMENT ==========");

        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine();

        Abonnement abonnement = abonnementService.findById(id);

        if (abonnement == null) {
            System.out.println("Abonnement introuvable.");
            return;
        }

        System.out.println();
        System.out.println("Abonnement actuel :");
        afficherAbonnement(abonnement);

        System.out.println();
        System.out.println("Nouveau nom du service : ");
        String nomService = scanner.nextLine();

        System.out.print("Nouveau montant mensuel : ");
        double montantMensuel = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Nouvelle date de début (YYYY-MM-DD) : ");
        LocalDate dateDebut = LocalDate.parse(scanner.nextLine());

        System.out.print("Nouvelle date de fin (YYYY-MM-DD, vide si aucune) : ");
        String dateFinInput = scanner.nextLine();

        LocalDate dateFin = null;

        if (!dateFinInput.trim().isEmpty()) {
            dateFin = LocalDate.parse(dateFinInput);
        }

        System.out.println();
        System.out.println("Statut :");
        System.out.println("1. ACTIVE");
        System.out.println("2. SUSPENDU");
        System.out.println("3. RESILIE");
        System.out.print("Votre choix : ");

        int statutChoix = scanner.nextInt();
        scanner.nextLine();

        StatutAbonnement statut;

        switch (statutChoix) {

            case 1:
                statut = StatutAbonnement.ACTIVE;
                break;

            case 2:
                statut = StatutAbonnement.SUSPENDU;
                break;

            case 3:
                statut = StatutAbonnement.RESILIE;
                break;

            default:
                System.out.println("Statut invalide.");
                return;
        }

        if (abonnement instanceof AbonnementAvecEngagement) {

            AbonnementAvecEngagement abonnementEngagement =
                    (AbonnementAvecEngagement) abonnement;

            System.out.print("Nouvelle durée d'engagement (mois) : ");
            int duree = scanner.nextInt();
            scanner.nextLine();

            abonnementEngagement.setNomService(nomService);
            abonnementEngagement.setMontantMensuel(montantMensuel);
            abonnementEngagement.setDateDebut(dateDebut);
            abonnementEngagement.setDateFin(dateFin);
            abonnementEngagement.setStatut(statut);
            abonnementEngagement.setDureeEngagementMois(duree);

        } else {

            abonnement.setNomService(nomService);
            abonnement.setMontantMensuel(montantMensuel);
            abonnement.setDateDebut(dateDebut);
            abonnement.setDateFin(dateFin);
            abonnement.setStatut(statut);
        }

        abonnementService.modifier(abonnement);

        System.out.println("Abonnement modifié avec succès.");
    }

    // =========================================================
    // 3. SUPPRIMER UN ABONNEMENT
    // =========================================================

    private void supprimerAbonnement() {

        System.out.println();
        System.out.println("========== SUPPRIMER UN ABONNEMENT ==========");

        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine();

        Abonnement abonnement = abonnementService.findById(id);

        if (abonnement == null) {
            System.out.println("Abonnement introuvable.");
            return;
        }

        afficherAbonnement(abonnement);

        System.out.print("Confirmer la suppression ? (oui/non) : ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("oui")) {

            abonnementService.supprimer(id);

            System.out.println("Abonnement supprimé avec succès.");

        } else {

            System.out.println("Suppression annulée.");
        }
    }

    // =========================================================
    // 4. RESILIER UN ABONNEMENT
    // =========================================================

    private void resilierAbonnement() {

        System.out.println();
        System.out.println("========== RESILIER UN ABONNEMENT ==========");

        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine();

        Abonnement abonnement = abonnementService.findById(id);

        if (abonnement == null) {
            System.out.println("Abonnement introuvable.");
            return;
        }

        afficherAbonnement(abonnement);

        System.out.print("Confirmer la résiliation ? (oui/non) : ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("oui")) {

            abonnementService.resilier(id);

        } else {

            System.out.println("Résiliation annulée.");
        }
    }

    // =========================================================
    // 5. LISTER TOUS LES ABONNEMENTS
    // =========================================================

    private void listerAbonnements() {

        System.out.println();
        System.out.println("========== TOUS LES ABONNEMENTS ==========");

        List<Abonnement> abonnements =
                abonnementService.findAll();

        if (abonnements.isEmpty()) {

            System.out.println("Aucun abonnement trouvé.");
            return;
        }

        for (Abonnement abonnement : abonnements) {

            afficherAbonnement(abonnement);

            System.out.println("--------------------------------------");
        }
    }

    // =========================================================
    // 6. LISTER LES ABONNEMENTS ACTIFS
    // =========================================================

    private void listerAbonnementsActifs() {

        System.out.println();
        System.out.println("========== ABONNEMENTS ACTIFS ==========");

        List<Abonnement> abonnements =
                abonnementService.findActifs();

        if (abonnements.isEmpty()) {

            System.out.println("Aucun abonnement actif.");
            return;
        }

        for (Abonnement abonnement : abonnements) {

            afficherAbonnement(abonnement);

            System.out.println("--------------------------------------");
        }
    }

    // =========================================================
    // 7. GENERER LES ECHEANCES
    // =========================================================

    private void genererEcheances() {

        System.out.println();
        System.out.println("========== GENERER LES ECHEANCES ==========");

        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine();

        abonnementService.genererEcheances(id);
    }

    // =========================================================
    // AFFICHER UN ABONNEMENT
    // =========================================================

    private void afficherAbonnement(Abonnement abonnement) {

        System.out.println();
        System.out.println("ID : " + abonnement.getId());
        System.out.println("Service : " + abonnement.getNomService());
        System.out.println("Montant mensuel : "
                + abonnement.getMontantMensuel() + " DH");

        System.out.println("Date début : "
                + abonnement.getDateDebut());

        System.out.println("Date fin : "
                + (abonnement.getDateFin() != null
                ? abonnement.getDateFin()
                : "Aucune"));

        System.out.println("Statut : "
                + abonnement.getStatut());

        if (abonnement instanceof AbonnementAvecEngagement) {

            AbonnementAvecEngagement abonnementEngagement =
                    (AbonnementAvecEngagement) abonnement;

            System.out.println(
                    "Type : AVEC ENGAGEMENT"
            );

            System.out.println(
                    "Durée engagement : "
                            + abonnementEngagement.getDureeEngagementMois()
                            + " mois"
            );

        } else {

            System.out.println(
                    "Type : SANS ENGAGEMENT"
            );
        }
    }
}