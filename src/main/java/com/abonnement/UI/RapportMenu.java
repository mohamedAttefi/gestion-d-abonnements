package com.abonnement.UI;

import com.abonnement.DAO.AbonnementDAO;
import com.abonnement.DAO.AbonnementDAOImpl;
import com.abonnement.DAO.PaiementDAO;
import com.abonnement.DAO.PaiementDAOImpl;
import com.abonnement.service.RapportService;

import java.util.Scanner;

public class RapportMenu {

    private Scanner scanner;
    private RapportService rapportService;

    public RapportMenu(Scanner scanner) {

        this.scanner = scanner;

        AbonnementDAO abonnementDAO =
                new AbonnementDAOImpl();

        PaiementDAO paiementDAO =
                new PaiementDAOImpl();

        this.rapportService =
                new RapportService(
                        abonnementDAO,
                        paiementDAO
                );
    }

    public void afficher() {

        int choix;

        do {

            System.out.println();
            System.out.println("======================================");
            System.out.println("          RAPPORTS FINANCIERS");
            System.out.println("======================================");
            System.out.println("1. Rapport mensuel");
            System.out.println("2. Rapport annuel");
            System.out.println("3. Rapport des impayés");
            System.out.println("4. Résumé financier");
            System.out.println("0. Retour");
            System.out.println("======================================");
            System.out.print("Votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {

                case 1:
                    rapportMensuel();
                    break;

                case 2:
                    rapportAnnuel();
                    break;

                case 3:
                    rapportImpayes();
                    break;

                case 4:
                    resumeFinancier();
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
    // 1. RAPPORT MENSUEL
    // =========================================================

    private void rapportMensuel() {

        System.out.println();
        System.out.println("========== RAPPORT MENSUEL ==========");

        System.out.print("Mois (1-12) : ");
        int mois = scanner.nextInt();

        System.out.print("Année : ");
        int annee = scanner.nextInt();

        scanner.nextLine();

        if (mois < 1 || mois > 12) {

            System.out.println("Mois invalide.");
            return;
        }

        rapportService.rapportMensuel(
                mois,
                annee
        );
    }

    // =========================================================
    // 2. RAPPORT ANNUEL
    // =========================================================

    private void rapportAnnuel() {

        System.out.println();
        System.out.println("========== RAPPORT ANNUEL ==========");

        System.out.print("Année : ");
        int annee = scanner.nextInt();

        scanner.nextLine();

        rapportService.rapportAnnuel(annee);
    }

    // =========================================================
    // 3. RAPPORT IMPAYES
    // =========================================================

    private void rapportImpayes() {

        rapportService.rapportImpayes();
    }

    // =========================================================
    // 4. RESUME FINANCIER
    // =========================================================

    private void resumeFinancier() {

        System.out.println();
        System.out.println("======================================");
        System.out.println("          RÉSUMÉ FINANCIER");
        System.out.println("======================================");

        long actifs =
                rapportService.nombreAbonnementsActifs();

        double revenu =
                rapportService.revenuMensuelTheorique();

        System.out.println(
                "Abonnements actifs : " + actifs
        );

        System.out.println(
                "Revenu mensuel théorique : "
                        + revenu + " DH"
        );

        System.out.println("======================================");
    }
}