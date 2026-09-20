package com.abonnement.DAO;

import com.abonnement.databaseSetup.DatabaseSetup;
import com.abonnement.entity.Paiement;
import com.abonnement.entity.StatutPaiement;
import com.abonnement.entity.TypePaiement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAOImpl implements PaiementDAO {

    @Override
    public void ajouter(Paiement paiement) {

        String sql =
                "INSERT INTO paiement " +
                        "(id_paiement, id_abonnement, date_echeance, " +
                        "date_paiement, type_paiement, statut) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // ID paiement
            statement.setString(
                    1,
                    paiement.getIdPaiement()
            );

            // ID abonnement
            statement.setString(
                    2,
                    paiement.getIdAbonnement()
            );

            // Date échéance
            statement.setDate(
                    3,
                    Date.valueOf(paiement.getDateEcheance())
            );

            // Date paiement
            if (paiement.getDatePaiement() != null) {

                statement.setDate(
                        4,
                        Date.valueOf(paiement.getDatePaiement())
                );

            } else {

                statement.setNull(
                        4,
                        Types.DATE
                );
            }

            // Type paiement
            if (paiement.getTypePaiement() != null) {

                statement.setString(
                        5,
                        paiement.getTypePaiement().name()
                );

            } else {

                statement.setNull(
                        5,
                        Types.VARCHAR
                );
            }

            // Statut
            statement.setString(
                    6,
                    paiement.getStatut().name()
            );

            statement.executeUpdate();

            System.out.println(
                    "Paiement ajouté avec succès."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Erreur lors de l'ajout du paiement : "
                            + e.getMessage()
            );
        }
    }

    @Override
    public List<Paiement> findAll() {

        List<Paiement> paiements = new ArrayList<>();

        String sql = "SELECT * FROM paiement";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Paiement paiement =
                        convertirResultatEnPaiement(resultSet);

                paiements.add(paiement);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erreur lors de la récupération des paiements : "
                            + e.getMessage()
            );
        }

        return paiements;
    }

    @Override
    public Paiement findById(String idPaiement) {

        String sql =
                "SELECT * FROM paiement " +
                        "WHERE id_paiement = ?";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    idPaiement
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return convertirResultatEnPaiement(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erreur lors de la recherche du paiement : "
                            + e.getMessage()
            );
        }

        return null;
    }

    @Override
    public List<Paiement> findByAbonnementId(
            String idAbonnement) {

        List<Paiement> paiements = new ArrayList<>();

        String sql =
                "SELECT * FROM paiement " +
                        "WHERE id_abonnement = ? " +
                        "ORDER BY date_echeance";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    idAbonnement
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Paiement paiement =
                            convertirResultatEnPaiement(
                                    resultSet
                            );

                    paiements.add(paiement);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erreur lors de la récupération des paiements : "
                            + e.getMessage()
            );
        }

        return paiements;
    }

    @Override
    public void modifier(Paiement paiement) {

        String sql =
                "UPDATE paiement SET " +
                        "id_abonnement = ?, " +
                        "date_echeance = ?, " +
                        "date_paiement = ?, " +
                        "type_paiement = ?, " +
                        "statut = ? " +
                        "WHERE id_paiement = ?";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // ID abonnement
            statement.setString(
                    1,
                    paiement.getIdAbonnement()
            );

            // Date échéance
            statement.setDate(
                    2,
                    Date.valueOf(
                            paiement.getDateEcheance()
                    )
            );

            // Date paiement
            if (paiement.getDatePaiement() != null) {

                statement.setDate(
                        3,
                        Date.valueOf(
                                paiement.getDatePaiement()
                        )
                );

            } else {

                statement.setNull(
                        3,
                        Types.DATE
                );
            }

            // Type paiement
            if (paiement.getTypePaiement() != null) {

                statement.setString(
                        4,
                        paiement.getTypePaiement().name()
                );

            } else {

                statement.setNull(
                        4,
                        Types.VARCHAR
                );
            }

            // Statut
            statement.setString(
                    5,
                    paiement.getStatut().name()
            );

            // ID paiement
            statement.setString(
                    6,
                    paiement.getIdPaiement()
            );

            statement.executeUpdate();

            System.out.println(
                    "Paiement modifié avec succès."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Erreur lors de la modification : "
                            + e.getMessage()
            );
        }
    }

    @Override
    public void supprimer(String idPaiement) {

        String sql =
                "DELETE FROM paiement " +
                        "WHERE id_paiement = ?";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    idPaiement
            );

            statement.executeUpdate();

            System.out.println(
                    "Paiement supprimé avec succès."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Erreur lors de la suppression : "
                            + e.getMessage()
            );
        }
    }

    private Paiement convertirResultatEnPaiement(
            ResultSet resultSet) throws SQLException {

        String idPaiement =
                resultSet.getString("id_paiement");

        String idAbonnement =
                resultSet.getString("id_abonnement");

        Date dateEcheanceSQL =
                resultSet.getDate("date_echeance");

        Date datePaiementSQL =
                resultSet.getDate("date_paiement");

        String typePaiementSQL =
                resultSet.getString("type_paiement");

        String statutSQL =
                resultSet.getString("statut");

        java.time.LocalDate dateEcheance =
                dateEcheanceSQL.toLocalDate();

        java.time.LocalDate datePaiement = null;

        if (datePaiementSQL != null) {

            datePaiement =
                    datePaiementSQL.toLocalDate();
        }

        TypePaiement typePaiement = null;

        if (typePaiementSQL != null) {

            typePaiement =
                    TypePaiement.valueOf(
                            typePaiementSQL
                    );
        }

        StatutPaiement statut =
                StatutPaiement.valueOf(
                        statutSQL
                );

        Paiement paiement = new Paiement(
                idAbonnement,
                dateEcheance,
                datePaiement,
                typePaiement,
                statut
        );

        // Restore the original database UUID
        paiement.setIdPaiement(idPaiement);

        return paiement;
    }
}