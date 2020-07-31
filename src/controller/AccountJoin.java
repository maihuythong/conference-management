package controller;


import account.StorageAccount;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


public class AccountJoin {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox hbox_control;
    
    @FXML
    private BorderPane accountBorderPane;

    @FXML
    void initialize() {
        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.BASELINE_CENTER);
        Class<?> clazz = this.getClass();
        InputStream input = clazz.getResourceAsStream("/img/login.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        vBox1.getChildren().add(imageView);
        Text text1 = new Text("Đăng nhập");
        text1.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 20));
        vBox1.getChildren().add(text1);
        vBox1.setAlignment(Pos.CENTER);

        VBox vBox2 = new VBox();
        vBox2.setAlignment(Pos.BASELINE_CENTER);
        Class<?> clazz2 = this.getClass();
        InputStream input2 = clazz2.getResourceAsStream("/img/add.png");
        Image image2 = new Image(input2);
        ImageView imageView2 = new ImageView(image2);
        vBox2.getChildren().add(imageView2);
        Text text2 = new Text("Đăng ký");
        text2.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 20));
        vBox2.getChildren().add(text2);
        vBox2.setAlignment(Pos.CENTER);

        hbox_control.getChildren().add(vBox1);
        hbox_control.getChildren().add(vBox2);
        hbox_control.setMargin(vBox2 ,new Insets(0,0,0,50));

        vBox1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("click login");
                try {
                    login(mouseEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        vBox2.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            System.out.println("click register");
            try {
                register(mouseEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        });
            
    }
    
     private void login(MouseEvent mouseEvent) throws IOException {
        Dialog<?> dialog = new Dialog<>();
        dialog.initOwner(accountBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/Login.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initStyle(StageStyle.DECORATED);
            dialog.setResizable(false);
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> System.out.println("close"));

        dialog.showAndWait();
        Stage stage = (Stage) hbox_control.getScene().getWindow();
        stage.close();
     }
    
    private void register(MouseEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/view/Register.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
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
        stage.showAndWait();
        
    }
}
