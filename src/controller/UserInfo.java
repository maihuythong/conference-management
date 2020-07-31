package controller;

import connection.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import javafx.stage.Window;
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
    private AnchorPane anchorPane;

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
    private Text list_conf;

    @FXML
    private Button block_btn;
    
    private Account account;


    @FXML
    void list_conf_click(MouseEvent event) {
        Dialog<?> listConf = new Dialog<>();
        listConf.initOwner(anchorPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/list_conference_user_joined.fxml"));

        try {
            listConf.getDialogPane().setContent(fxmlLoader.load());
            ListConferenceUserJoined list= fxmlLoader.getController();
           
            list.getConfData(account.getThamgiahoinghis());
            listConf.initStyle(StageStyle.DECORATED);
            listConf.setResizable(false);
            listConf.getDialogPane().setPrefSize(1200, 750);


        } catch(IOException ex) {
            System.out.println("Couldn't load the dialog");
            ex.printStackTrace();
            return;
        }

        Window window = listConf.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> initialize());

        listConf.showAndWait();
    }

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
