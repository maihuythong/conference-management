package controller;

import account.StorageAccount;
import connection.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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
        
        txtSignUp.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    actionSignUpClick(mouseEvent);
                } catch (IOException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
     private void actionSignUpClick(MouseEvent mouseEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/Register.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
     }
    
    public void getFullText(){
        userName = txtUsername.getText().trim();
        password = txtPassword.getText();
    }
    
    public void ActionSignIn(ActionEvent actionEvent) throws IOException{
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
       
       String hql = "select a from Account a where a.username = :usn";
       Query query = session.createQuery(hql);
       query.setString("usn", userName);
       
       List<Account> pws = query.list();
       if(pws.size() == 0){
           txtNotification.setText("Tên đăng nhập hoặc mật khẩu không đúng! Vui lòng thử lại");
            return;
       }
       Account logAccount = pws.get(0);
       
       if(pwAuth.authenticate(password, logAccount.getPassword())){
            StorageAccount acc = StorageAccount.getInstance();
            acc.setAccountId(logAccount.getIdAccount());
            acc.setIsAdmin(logAccount.isIsAdmin());
            acc.setAccount(logAccount);
            Stage stage = (Stage) btnSignIn.getScene().getWindow();
            stage.close();

       }else{
           Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fail");
            alert.setHeaderText("");
            alert.setContentText("Tên đăng nhập hoặc mật khẩu không đúng!");
 
            alert.showAndWait();
       }
    }
}
