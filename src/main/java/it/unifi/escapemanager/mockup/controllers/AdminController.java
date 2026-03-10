package it.unifi.escapemanager.mockup.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML private TableView<String[]> roomTable;
    @FXML private Label selectedRoomLabel;
    @FXML private ComboBox<String> strategyCombo;
    @FXML private TextField priceField;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {

        // ---- Colonne ----
        TableColumn<String[], String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        idCol.setPrefWidth(60);

        TableColumn<String[], String> nameCol = new TableColumn<>("Stanza");
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        nameCol.setPrefWidth(180);

        TableColumn<String[], String> capCol = new TableColumn<>("Capienza");
        capCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        capCol.setPrefWidth(90);

        TableColumn<String[], String> stratCol = new TableColumn<>("Strategia Attuale");
        stratCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));
        stratCol.setPrefWidth(170);

        TableColumn<String[], String> priceCol = new TableColumn<>("Prezzo Base (€)");
        priceCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[4]));
        priceCol.setPrefWidth(120);

        roomTable.getColumns().addAll(idCol, nameCol, capCol, stratCol, priceCol);

        // ---- Dati dummy ----
        ObservableList<String[]> data = FXCollections.observableArrayList(
            new String[]{"FI01", "Horror Asylum",      "2-6", "Tariffa Base",      "20.00"},
            new String[]{"FI02", "Fuga da Alcatraz",   "3-8", "Tariffa Weekend",   "22.00"},
            new String[]{"FI03", "Bunker Sotterraneo", "2-5", "Tariffa Base",      "18.00"},
            new String[]{"FI04", "Sci-Fi Lab",         "2-4", "Sconto Studenti",   "15.00"}
        );
        roomTable.setItems(data);

        // ---- Strategie ----
        strategyCombo.getItems().addAll(
            "Tariffa Base", "Tariffa Weekend (+20%)",
            "Sconto Studenti (-15%)", "Tariffa Dinamica");
        strategyCombo.setValue("Tariffa Base");
        priceField.setText("20.00");

        // ---- Selezione riga → aggiorna form ----
        roomTable.getSelectionModel().selectedItemProperty()
                 .addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedRoomLabel.setText(newVal[0] + " — " + newVal[1]);
                strategyCombo.setValue(mapStrategy(newVal[3]));
                priceField.setText(newVal[4]);
            }
        });
        roomTable.getSelectionModel().selectFirst();
    }

    private String mapStrategy(String raw) {
        if (raw.contains("Weekend"))  return "Tariffa Weekend (+20%)";
        if (raw.contains("Studenti")) return "Sconto Studenti (-15%)";
        if (raw.contains("Dinamica")) return "Tariffa Dinamica";
        return "Tariffa Base";
    }
}
