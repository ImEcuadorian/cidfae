package io.github.imecuadorian.cidfae.repository.impl;

import io.github.imecuadorian.cidfae.connection.DatabaseConnection;
import io.github.imecuadorian.cidfae.model.Batch;
import io.github.imecuadorian.cidfae.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class BatchRepository implements Repository<String, Batch> {

    private static final Logger LOGGER = Logger.getLogger(BatchRepository.class.getName());

    @Override
    public boolean save(Batch batch) {
        String sql = """
                INSERT INTO Batches (
                    work_order, batch_title, test_date,
                    maintenance_type, test_type, fiber_layer_count,
                    orientation, resin_type, fiber_type, curing_details
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (var conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, batch.getWorkOrder());
            preparedStatement.setString(2, batch.getBatchTitle());
            preparedStatement.setDate(3, Date.valueOf(batch.getTestDate()));
            preparedStatement.setString(4, batch.getMaintenanceType());
            preparedStatement.setString(5, batch.getTestType());
            preparedStatement.setInt(6, batch.getFiberLayerCount());
            preparedStatement.setString(7, batch.getOrientation());
            preparedStatement.setString(8, batch.getResinType());
            preparedStatement.setString(9, batch.getFiberType());
            preparedStatement.setString(10, batch.getCuringDetails());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.severe("❌ Error while saving batch: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Batch> findAll() {
        List<Batch> list = new ArrayList<>();
        String sql = """
        SELECT batch_id, work_order, batch_title, test_date, 
               fiber_layer_count, maintenance_type, orientation,
               test_type, resin_type, fiber_type, curing_details, specimen_count
        FROM Batches
        """;

        try (var conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(getBatch(resultSet));
            }

        } catch (SQLException e) {
            LOGGER.severe("❌ Error while fetching all batches: " + e.getMessage());
        }

        return list;
    }

    @Override
    public Optional<Batch> findByKey(String batchTitle) {
        String sql = """
        SELECT batch_id, work_order, batch_title, test_date, 
               fiber_layer_count, maintenance_type, orientation,
               test_type, resin_type, fiber_type, curing_details, specimen_count
        FROM Batches
        WHERE batch_title = ?
        """;

        try (var conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, batchTitle);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(getBatch(resultSet));
            }

        } catch (SQLException e) {
            LOGGER.severe("❌ Error while finding batch by title: " + e.getMessage());
        }

        return Optional.empty();
    }


    public List<Batch> findLast10() {
        List<Batch> list = new ArrayList<>();
        String sql = "SELECT work_order, batch_title FROM Batches ORDER BY batch_id DESC LIMIT 10";

        try (var conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                list.add(new Batch(
                        resultSet.getString("work_order"),
                        resultSet.getString("batch_title")
                ));
            }

        } catch (SQLException e) {
            LOGGER.severe("❌ Error while fetching last 10 batches: " + e.getMessage());
        }

        return list;
    }

    private Batch getBatch(ResultSet resultSet) throws SQLException {
        Batch batch = new Batch();
        batch.setBatchId(resultSet.getInt("batch_id"));
        batch.setWorkOrder(resultSet.getString("work_order"));
        batch.setBatchTitle(resultSet.getString("batch_title"));
        batch.setTestDate(resultSet.getDate("test_date").toLocalDate());
        batch.setMaintenanceType(resultSet.getString("maintenance_type"));
        batch.setTestType(resultSet.getString("test_type"));
        batch.setFiberLayerCount(resultSet.getInt("fiber_layer_count"));
        batch.setOrientation(resultSet.getString("orientation"));
        batch.setResinType(resultSet.getString("resin_type"));
        batch.setFiberType(resultSet.getString("fiber_type"));
        batch.setCuringDetails(resultSet.getString("curing_details"));
        return batch;
    }
}
