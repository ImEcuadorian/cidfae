package io.github.imecuadorian.cidfae.controller.view;

import io.github.imecuadorian.cidfae.model.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainViewController {
    @FXML
    private TableView<Batch> batchTable;
    @FXML private TableColumn<Batch, String> colOrder;
    @FXML private TableColumn<Batch, String> colTitle;
    @FXML private GridPane detailGrid;
}

