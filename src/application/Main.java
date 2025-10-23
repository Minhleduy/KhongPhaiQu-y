package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            SceneManager sceneManager = SceneManager.getInstance();

            sceneManager.setPrimaryStage(primaryStage);

            primaryStage.setTitle(Config.GAME_TITLE);
            primaryStage.setResizable(false);

            sceneManager.showMainMenuScene();

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
