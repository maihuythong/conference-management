package controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.stage.Window;


public class ManageConference {
    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> id;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableColumn<?, ?> place;

    @FXML
    private TableColumn<?, ?> time;

    @FXML
    private TableColumn<?, ?> duration;

    @FXML
    private TableColumn<?, ?> size;

    @FXML
    private TableColumn<?, ?> current;

    @FXML
    private Button add_btn;

    @FXML
    void add_btn_click(ActionEvent event) {
        Dialog<?> dialog = new Dialog<>();
        dialog.initOwner(anchorPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/add_conference.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initStyle(StageStyle.DECORATED);
            dialog.setResizable(false);
            dialog.getDialogPane().setPrefSize(900, 700);


        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> System.out.println("close"));

        dialog.show();
    }
}
