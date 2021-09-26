package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    public ImageView img_logo;
    @FXML
    public Button btn_translate;
    @FXML
    public Button btn_exit;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void changeTranslateStage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("translate_stage.fxml"));
        Scene scene = new Scene(root, 600, 400);
        btn_translate.getScene();
        Stage stage = (Stage) btn_translate.getScene().getWindow();
        stage.setTitle("Translate");
        stage.setScene(scene);
        stage.show();
    }

    public void changeVocabularyStage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("vocabulary_stage.fxml"));
        Scene scene = new Scene(root, 600, 400);
        btn_translate.getScene();
        Stage stage = (Stage) btn_translate.getScene().getWindow();
        stage.setTitle("Vocabulary");
        stage.setScene(scene);
        stage.show();
    }

    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
    }
}
