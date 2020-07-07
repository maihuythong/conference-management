package controller;

import connection.Connection;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        for(int i = 0; i < accounts.size(); ++i){
            System.out.println(accounts.get(i));
        }
//        btnSignUp.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                
//                Account account = new Account("MHT", "thongthong", "123","mht@mail.com", false);
                
//                System.out.print("vcl");
//                        Connection connection = new Connection();
//                        SessionFactory sessionFactory = connection.getSessionFactory();
//                        Session session = sessionFactory.openSession();
//                        
//                        String username = txtUsername.getText().toString().trim();
//                        
//                        String hql = "from Account u where u.username = :usern";
//                        System.out.println(username);
//                        Query queryUser = session.createQuery(hql);
//                        queryUser.setParameter("usern", username);
//                        List<Account> results = queryUser.list();
//                        
//                        System.out.println(results.size());
//                        for (Account u : results) {
//                            System.out.println(u.getIdAccount() + u.getUsername());
//                        }
//                        System.out.println("end");
//                        Transaction transaction = null;
//                        try {
//                            transaction = session.beginTransaction();
//                            session.save(user);
//                            transaction.commit();
//                        } catch (HibernateException ex) {
//                            transaction.rollback();
//                            System.err.println(ex);
//                        } finally {session.close();}
//            }
//        });
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
       Account newAccount = new Account(fullName, userName, password, email, false);
       
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
        password = txtPassword.getText().trim();
        password2 = txtPassword2.getText().trim();
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
