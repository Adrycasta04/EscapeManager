package it.unifi.escapemanager.mockup.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CatalogoController implements Initializable {

    @FXML private ComboBox<String> sedeCombo;
    @FXML private ListView<String[]> roomListView;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> slotCombo;
    @FXML private Spinner<Integer> playerSpinner;
    @FXML private Label priceLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Sedi
        sedeCombo.getItems().addAll("Firenze", "Roma", "Milano");
        sedeCombo.setValue("Firenze");

        // Stanze dummy: [nome, difficoltà, capienza, prezzo unitario]
        roomListView.getItems().addAll(
            new String[]{"Horror Asylum",      "Difficoltà: Alta",  "Capienza: 2-6", "20.00"},
            new String[]{"Fuga da Alcatraz",   "Difficoltà: Media", "Capienza: 3-8", "22.00"},
            new String[]{"Sci-Fi Lab",         "Difficoltà: Bassa", "Capienza: 2-4", "15.00"},
            new String[]{"The Pirate Ship",    "Difficoltà: Media", "Capienza: 2-6", "18.00"}
        );

        roomListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox(2);
                    Label name = new Label(item[0]);
                    name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    Label detail = new Label(item[1] + "  |  " + item[2] + "  |  €" + item[3] + "/giocatore");
                    detail.setStyle("-fx-text-fill: #757575; -fx-font-size: 11px;");
                    vbox.getChildren().addAll(name, detail);
                    setGraphic(vbox);
                }
            }
        });
        roomListView.getSelectionModel().selectFirst();

        // Data
        datePicker.setValue(LocalDate.of(2026, 10, 31));

        // Slot orari
        slotCombo.getItems().addAll(
            "10:00 – 11:00", "12:00 – 13:00", "14:00 – 15:00",
            "16:00 – 17:00", "18:00 – 19:00", "20:00 – 21:00", "21:00 – 22:00");
        slotCombo.setValue("21:00 – 22:00");

        // Spinner giocatori
        playerSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 8, 4));

        // Aggiornamento prezzo dinamico
        updatePrice();
        playerSpinner.valueProperty().addListener((obs, o, n) -> updatePrice());
        roomListView.getSelectionModel().selectedItemProperty()
                     .addListener((obs, o, n) -> updatePrice());
    }

    private void updatePrice() {
        String[] selected = roomListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            double base = Double.parseDouble(selected[3]);
            int players = playerSpinner.getValue();
            priceLabel.setText(String.format("€ %.2f", base * players));
        }
    }
}
