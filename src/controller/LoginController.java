package controller;

import connection.Connection;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;
import utils.PasswordAuthentication;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label txtSignUp;
    
    @FXML
    private Label txtNotification;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnSignIn;

    private String userName;
    private String password;
    
    
    @FXML
    void initialize() {
        
    }
    
    public void getFullText(){
        userName = txtUsername.getText().trim();
        password = txtPassword.getText().trim();
    }
    
    public void ActionSignIn(ActionEvent actionEvent){
       getFullText();
     
       if(userName.equals("")){
            txtNotification.setText("Vui lòng nhập tên đăng nhập!");
            return;
       }
       
       
       if(password.equals("")){
            txtNotification.setText("Vui lòng nhập mật khẩu!");
            return;
       }
       
       PasswordAuthentication pwAuth = new PasswordAuthentication();

       Connection connection = new Connection();
       SessionFactory sessionFactory = connection.getSessionFactory();
       Session session = sessionFactory.openSession();
       Transaction transaction = null;
       
       String hql = "select a.password from Account a where a.isAdmin = 0 AND a.username = :usn";
       Query query = session.createQuery(hql);
       query.setString("usn", userName);
       
       List<String> pws = query.list();
       if(pws.size() == 0){
           txtNotification.setText("Tên đăng nhập hoặc mật khẩu không đúng! Vui lòng thử lại");
            return;
       }
       String hashpw = pws.get(0);
       
       
       if(pwAuth.authenticate(password, hashpw)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Chúc mừng!!!");
            alert.setContentText("Đăng nhập thành công!");
 
            alert.showAndWait();
       }else{
           Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fail");
            alert.setHeaderText("");
            alert.setContentText("Có lỗi gì đó xảy ra! Bạn vui lòng thử lại!");
 
            alert.showAndWait();
       }
    }
}
