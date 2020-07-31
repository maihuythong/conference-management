package controller;

import connection.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;

// ref mail validation: https://stackoverflow.com/questions/8204680/java-regex-email

public class RegisterController {
    
    private final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private Pattern pattern;
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label txtSignIn;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnSignUp;

    @FXML
    private TextField txtFullName;

    @FXML
    private PasswordField txtPassword2;

    @FXML
    private TextField txtEmail;

    @FXML
    private Label txtNotification;

    //HBN
    private Connection connection = new Connection();
    private SessionFactory sessionFactory = connection.getSessionFactory();
    private Session session = sessionFactory.openSession();
    Transaction transaction = null;
    
    private String fullName;
    private String userName;
    private String password;
    private String password2;
    private String email;
    
    private List<String> accounts;
    
    @FXML
    void initialize() {
        
        accounts = session.createQuery("select a.username from Account a where a.isAdmin = 0").list();
        txtSignIn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    actionSignInClick(mouseEvent);
                } catch (IOException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void actionSignInClick(MouseEvent mouseEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    public void ActionSignUp(ActionEvent actionEvent){
       getFullText();
       if(fullName.equals("")){
            txtNotification.setText("Bạn phải nhập đầy đủ họ tên!");
            return;
       }
       if(userName.equals("")){
            txtNotification.setText("Bạn phải nhập tên đăng nhập!");
            return;
       }
       
       if(accounts.contains(userName)){
            txtNotification.setText("Tên đăng nhập đã tồn tại! Vui lòng chọn tên mới");
            return;
       }
       
       if(password.equals("")){
            txtNotification.setText("Vui lòng nhập mật khẩu!");
            return;
       }
       
       if(password2.equals("")){
            txtNotification.setText("Vui lòng nhập lại mật khẩu!");
            return;
       }
       
       if(!password.equals(password2)){
           txtNotification.setText("Mật khẩu không khớp! Vui lòng kiểm tra lại");
           return;
       }
      
       
       if(emailCheck(email)){
            txtNotification.setText("Địa chỉ email không hợp lệ!");
            return;
       }
       
       txtNotification.setText("");
       Account newAccount = new Account(fullName, userName, password, email, true, false);
       if(saveAccount(newAccount)){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Chúc mừng!!!");
            alert.setContentText("Bạn đã đăng ký tài khoản thành công!");
 
            alert.showAndWait();
       }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Fail");
            alert.setHeaderText("");
            alert.setContentText("Có lỗi gì đó xảy ra! Bạn vui lòng thử lại!");
 
            alert.showAndWait();
       }
    }
    
    public void getFullText(){
        fullName = txtFullName.getText().trim();
        userName = txtUsername.getText().trim();
        password = txtPassword.getText();
        password2 = txtPassword2.getText();
        email = txtEmail.getText().trim();
    }
    
   private boolean emailCheck(String email){
       pattern = Pattern.compile(regex);
       Matcher matcher = pattern.matcher(email);
       return !matcher.matches();
   }
   
   private boolean saveAccount(Account acc){
        
        try {
            transaction = session.beginTransaction();
            session.save(acc);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            System.err.println(ex);
        }
        
        return true;
   }
}
