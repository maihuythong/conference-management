package controller;

import account.StorageAccount;
import connection.Connection;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;
import pojos.Diadiem;

public class AddPlace {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField place_name;

    @FXML
    private TextField addr;

    @FXML
    private TextField size;

    @FXML
    private Button btn_add;

    @FXML
    private Text id;

    private String placeName;
    private String placeAddress;
    private Integer placeSize;

    @FXML
    void initialize() {
        btn_add.setOnMouseClicked(e -> {
            if (isValid()){
                addNewPlace();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Không hợp lệ");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng điền đẩy đủ thông tin địa điểm!");
                alert.showAndWait();
            }
        });
    }

    private void getFullText(){
        placeName = place_name.getText().trim();
        placeAddress = addr.getText().trim();
        placeSize = Integer.valueOf(size.getText().toString());
    }

    boolean isValid(){
        getFullText();
        if (placeName.equals("") || placeAddress.equals("") || placeSize == null){
            return false;
        }

        return true;
    }

    private void addNewPlace() {
        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
       
        transaction = session.beginTransaction();
        Diadiem place = new Diadiem(placeName, placeAddress, placeSize, true);
       
        session.save(place);
        transaction.commit();
        transaction = null;
        session.close();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful");
        alert.setContentText("Đã thêm địa điểm mới!");

        alert.showAndWait();
        
        Stage stage = (Stage) btn_add.getScene().getWindow();
        stage.close();
        
    }
}
