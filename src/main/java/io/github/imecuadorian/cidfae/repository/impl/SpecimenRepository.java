package io.github.imecuadorian.cidfae.repository.impl;

import io.github.imecuadorian.cidfae.connection.DatabaseConnection;
import io.github.imecuadorian.cidfae.model.Specimen;
import io.github.imecuadorian.cidfae.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class SpecimenRepository implements Repository<Integer, Specimen> {

    private static final Logger LOGGER = Logger.getLogger(SpecimenRepository.class.getName());

    private static final String COL_SPECIMEN_ID = "specimen_id";
    private static final String COL_BATCH_ID = "batch_id";
    private static final String COL_MAX_FORCE = "max_force";
    private static final String COL_ELASTIC_MODULUS = "elastic_modulus";

    @Override
    public boolean save(Specimen specimen) {
        String sql = """
            INSERT INTO Specimens (batch_id, max_force, elastic_modulus)
            VALUES (?, ?, ?)
        """;

        try (var conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, specimen.getBatchId());
            preparedStatement.setDouble(2, specimen.getMaxForce());
            preparedStatement.setDouble(3, specimen.getElasticModulus());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("❌ Error while saving specimen: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Specimen> findByKey(Integer specimenId) {
        String sql = """
            SELECT specimen_id, batch_id, max_force, elastic_modulus
            FROM Specimens
            WHERE specimen_id = ?
        """;

        try (var conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, specimenId);
            var rs = preparedStatement.executeQuery();

            if (rs.next()) {
                var specimen = new Specimen(
                        rs.getInt(COL_SPECIMEN_ID),
                        rs.getDouble(COL_MAX_FORCE),
                        rs.getDouble(COL_ELASTIC_MODULUS),
                        rs.getInt(COL_BATCH_ID)
                );
                return Optional.of(specimen);
            }

        } catch (SQLException e) {
            LOGGER.severe("❌ Error while finding specimen by ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Specimen> findAll() {
        List<Specimen> list = new ArrayList<>();
        String sql = """
            SELECT specimen_id, batch_id, max_force, elastic_modulus
            FROM Specimens
        """;

        try (var conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new Specimen(
                        resultSet.getInt(COL_SPECIMEN_ID),
                        resultSet.getDouble(COL_MAX_FORCE),
                        resultSet.getDouble(COL_ELASTIC_MODULUS),
                        resultSet.getInt(COL_BATCH_ID)
                ));
            }

        } catch (SQLException e) {
            LOGGER.severe("❌ Error while fetching all specimens: " + e.getMessage());
        }

        return list;
    }

    public List<Specimen> findByBatchId(int batchId) {
        List<Specimen> list = new ArrayList<>();
        String sql = """
            SELECT specimen_id, batch_id, max_force, elastic_modulus
            FROM Specimens
            WHERE batch_id = ?
        """;

        try (var conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, batchId);
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(new Specimen(
                        resultSet.getInt(COL_SPECIMEN_ID),
                        resultSet.getDouble(COL_MAX_FORCE),
                        resultSet.getDouble(COL_ELASTIC_MODULUS),
                        resultSet.getInt(COL_BATCH_ID)
                ));
            }

        } catch (SQLException e) {
            LOGGER.severe("❌ Error while fetching specimens by batch ID: " + e.getMessage());
        }

        return list;
    }

    public boolean update(Specimen specimen) {
        String sql = """
            UPDATE Specimens
            SET max_force = ?, elastic_modulus = ?
            WHERE specimen_id = ?
        """;

        try (var conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setDouble(1, specimen.getMaxForce());
            preparedStatement.setDouble(2, specimen.getElasticModulus());
            preparedStatement.setInt(3, specimen.getSpecimenId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("❌ Error while updating specimen: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int specimenId) {
        String sql = "DELETE FROM Specimens WHERE specimen_id = ?";

        try (var conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, specimenId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("❌ Error while deleting specimen: " + e.getMessage());
            return false;
        }
    }
}
