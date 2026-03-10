package it.unifi.escapemanager.mockup.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardGMController implements Initializable {

    @FXML private FlowPane roomContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addRoomCard("Horror Asylum",
            "Difficoltà: Alta  |  Max: 6",
            "DISPONIBILE", "badge-disponibile",
            "Avvia Sessione", "btn-success");

        addRoomCard("Fuga da Alcatraz",
            "Difficoltà: Media  |  Max: 8",
            "IN CORSO", "badge-incorso",
            "Termina Sessione", "btn-danger");

        addRoomCard("Bunker Sotterraneo",
            "Difficoltà: Alta  |  Max: 5",
            "IN PULIZIA", "badge-inpulizia",
            "Rimetti Disponibile", "btn-warning");

        addRoomCard("Sci-Fi Lab",
            "Difficoltà: Bassa  |  Max: 4",
            "DISPONIBILE", "badge-disponibile",
            "Avvia Sessione", "btn-success");

        addRoomCard("The Pirate Ship",
            "Difficoltà: Media  |  Max: 6",
            "IN MANUTENZIONE", "badge-manutenzione",
            "Risolvi Guasto", "btn-secondary");

        addRoomCard("Tomb of Pharaoh",
            "Difficoltà: Alta  |  Max: 6",
            "IN CORSO", "badge-incorso",
            "Termina Sessione", "btn-danger");
    }

    private void addRoomCard(String name, String detail,
                             String status, String badgeCss,
                             String actionText, String btnCss) {

        VBox card = new VBox(10);
        card.getStyleClass().add("room-card");
        card.setAlignment(Pos.TOP_LEFT);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("room-name");

        Label detailLabel = new Label(detail);
        detailLabel.getStyleClass().add("room-detail");

        Label badge = new Label(status);
        badge.getStyleClass().add(badgeCss);

        Region spacer = new Region();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Button actionBtn = new Button(actionText);
        actionBtn.getStyleClass().add(btnCss);
        actionBtn.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(nameLabel, detailLabel, badge, spacer, actionBtn);
        roomContainer.getChildren().add(card);
    }
}
