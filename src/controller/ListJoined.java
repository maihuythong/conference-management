package controller;

import java.net.URL;
import java.util.Iterator;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import pojos.Account;
import pojos.Diadiem;
import pojos.Hoinghi;
import pojos.Thamgiahoinghi;

public class ListJoined {
 
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Account> table;

    @FXML
    private TableColumn<Account, String> nameCol;

    @FXML
    private TableColumn<Account, String> emailCol;

    @FXML
    private Text name_conf;
    
    private ObservableList<Account> observableList;

    @FXML
    void initialize() {
        table.setPlaceholder(new Label("Chưa có người dùng tham gia hội nghị"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
    
    void getData(Set listJoined, Hoinghi conf) {
        name_conf.setText(conf.getTenHoiNghi());
        observableList = FXCollections.observableArrayList();
        
        Iterator<Thamgiahoinghi> itor = listJoined.iterator();
        while(itor.hasNext()){
            observableList.add(itor.next().getAccount());
        }
        
        table.setItems(observableList);
    }
}
