package controller;

import account.StorageAccount;
import connection.Connection;
import java.awt.event.ComponentListener;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;
import utils.PasswordAuthentication;


public class ChangePassword {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private PasswordField txt_current_pw;

    @FXML
    private PasswordField txt_new_pw;

    @FXML
    private PasswordField txt_new_pw2;

    @FXML
    private Button btn_confirm;

    @FXML
    private Button btn_cancel;
    
    @FXML
    private Text txt_noti;
    
    private String cpassword;
    private String npassword;
    private String npassword2;
    @FXML
    void confirmClick(ActionEvent event) {
        
        cpassword = txt_current_pw.getText();
        npassword = txt_new_pw.getText();
        npassword2 = txt_new_pw2.getText();

        if(cpassword.equals("")){
            txt_noti.setText("Vui lòng nhập lại mật khẩu cũ!");
            return;
        }
        
        StorageAccount cAccount = StorageAccount.getInstance();
        Account account = cAccount.getAccount();

        PasswordAuthentication pwAuth = new PasswordAuthentication();

        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        if(pwAuth.authenticate(cpassword, account.getPassword())){
            if(npassword.equals("")){
            txt_noti.setText("Vui lòng nhập mật khẩu mới!");
            return;
            }

            if(npassword2.equals("")){
                txt_noti.setText("Vui lòng nhập lại mật khẩu mới!");
                return;
            }

            if(!npassword.equals(npassword2)){
                txt_noti.setText("Mật khẩu không khớp! Vui lòng kiểm tra lại");
                return;
            }
            
            String newhashpw = pwAuth.hash(npassword);
            System.out.println(newhashpw);
            
            transaction = session.beginTransaction();
            
            String hql = "update Account set password = :newpassword where idAccount = :id";
            Query query = session.createQuery(hql);
            query.setParameter("newpassword", newhashpw);
            query.setParameter("id", account.getIdAccount());

            int result = query.executeUpdate();
            
            transaction.commit();
            
            
            if(result > 0){
                transaction = session.beginTransaction();
                String hql2 = "select a from Account a where a.idAccount = :id";
                Query query2 = session.createQuery(hql2);
                query2.setParameter("id", account.getIdAccount());

                List<Account> acc = query2.list();
                if(acc.size() == 0){
                    transaction.rollback();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fail");
                    alert.setHeaderText("");
                    alert.setContentText("Có lỗi gì đó xảy ra! Bạn vui lòng thử lại!");
                    alert.showAndWait();
                }else {
                    transaction.commit();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful");
                    alert.setHeaderText("");
                    alert.setContentText("Bạn đã đổi mật khẩu thành công!");
                    alert.showAndWait();
                    
                    Account logAccount = acc.get(0);
                    cAccount.setAccount(logAccount);
                    
                    session.close();
                    Stage stage = (Stage) btn_cancel.getScene().getWindow();
                    transaction = null;
                    stage.close();
                }
            }else {
                transaction.rollback();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fail");
                alert.setHeaderText("");
                alert.setContentText("Có lỗi gì đó xảy ra! Bạn vui lòng thử lại!");
                alert.showAndWait();
            }
            

        }else{
            txt_noti.setText("Mật khẩu cũ không chính xác!");
            return;
        } 
    }

    @FXML
    void initialize() {

    }


    public void cancelClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }
}
