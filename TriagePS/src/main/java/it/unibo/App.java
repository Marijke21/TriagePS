package it.unibo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Gestione Pronto Soccorso - FHIR");
        navigateTo("main");
        primaryStage.show();
    }

    public static void navigateTo(String fxmlName) {
        navigateTo(fxmlName, null);
    }

    public static void navigateTo(String fxmlName, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxmlName + ".fxml"));
            Parent root = loader.load();

            // Passa dati al controller se supportato
            Object controller = loader.getController();
            if (data != null && controller instanceof DataReceiver) {
                ((DataReceiver) controller).receiveData(data);
            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
