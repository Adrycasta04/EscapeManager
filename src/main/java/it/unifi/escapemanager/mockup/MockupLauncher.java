package it.unifi.escapemanager.mockup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launcher per i mockup JavaFX di EscapeManager.
 * Cambia il valore di VISTA (1-4) per caricare la schermata desiderata,
 * poi avvia con:  mvn javafx:run
 */
public class MockupLauncher extends Application {

    /*  1 = UC1 – Catalogo e Prenotazione
     *  2 = UC2 – Lista d'Attesa (Popup)
     *  3 = UC3 – Dashboard Game Master
     *  4 = UC4 – Amministrazione Tariffe          */
    private static final int VISTA = 2;

    @Override
    public void start(Stage stage) throws Exception {

        String fxmlFile;
        String title;
        double width;
        double height;

        switch (VISTA) {
            case 1  -> { fxmlFile = "CatalogoView.fxml";     title = "UC1 — Prenotazione";            width = 950; height = 620; }
            case 2  -> { fxmlFile = "ListaAttesaView.fxml";  title = "UC2 — Lista d'Attesa";          width = 580; height = 380; }
            case 3  -> { fxmlFile = "DashboardGMView.fxml";  title = "UC3 — Dashboard Game Master";   width = 920; height = 560; }
            case 4  -> { fxmlFile = "AdminView.fxml";        title = "UC4 — Configurazione Tariffe";  width = 950; height = 620; }
            default -> throw new IllegalArgumentException("Vista non valida: " + VISTA);
        }

        Parent root = FXMLLoader.load(
            getClass().getResource("/fxml/" + fxmlFile));

        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(
            getClass().getResource("/css/mockup.css").toExternalForm());

        stage.setTitle("EscapeManager — " + title);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
