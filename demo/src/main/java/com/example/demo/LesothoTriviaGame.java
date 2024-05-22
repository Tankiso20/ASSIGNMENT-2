package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LesothoTriviaGame extends Application {

    // Trivia questions, options, answers, and corresponding image file names
    private final String[] questions = {
            "Who is the founder of the Basotho Nation?",
            "When did Lesotho gain its independence?",
            "What is the biggest border gate in Lesotho?",
            "What is the national park in Lesotho?",
            "Who is the prime minister of Lesotho?"
    };

    private final String[][] options = {
            {"A. Mothejoa Metsing", "B. King Moshoeshoe 1", "C. King Moorosi", "D. Motlotlehi "},
            {"A. 2002", "B. 2024", "C. 1966", "D. Yesterday"},
            {"A. Maseru Bridge", "B. Peka Bridge", "C. Vanrooi Bridge", "D. Caledon Bridge"},
            {"A. Moshoeshoe park", "B. Kome", "C. Maletsunyane", "D. Sehlabathebe National Park"},
            {"A. Mofomobe", "B. Mokhothi", "C. Ntsokoane", "D. Lebona"}
    };

    private final char[] answers = {'B', 'C', 'A', 'D', 'C'};

    // Image file names for each question
    private final String[] imageFileNames = {
            "mosh.jpg",
            "independance.jpg",
            "MASERUBRIDGE.jpeg",
            "sehlabathebe.jpg",
            "sam.jpg"
    };

    private int currentQuestionIndex = 0;
    private int score = 0;
    private int totalQuestions = questions.length;

    private Label questionLabel;
    private ToggleGroup optionsGroup;
    private Label resultLabel;
    private Button submitButton;
    private Label scoreLabel;
    private BorderPane root;
    private ImageView imageView;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Top: Image and Question
        imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 2;");
        questionLabel = new Label(questions[currentQuestionIndex]);
        questionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        VBox topBox = new VBox(10, imageView, questionLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20));
        root.setTop(topBox);

        // Center: Options
        VBox optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        optionsBox.setStyle("-fx-spacing: 10; -fx-padding: 20;");
        optionsGroup = new ToggleGroup();
        for (String option : options[currentQuestionIndex]) {
            RadioButton radioButton = new RadioButton(option);
            radioButton.setStyle("-fx-font-size: 14px;");
            radioButton.setToggleGroup(optionsGroup);
            optionsBox.getChildren().add(radioButton);
        }
        root.setCenter(optionsBox);

        // Bottom: Navigation buttons
        HBox bottomBox = new HBox(20);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setStyle("-fx-spacing: 20; -fx-padding: 20;");
        submitButton = new Button("Submit");
        submitButton.setStyle("-fx-font-size: 14px;");
        submitButton.setOnAction(e -> submitAnswer());
        bottomBox.getChildren().addAll(submitButton);
        root.setBottom(bottomBox);

        // Left: Score and Result
        VBox leftBox = new VBox(10);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setStyle("-fx-spacing: 10; -fx-padding: 20;");
        scoreLabel = new Label("Score: " + score);
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");
        leftBox.getChildren().addAll(scoreLabel, resultLabel);
        root.setLeft(leftBox);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.show();

        // Initial image load
        loadImageAndSetImageView(currentQuestionIndex);
    }

    private void submitAnswer() {
        RadioButton selectedRadioButton = (RadioButton) optionsGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            char selectedAnswer = selectedRadioButton.getText().charAt(0);
            checkAnswer(selectedAnswer);
        } else {
            resultLabel.setText("Please select an answer.");
        }
    }

    private void checkAnswer(char selectedAnswer) {
        if (selectedAnswer == answers[currentQuestionIndex]) {
            score++;
            resultLabel.setText("Correct!");
        } else {
            resultLabel.setText("Incorrect. The correct answer is " + answers[currentQuestionIndex]);
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < totalQuestions) {
            showQuestion();
        } else {
            resultLabel.setText("Quiz completed! Final score: " + score + "/" + totalQuestions);
            submitButton.setDisable(true);
            scoreLabel.setText("Score: " + score);
        }
    }

    private void showQuestion() {
        questionLabel.setText(questions[currentQuestionIndex]);
        optionsGroup.selectToggle(null);
        VBox optionsBox = (VBox) root.getCenter();
        optionsBox.getChildren().clear();
        for (String option : options[currentQuestionIndex]) {
            RadioButton radioButton = new RadioButton(option);
            radioButton.setStyle("-fx-font-size: 14px;");
            radioButton.setToggleGroup(optionsGroup);
            optionsBox.getChildren().add(radioButton);
        }

        loadImageAndSetImageView(currentQuestionIndex);

        resultLabel.setText("");
        submitButton.setDisable(false);
        scoreLabel.setText("Score: " + score);
    }

    private void loadImageAndSetImageView(int index) {
        String imageName = imageFileNames[index];
        Image image = loadImage(imageName);
        if (image != null) {
            imageView.setImage(image);
        } else {
            imageView.setImage(null); // Clear image if loading fails
        }
    }

    private Image loadImage(String fileName) {
        try {
            return new Image(getClass().getResourceAsStream("/" + fileName));
        } catch (Exception e) {
            System.err.println("Could not load image: " + fileName);
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
