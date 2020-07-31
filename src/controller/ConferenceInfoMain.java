package controller;

import account.StorageAccount;
import connection.Connection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;
import pojos.Diadiem;
import pojos.Hoinghi;
import pojos.Thamgiahoinghi;
import pojos.ThamgiahoinghiId;


public class ConferenceInfoMain {
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView img;

    @FXML
    private Text duration;

    @FXML
    private Text time;

    @FXML
    private Text addr;

    @FXML
    private TextArea short_des;

    @FXML
    private TextArea description;

    @FXML
    private Button close;

    @FXML
    private Text name;

    @FXML
    private Button join;
    
    private Hoinghi currentConference;

    StorageAccount sa = StorageAccount.getInstance();
    Account acc = sa.getAccount();
    
    private boolean leave = false;

    @FXML
    void join_click(ActionEvent event) throws Exception {
        
        if(leave){
            leaveConference();
            return;
        }
        
        if(acc == null){
            login(event);
            if(!checkJoined()){
                join_conference(acc);
            }
        }else{
            try {
                join_conference(acc);
            }catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thất bại");
                alert.setHeaderText("");
                alert.setContentText("Bạn đã tham gia hội nghị này rồi!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void initialize() {
        close.setOnMouseClicked(e -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
    }
    
    void getData(Hoinghi ConfSelected) {
        currentConference = ConfSelected;
        
        name.setText(currentConference.getTenHoiNghi());
        time.setText(currentConference.getDateString());
        duration.setText(String.valueOf(currentConference.getKhoangThoiGian()));
        addr.setText(currentConference.getDiadiem().toString());
        short_des.setText(currentConference.getMoTaNgan());
        description.setText(currentConference.getMoTaChiTiet());
        byte []ig = currentConference.getHinhAnh();
        if(ig != null){
            ByteArrayInputStream bais = new ByteArrayInputStream(currentConference.getHinhAnh());
            Image image = new Image(bais);
            img.setImage(image);
            img.setFitWidth(300);
            img.setFitHeight(250);
            img.setPreserveRatio(true);
        }
        
        if(currentConference.getJoined() >= currentConference.getDiadiem().getSucChua()){
            join.setDisable(true);
        }
        if(acc != null){
            checkJoined();
        }
        
//        if(acc != null){
//            checkJoined();
//        }
    }

    private void login(ActionEvent event) {
        Dialog<?> dialog = new Dialog<>();
        dialog.initOwner(anchorPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/login_when_join.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initStyle(StageStyle.DECORATED);
            dialog.setResizable(false);
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> System.out.println("close"));

        dialog.showAndWait();
        acc = sa.getAccount();
    }

    private void join_conference(Account acc) throws Exception {
        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        ThamgiahoinghiId tgId = new ThamgiahoinghiId(acc.getIdAccount(), currentConference.getIdHoiNghi());
        Thamgiahoinghi tg = new Thamgiahoinghi(tgId, acc, currentConference, false);
        transaction = session.beginTransaction();
        Object ob = session.save(tg);
        transaction.commit();
        transaction = null;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText("");
        alert.setContentText("Bạn dã gửi yêu cầu tham gia hội nghị thành công!");
        alert.showAndWait();
        session.close();
        leave = true;
        join.setText("Hủy đăng ký");
    }
    
    private boolean checkJoined(){
        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        String hql = "select a from Account a where idAccount = :idAccount";
        Query query = session.createQuery(hql);
        query.setInteger("idAccount", acc.getIdAccount());
        List<Account> list = query.list();
        if(list.size() == 0){
             return false;
        }else{
            for(int i = 0; i < list.size(); ++i){
               Iterator<Thamgiahoinghi> confs = list.get(i).getThamgiahoinghis().iterator();
               while(confs.hasNext()){
                   Thamgiahoinghi tg = confs.next();
                   if(tg.getHoinghi().getIdHoiNghi() == currentConference.getIdHoiNghi()){
//                       join.setDisable(true);
                       leave = true;
                       if(tg.isActive()){
                           join.setText("Hủy tham gia");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thông báo");
                            alert.setHeaderText("");
                            alert.setContentText("Bạn dã tham gia hội nghị này trước đó!");
                            alert.showAndWait();
                       }else{
                           join.setText("Hủy đăng ký");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thành công");
                            alert.setHeaderText("");
                            alert.setContentText("Bạn đăng ký tham gia hội nghị này trước đó!");
                            alert.showAndWait();
                       }
                       
                    transaction.commit();
                    session.close();
                    return true;
                   }
               }
            }
        }
        
        transaction.commit();
        session.close();
        return false;
    }

    private void leaveConference() {
        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        String hql = "select a from Account a where idAccount = :idAccount";
        Query query = session.createQuery(hql);
        query.setInteger("idAccount", acc.getIdAccount());
        List<Account> list = query.list();
        transaction.commit();

        if(list.size() == 0){
             return;
        }else{
            for(int i = 0; i < list.size(); ++i){
               Iterator<Thamgiahoinghi> confs = list.get(i).getThamgiahoinghis().iterator();
               while(confs.hasNext()){
                    Thamgiahoinghi tg = confs.next();
                    if(tg.getHoinghi().getIdHoiNghi() == currentConference.getIdHoiNghi()){
                        ThamgiahoinghiId tgid = new ThamgiahoinghiId(acc.getIdAccount(), currentConference.getIdHoiNghi());
                        transaction = session.beginTransaction();
                        String hql2 = "delete from Thamgiahoinghi where id = :id";
                        Query query2 = session.createQuery(hql2);
                        query2.setParameter("id", tgid);
                        int count = query2.executeUpdate();
                        if(count > 0){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thông báo");
                            alert.setHeaderText("");
                            alert.setContentText("Bạn dã hủy tham gia hội nghị!");
                            transaction.commit();
                            session.close();
                            alert.showAndWait();
                            join.setText("Đăng ký");
                            leave = false;
                            return;
                        }else{
                            transaction.rollback();
                            session.close();
                            return;
                        }
                   }
               }
            }
        }
        
        transaction.commit();
        session.close();
    }
    
        @FXML
    private Button list_joined;

    @FXML
    void list_joined_click(ActionEvent event) {
        Dialog<?> list = new Dialog<>();
        list.initOwner(anchorPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/list_joined.fxml"));

        try {
            list.getDialogPane().setContent(fxmlLoader.load());
            ListJoined listJoined = fxmlLoader.getController();
            listJoined.getData(currentConference.getThamgiahoinghis(), currentConference);
            list.initStyle(StageStyle.DECORATED);
            list.setResizable(false);
            list.getDialogPane().setPrefSize(600, 700);


        } catch(IOException ex) {
            System.out.println("Couldn't load the dialog");
            ex.printStackTrace();
            return;
        }

        Window window = list.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(ev -> initialize());
        list.showAndWait();
    }
}
