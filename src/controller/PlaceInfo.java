package controller;

import connection.Connection;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;
import pojos.Diadiem;
    
public class PlaceInfo {

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
    
    private Diadiem currentData;
    
    private String newName;
    private String newAddr;
    private Integer newSize;
    
    @FXML
    void initialize() {
        
        btn_add.setOnMouseClicked(e -> {
            getNewData();
           if(isChanged()){
               updatePlace();
           } else{
                Stage stage = (Stage) btn_add.getScene().getWindow();
                stage.close();
           }
        });
    }
    
    void getData(Diadiem placeSelected) {
        currentData = placeSelected;
        
        place_name.setText(currentData.getTenDiaDiem());
        addr.setText(currentData.getDiaChi());
        size.setText(String.valueOf(currentData.getSucChua()));
    }
    
    void getNewData(){
        newName = place_name.getText().trim();
        newAddr = addr.getText().trim();
        newSize = Integer.valueOf(size.getText().trim());
    }

    private boolean isChanged() {
        if(newName.equals(currentData.getTenDiaDiem()) && newAddr.equals(currentData.getDiaChi())
                && newSize == currentData.getSucChua()){
            return false;
        }
        return true;
    }

    private void updatePlace() {
        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        transaction = session.beginTransaction();
        String hql = "update Diadiem set tenDiaDiem = :newName, diaChi = :newAddr, sucChua= :newSize where idDiaDiem = :id";
        Query query = session.createQuery(hql);
        query.setParameter("newName", newName);
        query.setParameter("newAddr", newAddr);
        query.setParameter("newSize", newSize);
        query.setParameter("id", currentData.getIdDiaDiem());

        int result = query.executeUpdate();
        if(result > 0){
            transaction.commit();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("");
            alert.setContentText("Bạn đã thay đổi thông tin thành công!");
            alert.showAndWait();
            transaction = null;
            session.close();
            Stage stage = (Stage) btn_add.getScene().getWindow();
            stage.close();
        }else{
            transaction.rollback();
            transaction = null;
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Fail");
            alert2.setHeaderText("");
            alert2.setContentText("Có lỗi gì đó xảy ra! Bạn vui lòng thử lại!");
            alert2.showAndWait();
        }
    }
}
