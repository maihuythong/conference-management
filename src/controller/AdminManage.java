
package controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;

public class AdminManage {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ImageView return_home;

    @FXML
    private HBox hbox_control;

    @FXML
    void initialize() {

        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.BASELINE_CENTER);
        Class<?> clazz = this.getClass();
        InputStream input = clazz.getResourceAsStream("/img/conference_manage.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        vBox1.getChildren().add(imageView);
        Text text1 = new Text("Hội nghị");
        text1.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 20));
        vBox1.getChildren().add(text1);
        vBox1.setAlignment(Pos.CENTER);

        VBox vBox2 = new VBox();
        vBox2.setAlignment(Pos.BASELINE_CENTER);
        Class<?> clazz2 = this.getClass();
        InputStream input2 = clazz2.getResourceAsStream("/img/place_manage.png");
        Image image2 = new Image(input2);
        ImageView imageView2 = new ImageView(image2);
        vBox2.getChildren().add(imageView2);
        Text text2 = new Text("Địa điểm");
        text2.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 20));
        vBox2.getChildren().add(text2);
        vBox2.setAlignment(Pos.CENTER);

        VBox vBox3 = new VBox();
        vBox3.setAlignment(Pos.BASELINE_CENTER);
        Class<?> clazz3 = this.getClass();
        InputStream input3 = clazz3.getResourceAsStream("/img/account_manage.png");
        Image image3 = new Image(input3);
        ImageView imageView3 = new ImageView(image3);
        vBox3.getChildren().add(imageView3);
        Text text3 = new Text("Tài khoản");
        text3.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 20));
        vBox3.getChildren().add(text3);
        vBox3.setAlignment(Pos.CENTER);

        VBox vBox4 = new VBox();
        vBox4.setAlignment(Pos.BASELINE_CENTER);
        Class<?> clazz4 = this.getClass();
        InputStream input4 = clazz4.getResourceAsStream("/img/request.png");
        Image image4 = new Image(input4);
        ImageView imageView4 = new ImageView(image4);
        vBox4.getChildren().add(imageView4);
        Text text4 = new Text("Yêu cầu");
        text4.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 20));
        vBox4.getChildren().add(text4);
        vBox4.setAlignment(Pos.CENTER);

        hbox_control.getChildren().add(vBox1);
        hbox_control.getChildren().add(vBox2);
        hbox_control.getChildren().add(vBox3);
        hbox_control.getChildren().add(vBox4);
        hbox_control.setMargin(vBox2 ,new Insets(0,0,0,50));
        hbox_control.setMargin(vBox3 ,new Insets(0,0,0,50));
        hbox_control.setMargin(vBox4 ,new Insets(0,0,0,50));

        vBox1.setOnMouseClicked(e -> {
            actionConferenceClick();
        });

        vBox2.setOnMouseClicked(e -> {
            actionPlaceClick(e);
        });

        vBox3.setOnMouseClicked(e -> {
            try {
                actionAccountClick(e);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        vBox4.setOnMouseClicked(e -> {
            actionRequest();
        });

        return_home.setOnMouseClicked(e -> {

            System.out.println("click return");
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getResource("/view/Mainscreen.fxml"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Scene scene = new Scene(parent, 990, 700);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            stage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
                }
            });

            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        });
    }
    
    
    private void actionConferenceClick() {
        Dialog<?> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/manage_conference.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initStyle(StageStyle.DECORATED);
            dialog.setResizable(false);
            dialog.getDialogPane().setPrefSize(900, 700);
            centerStage(dialog,916,739);

        } catch(IOException ex) {
            System.out.println("Couldn't load the dialog");
            ex.printStackTrace();
            return;
        }

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> System.out.println("close"));

        dialog.show();
    }

    private void actionAccountClick(MouseEvent e) throws IOException {
        Dialog<?> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/manage_account.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initStyle(StageStyle.DECORATED);
            dialog.setResizable(false);
            dialog.getDialogPane().setPrefSize(900, 700);


        } catch(IOException ex) {
            System.out.println("Couldn't load the dialog");
            ex.printStackTrace();
            return;
        }

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> System.out.println("close"));

        dialog.show();
    }

    private void actionPlaceClick(MouseEvent e) {
        Dialog<?> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/manage_place.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initStyle(StageStyle.DECORATED);
            dialog.setResizable(false);
            dialog.getDialogPane().setPrefSize(940, 700);


        } catch(IOException ex) {
            System.out.println("Couldn't load the dialog");
            ex.printStackTrace();
            return;
        }

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> System.out.println("close"));

        dialog.show();
    }

    private void centerStage(Dialog<?> stage, double width, double height) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
    }

    private void actionRequest() {
        Dialog<?> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/request.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initStyle(StageStyle.DECORATED);
            dialog.setResizable(false);
            dialog.getDialogPane().setPrefSize(940, 700);


        } catch(IOException ex) {
            System.out.println("Couldn't load the dialog");
            ex.printStackTrace();
            return;
        }

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> System.out.println("close"));

        dialog.show();
    }

}
