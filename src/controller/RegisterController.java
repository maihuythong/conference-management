package controller;

import connection.Connection;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;

public class RegisterController {

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

    @FXML
    void initialize() {
        btnSignUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Click register");
                        Connection connection = new Connection();
                        SessionFactory sessionFactory = connection.getSessionFactory();
                        Session session = sessionFactory.openSession();
                        
                        String username = txtUsername.getText().toString().trim();
                        
                        String hql = "from Account u where u.username = :usern";
                        System.out.println(username);
                        Query queryUser = session.createQuery(hql);
                        queryUser.setParameter("usern", username);
                        List<Account> results = queryUser.list();
                        
                        System.out.println(results.size());
                        for (Account u : results) {
                            System.out.println(u.getIdAccount() + u.getUsername());
                        }
                        System.out.println("end");
//                        Transaction transaction = null;
//                        try {
//                            transaction = session.beginTransaction();
//                            session.save(user);
//                            transaction.commit();
//                        } catch (HibernateException ex) {
//                            transaction.rollback();
//                            System.err.println(ex);
//                        } finally {session.close();}
            }
        });
    }
    
    
}
