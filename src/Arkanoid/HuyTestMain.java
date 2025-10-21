package Arkanoid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HuyTestMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            String fxmlName = "MainMenu.fxml";
            //String fxmlName = "WinScreen.fxml";
            //String fxmlName = "GameOver.fxml";
            //String fxmlName = "StoryDialog.fxml";
            //String fxmlName = "GameUI.fxml"

            URL fxmlLocation = getClass().getResource("/ui/" + fxmlName);
            if (fxmlLocation == null) {
                System.out.println("Không tìm thấy file " + fxmlName + ". Hãy kiểm tra lại đường dẫn trong thư mục /ui/");
                return;
            }
            Parent root = FXMLLoader.load(fxmlLocation);
            Scene scene = new Scene(root, 1280, 720);

            primaryStage.setTitle("Arkanoid - Test " + fxmlName);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
