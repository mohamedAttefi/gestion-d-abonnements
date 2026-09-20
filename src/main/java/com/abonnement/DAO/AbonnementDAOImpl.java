package com.abonnement.DAO;

import com.abonnement.databaseSetup.DatabaseSetup;
import com.abonnement.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDAOImpl implements AbonnementDAO {

    @Override
    public void ajouter(Abonnement abonnement) {

        String sql = "INSERT INTO abonnement " +
                "(id, nom_service, montant_mensuel, date_debut, date_fin, statut, " +
                "type_abonnement, duree_engagement_mois) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, abonnement.getId());
            statement.setString(2, abonnement.getNomService());
            statement.setDouble(3, abonnement.getMontantMensuel());
            statement.setDate(4, Date.valueOf(abonnement.getDateDebut()));

            if (abonnement.getDateFin() != null) {
                statement.setDate(5, Date.valueOf(abonnement.getDateFin()));
            } else {
                statement.setNull(5, Types.DATE);
            }

            statement.setString(6, abonnement.getStatut().name());

            if (abonnement instanceof AbonnementAvecEngagement) {

                statement.setString(7, "AVEC_ENGAGEMENT");

                AbonnementAvecEngagement abonnementEngagement =
                        (AbonnementAvecEngagement) abonnement;

                statement.setInt(
                        8,
                        abonnementEngagement.getDureeEngagementMois()
                );

            } else {

                statement.setString(7, "SANS_ENGAGEMENT");
                statement.setNull(8, Types.INTEGER);
            }

            statement.executeUpdate();

            System.out.println("Abonnement ajouté avec succès.");

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage() +"/n"+e.getStackTrace());
        }
    }

    @Override
    public List<Abonnement> findAll() {

        List<Abonnement> abonnements = new ArrayList<>();

        String sql = "SELECT * FROM abonnement";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Abonnement abonnement = convertirResultatEnAbonnement(resultSet);

                abonnements.add(abonnement);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération : " + e.getMessage());
        }

        return abonnements;
    }

    @Override
    public Abonnement findById(String id) {

        String sql = "SELECT * FROM abonnement WHERE id = ?";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return convertirResultatEnAbonnement(resultSet);
            }

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        return null;
    }

    @Override
    public void modifier(Abonnement abonnement) {

        String sql = "UPDATE abonnement SET " +
                "nom_service = ?, " +
                "montant_mensuel = ?, " +
                "date_debut = ?, " +
                "date_fin = ?, " +
                "statut = ?, " +
                "type_abonnement = ?, " +
                "duree_engagement_mois = ? " +
                "WHERE id = ?";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, abonnement.getNomService());
            statement.setDouble(2, abonnement.getMontantMensuel());
            statement.setDate(3, Date.valueOf(abonnement.getDateDebut()));

            if (abonnement.getDateFin() != null) {
                statement.setDate(4, Date.valueOf(abonnement.getDateFin()));
            } else {
                statement.setNull(4, Types.DATE);
            }

            statement.setString(5, abonnement.getStatut().name());

            if (abonnement instanceof AbonnementAvecEngagement) {

                statement.setString(6, "AVEC_ENGAGEMENT");

                AbonnementAvecEngagement a =
                        (AbonnementAvecEngagement) abonnement;

                statement.setInt(7, a.getDureeEngagementMois());

            } else {

                statement.setString(6, "SANS_ENGAGEMENT");
                statement.setNull(7, Types.INTEGER);
            }

            statement.setString(8, abonnement.getId());

            statement.executeUpdate();

            System.out.println("Abonnement modifié avec succès.");

        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(String id) {

        String sql = "DELETE FROM abonnement WHERE id = ?";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            statement.executeUpdate();

            System.out.println("Abonnement supprimé avec succès.");

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    private Abonnement convertirResultatEnAbonnement(ResultSet resultSet)
            throws SQLException {

        String id = resultSet.getString("id");
        String nomService = resultSet.getString("nom_service");
        double montant = resultSet.getDouble("montant_mensuel");

        Date dateDebutSQL = resultSet.getDate("date_debut");
        Date dateFinSQL = resultSet.getDate("date_fin");

        java.time.LocalDate dateDebut = dateDebutSQL.toLocalDate();

        java.time.LocalDate dateFin =
                dateFinSQL != null ? dateFinSQL.toLocalDate() : null;

        StatutAbonnement statut =
                StatutAbonnement.valueOf(resultSet.getString("statut"));

        String type =
                resultSet.getString("type_abonnement");

        Abonnement abonnement;

        if (type.equals("AVEC_ENGAGEMENT")) {

            int duree =
                    resultSet.getInt("duree_engagement_mois");

            abonnement = new AbonnementAvecEngagement(
                    nomService,
                    montant,
                    dateDebut,
                    dateFin,
                    statut,
                    duree
            );

        } else {

            abonnement = new AbonnementSansEngagement(
                    nomService,
                    montant,
                    dateDebut,
                    dateFin,
                    statut
            );
        }

        return abonnement;
    }
}