package hangmanfx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class HangmanFX extends Application {

    private static Text scenetitle = new Text("");
    private static GridPane grid = new GridPane();
    private static Scene scene = new Scene(grid, 550, 175);
    private static Label wbl = new Label(" No guesses yet");
    private static Label gl = new Label("");
    private static TextField letter = new TextField();
    private static Label ll = new Label("");
    private static Button b = new Button("guess");
    private static volatile boolean bp = false;

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        primaryStage.setTitle("Hangman");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPadding(new Insets(25, 25, 25, 25));
        primaryStage.setScene(scene);
        scenetitle.setFont(Font.font("Courier", FontWeight.NORMAL, 40));
        ll.setFont(Font.font("NK57 Monospace Cd Rg", FontWeight.NORMAL, 9));
        grid.add(scenetitle, 0, 0, 2, 1);
        grid.add(wbl, 2, 0);
        grid.add(gl, 0, 2);
        grid.add(letter, 2, 2);
        grid.add(ll, 0, 8);
        grid.add(b, 2, 4);
        b.setOnAction((ActionEvent event) -> {
            bp = true;
            bp = false;
        });
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                System.exit(0);
            }

        });
        primaryStage.show();
    }

    public static void main(String[] args) throws FileNotFoundException {
        mth T1 = new mth("Thread-1");
        T1.start();
        launch(args);

    }

    public static void setGL(int gln) {
        Platform.runLater(() -> {
            gl.setText(gln + " guesses left");
        });

    }

    public static void setIC(boolean correct) {
        Platform.runLater(() -> {

            if (correct) {
                wbl.setText("Correct!");
            } else {
                wbl.setText("INCORRECT");
            }
        });
    }

    public static void setLetters(ArrayList<String> ltrs) {
        Platform.runLater(() -> {
            ll.setText(Arrays.toString(ltrs.toArray()));
        });

    }

    public static void setKL(char[] cf) {
        Platform.runLater(() -> {
            String word = "";
            for (char c : cf) {
                word += c;
            }
            scenetitle.setText(word);
        });

    }

    public static boolean bP() {
        return bp;
    }

    public static char gC() {
        if ("".equals(letter.getText())) {
            return " ".charAt(0);
        }
        char c = letter.getText().charAt(0);
        return c;
    }

    public static void rTV() {
        Platform.runLater(() -> {
            letter.setText("");
        });

    }

    public static void win() {
        Platform.runLater(() -> {
            String m = mth.getM();
            scenetitle.setFont(Font.font("Courier", FontWeight.NORMAL, 15));
            scenetitle.setText("Congratulations! You win! The word was " + "\"" + m + "\"");
        });
    }

    public static void lose() {
        Platform.runLater(() -> {
            String m = mth.getM();
            scenetitle.setFont(Font.font("Courier", FontWeight.NORMAL, 15));
            scenetitle.setText("YOU LOSE! " + "THE WORD WAS " + "\"" + m + "\"!");
        });
    }
}

class mth extends Thread {

    private Thread t;
    private final String threadName;
    private static String m = "";

    mth(String name) {
        threadName = name;
    }

    @Override
    public void run() {
        try {
            hangman(6);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(mth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(mth.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public static void hangman(int guesses) throws FileNotFoundException, InterruptedException {
        ArrayList<String> list;
        try (Scanner s = new Scanner(new File("C:\\Users\\thenewmans\\Documents\\NetBeansProjects\\Name\\src\\name\\1-1000.txt"))) {
            list = new ArrayList<>();
            while (s.hasNext()) {
                list.add(s.next());
            }
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size() + 1);
        m = list.get(randomNum);
        ArrayList<String> letters = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            letters.add(c + "");
        }
        HangmanFX.setLetters(letters);
        //Scanner s = new Scanner(System.in);
        String input;
        char[] wrd = m.toCharArray();
        char[] ca = repeat("-", m.length()).toCharArray();
        HangmanFX.setKL(ca);
        int g = guesses;
        HangmanFX.setGL(g);
        while (!Arrays.equals(ca, wrd) && g > 0) {
            while (!HangmanFX.bP()) {
            }
            input = HangmanFX.gC() + "";
            HangmanFX.rTV();
            //System.out.println("You have " + g + " guesses left");

            if (letters.contains(input)) {
                letters.set(letters.indexOf(input), "_");
                HangmanFX.setLetters(letters);
                if (m.contains(input)) {
                    char[] wd = m.toCharArray();
                    ArrayList<Character> wda = new ArrayList<>();
                    for (char c : wd) {
                        wda.add(c);
                    }
                    for (int i = 0; i < m.length(); i++) {
                        if (wda.indexOf(input.toCharArray()[0]) != -1) {
                            ca[wda.indexOf(input.toCharArray()[0])] = wda.get(wda.indexOf(input.toCharArray()[0]));
                            wda.set(wda.indexOf(input.toCharArray()[0]), " ".charAt(0));
                        }
                    }

                    HangmanFX.setIC(true);
                    HangmanFX.setKL(ca);
                } else {
                    g--;
                    //System.out.println("INCORRECT, you have " + g + " guesses left");
                    HangmanFX.setGL(g);
                    HangmanFX.setIC(false);
                }
            } else {
                //System.out.println("You have already used that letter or your input was not a letter");
            }
        }
        if (g == 0) {
            HangmanFX.lose();
//System.out.println("YOU LOSE! " + "THE WORD WAS " + "\"" + m + "\"");
        } else {
            //System.out.println("Congratulations! You win!");
            HangmanFX.win();
        }

    }

    public static String getM() {
        return m;
    }

    public static String repeat(String s, int n) {
        String rv = "";
        for (int i = 0; i < n; i++) {
            rv += s;
        }
        return rv;
    }
}
