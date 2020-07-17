package controller;

import connection.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;


public class UserInfo {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text fullName;

    @FXML
    private Text email;

    @FXML
    private Text username;

    @FXML
    private HBox status_box;

    @FXML
    private Text status;

    @FXML
    private Text time2;
    
    @FXML 
    private Button block_btn;

    @FXML
    private TableView<?> conf_table;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> confCol;

    @FXML
    private TableColumn<?, ?> timeCol;

    @FXML
    private TableColumn<?, ?> addrCol;

    private Account account;

    @FXML
    void initialize() {
        block_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("click signup");
                Connection connection = new Connection();
                SessionFactory sessionFactory = connection.getSessionFactory();
                Session session = sessionFactory.openSession();
                Transaction transaction = null;
                transaction = session.beginTransaction();
                String hql = "update Account set active = :status where idAccount = :id";
                Query query = session.createQuery(hql);
                query.setParameter("status", !account.isActive());
                query.setParameter("id", account.getIdAccount());
                int result = query.executeUpdate();
                if(result > 0){
                    transaction.commit();
                    account.setActive(!account.isActive());
                    
                    if(account.isActive()){
                        block_btn.setText("Chặn");
                    }else{
                        block_btn.setText("Bỏ chặn");
                    }
                }else{
                    transaction.rollback();
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Fail");
                    alert2.setHeaderText("");
                    alert2.setContentText("Có lỗi gì đó xảy ra! Bạn vui lòng thử lại!");
                    alert2.showAndWait();
                }
                transaction = null;
            }
        });
    }

    public void getData(Account account){
        this.account = account;
        
        fullName.setText(account.getHoTen());
        email.setText(account.getEmail());
        username.setText(account.getUsername());
        status.setText(account.getStatus());
        
        if(account.isActive()){
            block_btn.setText("Chặn");
        }else{
            block_btn.setText("Bỏ chặn");
        }
        
    }
}
