package sample;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.ResourceBundle;

public class TranslateController implements Initializable {
    @FXML
    public TextArea word_translated;
    @FXML
    public TextField word_target;
    @FXML
    public ImageView img_speech;
    @FXML
    public ProgressIndicator loading;
    @FXML
    public Button btn_translate;


    public void translate() throws IOException, ParseException, InterruptedException {
        String text = word_target.getText();
        if (text.equals("")) {
            showAlert();
            return;
        }
        loading.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlStr = null;
                try {
                    urlStr = "https://script.google.com/macros/s/AKfycbwBBo3bAEC5aDzqDq1gYehfcUJShDDYU6hkpB4DJWl3kXBeURuq/exec" +
                            "?q=" + URLEncoder.encode(text, "UTF-8") +
                            "&target=" + "vi" +
                            "&source=" + "en";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                URL url = null;
                try {
                    url = new URL(urlStr);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                StringBuilder response = new StringBuilder();
                HttpURLConnection con = null;
                try {
                    con = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String inputLine = new String();
                while (true) {
                    try {
                        if (!((inputLine = in.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.append(inputLine);
                }
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loading.setVisible(false);
                word_translated.setText(response.toString());
            }
        }).start();

//        String text = word_target.getText();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://google-translate1.p.rapidapi.com/language/translate/v2"))
//                .header("content-type", "application/x-www-form-urlencoded")
//                .header("accept-encoding", "application/gzip")
//                .header("x-rapidapi-host", "google-translate1.p.rapidapi.com")
//                .header("x-rapidapi-key", "cd524481ecmsh7215b6bc6fc25a1p1d91dfjsn958e0ab84964")
//                .method("POST", HttpRequest.BodyPublishers.ofString("q="+text+"&target=vi&source=en"))
//                .build();
//
//        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        JSONParser parser = new JSONParser();
//        JSONObject resultObject = (JSONObject) parser.parse(response.body());
//        JSONObject object1 = (JSONObject) resultObject.get("data");
//        JSONArray array = (JSONArray) object1.get("translations");
//        Object[] arrayList = array.toArray();
////        JSONObject o = (JSONObject) object1.get("translations");
//        JSONObject s = (JSONObject) array.get(0);
//        String str = (String) s.get("translatedText");
//        word_translated.setText(str);

    }

    public void back() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
        Scene scene = new Scene(root, 600, 400);
        Stage stage = (Stage) word_target.getScene().getWindow();
        stage.setTitle("Dictionary");
        stage.setScene(scene);

        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loading.setVisible(false);
        speech();
    }

    public void speech() {
        img_speech.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
                Voice voice = VoiceManager.getInstance().getVoice("kevin16");
                voice.allocate();
                voice.speak(word_target.getText());
            }
        });
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Alert");
        alert.setContentText("Bạn chưa nhập gì mà :<");
        alert.showAndWait();
    }
}
