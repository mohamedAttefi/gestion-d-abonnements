package com.abonnement.UI;

import com.abonnement.UI.AbonnementMenu;
import com.abonnement.UI.PaiementMenu;
import com.abonnement.UI.RapportMenu;

import java.util.Scanner;

public class Menu {

    private Scanner scanner;

    private AbonnementMenu abonnementMenu;
    private PaiementMenu paiementMenu;
    private RapportMenu rapportMenu;

    public Menu() {

        scanner = new Scanner(System.in);

        abonnementMenu = new AbonnementMenu(scanner);
        paiementMenu = new PaiementMenu(scanner);
        rapportMenu = new RapportMenu(scanner);
    }

    public void start() {

        int choix;

        do {

            afficherMenuPrincipal();

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {

                case 1:
                    abonnementMenu.afficher();
                    break;

                case 2:
                    paiementMenu.afficher();
                    break;

                case 3:
                    rapportMenu.afficher();
                    break;

                case 0:
                    System.out.println(
                            "\nAu revoir !"
                    );
                    break;

                default:
                    System.out.println(
                            "\nChoix invalide."
                    );
            }

        } while (choix != 0);
    }

    private void afficherMenuPrincipal() {

        System.out.println();
        System.out.println("======================================");
        System.out.println("       GESTION DES ABONNEMENTS");
        System.out.println("======================================");
        System.out.println("1. Gestion des abonnements");
        System.out.println("2. Gestion des paiements");
        System.out.println("3. Rapports financiers");
        System.out.println("0. Quitter");
        System.out.println("======================================");
        System.out.print("Votre choix : ");
    }
}