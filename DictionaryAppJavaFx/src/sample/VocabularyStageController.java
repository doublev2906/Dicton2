package sample;

import com.sun.javafx.css.StyleCacheEntry;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.IntStream;

public class VocabularyStageController implements Initializable {

    ArrayList<Word> words;
    ObservableList<String> lv_word;

    @FXML
    public Button btn_back;
    @FXML
    public ImageView speech;
    @FXML
    public Button btn_add_word;
    @FXML
    public TextArea en_word;
    @FXML
    public TextArea vn_word;
    @FXML
    public TextField tf_find_word;
    @FXML
    public ListView<String> lv_list_vocab;

    public void initWords() throws IOException {
        File file = new File("data.txt");
        var check = file.exists();
        words = new ArrayList<>();
        int count = 1;
        Scanner readFile = new Scanner(file);
        FileReader fileReader = new FileReader(file);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        Word word = new Word();
        while (lineNumberReader.readLine() != null) {

            var line = readFile.nextLine();
            if (count % 2 == 1) {
                word.setWordTarget(line);
            } else {
                word.setWordExplain(line);
                words.add(word);
                word = new Word();
            }
            count++;

        }

        readFile.close();
    }

    private void setListView() {
        ArrayList<String> vocab_en_words = new ArrayList<>();
        for (var i : words) {
            vocab_en_words.add(i.getWordTarget());
        }
        lv_word = FXCollections.observableList(vocab_en_words);

        IntStream.range(0, 1000).mapToObj(Integer::toString).forEach(lv_word::add);
        FilteredList<String> filteredData = new FilteredList<>(lv_word, s -> true);
        lv_list_vocab.setItems(filteredData);
        tf_find_word.textProperty().addListener((observableValue, s, t1) -> {
            String filter = tf_find_word.getText();
            if (filter == null || filter.length() == 0) {
                filteredData.setPredicate(s1 -> true);
            } else {
                filteredData.setPredicate(s1 -> s1.toLowerCase().startsWith(filter.toLowerCase()));
            }
        });
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem();
        editItem.textProperty().bind(Bindings.format("Edit"));
        MenuItem deleteItem = new MenuItem();
        deleteItem.textProperty().bind(Bindings.format("Delete"));
        contextMenu.getItems().addAll(editItem, deleteItem);
        lv_list_vocab.setOnMouseClicked(mouseEvent -> {
            int index = find(lv_list_vocab.getSelectionModel().getSelectedItem());
            String s = lv_list_vocab.getSelectionModel().getSelectedItem();
//            ArrayList<String> list = (ArrayList<String>) lv_list_vocab.getItems();
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                editItem.setOnAction(actionEvent -> {
                    Word editWord = createDialog(words.get(index));
                    words.set(index, editWord);
                    lv_word.set(index, editWord.getWordTarget());
                });
                deleteItem.setOnAction(actionEvent -> {
                    words.remove(index);
                    lv_word.remove(s);
                });
                contextMenu.show(lv_list_vocab, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            } else {
                contextMenu.hide();
                if (index != -1) {
                    en_word.setText(words.get(index).getWordTarget());
                    vn_word.setText(words.get(index).getWordExplain());
                }
            }


        });


    }

    private int find(String text) {
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getWordTarget().equals(text)) {
                return i;
            }
        }
        return -1;
    }

    private Word createDialog(Word word) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Dialog");

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField vocabulary = new TextField();

        TextField mean = new TextField();

        if (word == null) {
            vocabulary.setPromptText("Vocabulary");
            mean.setPromptText("Mean");
        } else {
            vocabulary.setText(word.getWordTarget());
            mean.setText(word.getWordExplain());
        }

        grid.add(new Label("Vocabulary:"), 0, 0);
        grid.add(vocabulary, 1, 0);
        grid.add(new Label("Mean:"), 0, 1);
        grid.add(mean, 1, 1);

//        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);

//        vocabulary.textProperty().addListener((observable, oldValue, newValue) -> {
//            loginButton.setDisable(newValue.trim().isEmpty());
//        });
        dialog.getDialogPane().setContent(grid);

//        dialog.setResultConverter(dialogButton -> {
//            if (dialogButton == loginButtonType){
//                return new Pair<>(userName.getText(), password.getText());
//            }
//            return null;
//        } );
        dialog.showAndWait();
        Word word1 = new Word(vocabulary.getText(), mean.getText());
        return word1;
    }

    private void setFindHandler() {
        tf_find_word.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    int index = find(tf_find_word.getText());
                    if (index != -1) {
                        en_word.setText(words.get(index).getWordTarget());
                        vn_word.setText(words.get(index).getWordExplain());
                    } else showAlert();
                }
            }
        });
    }

    public void addWord() {
        Word add_word = createDialog(null);
        words.add(add_word);
        lv_word.add(add_word.getWordTarget());

//        notify();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Alert");
        alert.setContentText("Từ này hơi cao siêu nên mình chưa thêm vào từ điển :<<");
        alert.showAndWait();
    }

    public void speechEn() {
        speech.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Voice voice = VoiceManager.getInstance().getVoice("kevin16");
            voice.allocate();
            voice.speak(en_word.getText());
        });
    }

    public void back() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene scene = new Scene(root, 600, 400);
        Stage stage = (Stage) btn_back.getScene().getWindow();
        stage.setTitle("Dictionary");
        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initWords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setListView();
        setFindHandler();
        speechEn();
    }
}
