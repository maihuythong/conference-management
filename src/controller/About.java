package controller;

import account.StorageAccount;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pojos.Account;

public class About {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Text account_name;

    @FXML
    private BorderPane dashboard;

    @FXML
    private BorderPane about;

    @FXML
    private BorderPane account;

    @FXML
    private BorderPane manage;

    @FXML
    void initialize() {
        StorageAccount sa = StorageAccount.getInstance();
        if(sa.getAccount() != null){
            Account acc = sa.getAccount();
            account_name.setText(acc.getHoTen());
        }
        if(!sa.getIsAdmin()){
            manage.setVisible(false);
        }
        dashboard.setOnMouseClicked(e -> {
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getResource("/view/Mainscreen.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Scene scene = new Scene(parent);
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
        
        manage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Parent parent = null;
                try {
                    parent = FXMLLoader.load(getClass().getResource("/view/admin_manage2.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

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
            }
        });


        account.setOnMouseClicked(e ->{
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getResource("/view/Accountscreen2.fxml"));
            } catch (IOException ev) {
                ev.printStackTrace();
            }
            Scene scene = new Scene(parent);
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
        
        about.setOnMouseClicked(e -> {
            return;
        });
    }
}
