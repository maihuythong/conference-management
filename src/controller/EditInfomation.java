package controller;

import account.StorageAccount;
import connection.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;
import utils.PasswordAuthentication;

public class EditInfomation {
    
    @FXML
    private GridPane gridPane;
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txt_fullname;

    @FXML
    private TextField txt_username;

    @FXML
    private PasswordField txt_password;

    @FXML
    private TextField txt_email;

    @FXML
    private Button btn_finish;

    @FXML
    private ImageView edit_fullname;

    @FXML
    private ImageView edit_password;

    @FXML
    private ImageView edit_email;

    @FXML
    private Text txt_noti;
    
    private String fullName;
    private String email;

    @FXML
    void finishClick(ActionEvent event) throws IOException {
        
        fullName = txt_fullname.getText().trim();
        email = txt_email.getText().trim();
        
        if(fullName.equals("")){
            txt_noti.setText("Vui lòng nhập họ tên!");
            return;
        }
        
        if(emailCheck(email)){
            txt_noti.setText("Địa chỉ email không hợp lệ!");
            return;
        }
        
        
        StorageAccount cAccount = StorageAccount.getInstance();
        Account account = cAccount.getAccount();

        PasswordAuthentication pwAuth = new PasswordAuthentication();

        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        transaction = session.beginTransaction();
        String hql = "update Account set hoTen = :fullName, email = :email  where idAccount = :id";
        Query query = session.createQuery(hql);
        query.setParameter("fullName", fullName);
        query.setParameter("email", email);
        query.setParameter("id", account.getIdAccount());

        int result = query.executeUpdate();
        if(result > 0){
            transaction.commit();
            transaction = session.beginTransaction();
            String hql2 = "select a from Account a where a.idAccount = :id";
            Query query2 = session.createQuery(hql2);
            query2.setParameter("id", account.getIdAccount());
            
            List<Account> acc = query2.list();
            if(acc.size() == 0){
                transaction.rollback();
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Fail");
                alert2.setHeaderText("");
                alert2.setContentText("Có lỗi gì đó xảy ra! Bạn vui lòng thử lại!");
                alert2.showAndWait();
            }else {
                transaction.commit();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setHeaderText("");
                alert.setContentText("Bạn đã thay đổi thông tin thành công!");
                alert.showAndWait();

                Account logAccount = acc.get(0);
                cAccount.setAccount(logAccount);
                session.close();
                transaction = null;

                Stage stage = (Stage) btn_finish.getScene().getWindow();
                stage.close();
            }
        }else{
            transaction.rollback();
        }
    }

    @FXML
    void initialize() {
        StorageAccount acc = StorageAccount.getInstance();
        Account currentAccount = acc.getAccount();
        txt_fullname.setText(currentAccount.getHoTen());
        txt_username.setText(currentAccount.getUsername());
        txt_password.setText("********");
        txt_email.setText(currentAccount.getEmail());
        
        txt_fullname.setDisable(true);
        txt_username.setDisable(true);
        txt_password.setDisable(true);
        txt_email.setDisable(true);
        
        edit_fullname.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
               txt_fullname.setDisable(false);
               txt_fullname.setEditable(true);
            }
        });
        
        edit_email.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
               txt_email.setDisable(false);
               txt_email.setEditable(true);
            }
        });
        
        edit_password.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
               editPassword(mouseEvent);
            }
        });
    }
    
    private void editPassword(MouseEvent mouseEvent){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gridPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/change_password.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initStyle(StageStyle.UNDECORATED);


        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        Optional<ButtonType> result = dialog.showAndWait();
      
    }
    
    private boolean emailCheck(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern;
        pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }
}
