package controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import pojos.Diadiem;
import pojos.Hoinghi;

public class ConferenceInfo {
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView img;

    @FXML
    private Button btn_choose_img;

    
    @FXML
    private Text name;
    
    @FXML
    private Text duration;

    @FXML
    private Text time;

    @FXML
    private Text addr;

    @FXML
    private TextArea short_des;

    @FXML
    private TextArea description;

    @FXML
    private Button close;
    
    
    @FXML
    private Button list_joined;

    
    private Hoinghi currentConference;

    @FXML
    void initialize() {
        close.setOnMouseClicked(e -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
    }
    
    void getData(Hoinghi ConfSelected) {
        currentConference = ConfSelected;
        
        name.setText(currentConference.getTenHoiNghi());
        time.setText(currentConference.getDateString());
        duration.setText(String.valueOf(currentConference.getKhoangThoiGian()));
        addr.setText(currentConference.getDiadiem().toString());
        short_des.setText(currentConference.getMoTaNgan());
        description.setText(currentConference.getMoTaChiTiet());
        byte []ig = currentConference.getHinhAnh();
        if(ig != null){
            ByteArrayInputStream bais = new ByteArrayInputStream(currentConference.getHinhAnh());
            Image image = new Image(bais);
            img.setImage(image);
            img.setFitWidth(300);
            img.setFitHeight(250);
            img.setPreserveRatio(true);
        }
    }
    
    @FXML
    void list_joined_click(ActionEvent event) {
        Dialog<?> list = new Dialog<>();
        list.initOwner(anchorPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/list_joined.fxml"));

        try {
            list.getDialogPane().setContent(fxmlLoader.load());
            ListJoined listJoined = fxmlLoader.getController();
            listJoined.getData(currentConference.getThamgiahoinghis(), currentConference);
            list.initStyle(StageStyle.DECORATED);
            list.setResizable(false);
            list.getDialogPane().setPrefSize(600, 700);


        } catch(IOException ex) {
            System.out.println("Couldn't load the dialog");
            ex.printStackTrace();
            return;
        }

        Window window = list.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> initialize());
        list.showAndWait();
    }
}
