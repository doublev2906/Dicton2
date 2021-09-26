package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class WordManagement {
    ArrayList<Word> words;

    public ArrayList<Word> getWords() {
        return words;
    }

    public WordManagement() throws IOException {
        initWords();
    }

    public void initWords() {
        File file = new File("data.txt");
        var check = file.exists();
        words = new ArrayList<>();
        int count = 1;
        Scanner readFile = null;
        try {
            readFile = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        Word word = new Word();
        while (true) {
            try {
                if (!(lineNumberReader.readLine() != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

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
}
