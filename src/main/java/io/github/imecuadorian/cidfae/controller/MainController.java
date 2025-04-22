package io.github.imecuadorian.cidfae.controller;

import io.github.imecuadorian.cidfae.model.*;
import io.github.imecuadorian.cidfae.service.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;
import java.util.logging.*;

public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
    private final BatchService batchService;
    private final SpecimenService specimenService;
    private ObservableList<Batch> batchList;

    public MainController(BatchService batchService, SpecimenService specimenService) {
        this.batchService = batchService;
        this.specimenService = specimenService;
    }

    public ObservableList<Batch> getBatches() {
        if (batchList == null) {
            try {
                batchList = FXCollections.observableArrayList(batchService.getLast10Batches());
            } catch (Exception e) {
                LOGGER.severe("❌ Error fetching batches.");
                LOGGER.severe(e.getMessage());
                batchList = FXCollections.observableArrayList();
            }
        }
        return batchList;
    }

    public void loadBatchesIntoTable(TableView<Batch> batchTable) {
        try {
            List<Batch> batches = batchService.getLast10Batches();
            batchTable.getItems().clear();
            batchTable.getItems().addAll(batches);
        } catch (Exception e) {
            LOGGER.severe("❌ Error loading batches into table.");
            LOGGER.severe(e.getMessage());
        }
    }

    public void setupTableWithDetails(TableView<Batch> batchTable, List<Label> detailLabels) {
        batchTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> loadBatchDetails(selected, detailLabels));
    }

    public void loadBatchDetails(Batch selectedBatch, List<Label> detailLabels) {
        if (selectedBatch == null || detailLabels == null || detailLabels.size() < 10) return;

        detailLabels.get(0).setText(String.valueOf(selectedBatch.getBatchId()));
        detailLabels.get(1).setText(selectedBatch.getBatchTitle());
        detailLabels.get(2).setText(selectedBatch.getTestDate() != null ? selectedBatch.getTestDate().toString() : "Unavailable");
        detailLabels.get(3).setText(String.valueOf(selectedBatch.getFiberLayerCount()));
        detailLabels.get(4).setText(selectedBatch.getMaintenanceType());
        detailLabels.get(5).setText(selectedBatch.getOrientation());
        detailLabels.get(6).setText(selectedBatch.getTestType());
        detailLabels.get(7).setText(selectedBatch.getResinType());
        detailLabels.get(8).setText(selectedBatch.getFiberType());
        detailLabels.get(9).setText(selectedBatch.getCuringDetails());
    }

    public Optional<Specimen> getFirstSpecimenOfBatch(int batchId) {
        try {
            var specimens = specimenService.getSpecimensByBatchId(batchId);
            return specimens.isEmpty() ? Optional.empty() : Optional.of(specimens.getFirst());
        } catch (Exception e) {
            LOGGER.severe("❌ Error fetching specimens for batch ID: " + batchId);
            LOGGER.severe(e.getMessage());
            return Optional.empty();
        }
    }

    public void initDetailLabels(List<Label> labels, GridPane grid, String[] captions) {
        if (labels == null || captions == null) return;

        for (int i = 0; i < captions.length; i++) {
            Label label = new Label("");
            labels.add(label);
            grid.add(label, 1, i);
        }
    }
}
