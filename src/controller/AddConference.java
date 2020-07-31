package controller;

import connection.Connection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Diadiem;
import pojos.Hoinghi;

public class AddConference {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView img;

    @FXML
    private Button btn_choose_img;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<Integer> hour;

    @FXML
    private ComboBox<Integer> minute;

    @FXML
    private ComboBox<Integer> duration;

    @FXML
    private ComboBox<Diadiem> diadiem;

    @FXML
    private TextArea short_des;

    @FXML
    private TextArea description;

    @FXML
    private TextField title;

    @FXML
    private Button add_btn;

    private Image image;
    private File file;
    private FileChooser fileChooser;

    private String titleConf;
    private Date dateConf;
    private Integer hourCof;
    private Integer minuteConf;
    private Integer durationConf;
    private String shortDesConf;
    private String desConf;
    private String dateTime;
    private Diadiem placeSelected;
    
    private Hoinghi currentConference;
    
    private boolean validDateTime = false;


    @FXML
    void initialize() {

        setData();

        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.jpeg")
        );


        btn_choose_img.setOnAction(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            file = fileChooser.showOpenDialog(stage);
            if (file != null){
                image = new Image(file.toURI().toString(), 330,265,true, true);
                img.setImage(image);
                img.setFitWidth(300);
                img.setFitHeight(250);
                img.setPreserveRatio(true);

            }
        });

        add_btn.setOnMouseClicked(e -> {
            getInfo();

            if (image == null || titleConf.equals("") || dateTime.equals("") || shortDesConf.equals("") ||
                desConf.equals("") || durationConf == null || hourCof == null || minuteConf == null || placeSelected == null){
                if(!validDateTime){
                    notiFailDate();
                    return;
                }
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Không hợp lệ");
                alert2.setHeaderText(null);
                alert2.setContentText("Vui lòng điền đẩy đủ thông tin và chọn hình ảnh!");
                alert2.showAndWait();
            }else{
                try {
                    byte[] imageConf = Files.readAllBytes(file.toPath());
                    Connection connection = new Connection();
                    SessionFactory sessionFactory = connection.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction transaction = null;

                    Hoinghi newConf = new Hoinghi(placeSelected, titleConf, shortDesConf, desConf, imageConf, dateConf, durationConf, true);
                    transaction = session.beginTransaction();
                    session.save(newConf);
                    transaction.commit();
                    transaction = null;
                    session.close();
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful");
                    alert.setContentText("Đã thêm hội nghị thành công!");

                    alert.showAndWait();

                    Stage stage = (Stage) add_btn.getScene().getWindow();
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(AddConference.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void setData() {
        ObservableList<Integer> observableListHour = FXCollections.observableArrayList();
        observableListHour.addAll(7,8,9,10,11,13,14,15,16,17);
        hour.setItems(observableListHour);

        ObservableList<Integer> observableListMinute = FXCollections.observableArrayList();
        observableListMinute.addAll(0,15,30,45);
        minute.setItems(observableListMinute);

        ObservableList<Integer> observableListDuration = FXCollections.observableArrayList();
        observableListDuration.addAll(1,2,3,4);
        duration.setItems(observableListDuration);
        
         // Dia diem
        
        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        String hql = "select d from Diadiem d";
        Query query = session.createQuery(hql);

        List<Diadiem> list = query.list();
        transaction.commit();
        transaction = null;
        session.close();
        
        ObservableList<Diadiem> observablePlace = FXCollections.observableArrayList();
        for(int i = 0; i < list.size(); ++i){
            Diadiem place = list.get(i);
            observablePlace.add(place);
        }
        diadiem.setItems(observablePlace);     
    }

    private void getInfo() {
        titleConf = title.getText().trim();
        LocalDate localDate = date.getValue();
//        if (localDate == null){
//            Alert alert2 = new Alert(Alert.AlertType.ERROR);
//            alert2.setTitle("Không hợp lệ");
//            alert2.setHeaderText(null);
//            alert2.setContentText("Vui lòng chọn thời gian bắt đầu hội nghị!");
//            alert2.showAndWait();
//        }
        Instant instant = null;
        try{
            instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            dateConf = Date.from(instant);
            hourCof = hour.getValue();
            minuteConf = minute.getValue();
            durationConf = duration.getValue();
            shortDesConf = short_des.getText();
            desConf = description.getText();
            placeSelected = diadiem.getValue();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String temp = sdf.format(dateConf);
                temp = temp.substring(0, 10);
                temp += " "+ hourCof +":"+minuteConf+":00";
                dateConf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(temp);
                dateTime = sdf.format(dateConf);
            }catch(ParseException e){
                System.out.println("Lỗi chuyển đổi ngày");
            }
        }catch(Exception e){
            
        }
        
    }
    
    @FXML
    void dateChoose(ActionEvent event) {
        timeCheck();
    }

    @FXML
    void durationChoose(ActionEvent event) {
        timeCheck();
    }

    @FXML
    void hourChoose(ActionEvent event) {
        timeCheck();
    }

    @FXML
    void minuteChoose(ActionEvent event) {
        timeCheck();
    }

    @FXML
    void placeChoose(ActionEvent event) {
        timeCheck();
    }
    
    private void timeCheck(){
        getInfo();
        validDateTime = false;
        if(date.equals("") || hourCof == null || minuteConf == null || durationConf == null || placeSelected == null){
            return;
        }
        LocalDate localDate = date.getValue();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      
        String startStr = localDate + " " + hourCof + ":" + minuteConf + ":00";
        String endStr =  localDate + " " + (hourCof + durationConf) + ":" + minuteConf + ":00";
        
        try {
            Date start = sdf.parse(startStr);
            Date end = sdf.parse(endStr);
            
            Connection connection = new Connection();
            SessionFactory sessionFactory = connection.getSessionFactory();
            Session session = sessionFactory.openSession();
            Transaction transaction = null;

            transaction = session.beginTransaction();
            String hql = "from Hoinghi h where diadiem = :place";
            Query query = session.createQuery(hql);
            query.setParameter("place", placeSelected);

            List<Hoinghi> list = query.list();
            
            for(int i = 0; i < list.size(); ++i){
                Date dateStart = list.get(i).getThoiGian();
                Integer dur = list.get(i).getKhoangThoiGian();
                String dateStartStr = sdf.format(dateStart);
                String dateEndStr = dateStartStr.substring(0,11) + (Integer.valueOf(dateStartStr.substring(11,13)) + dur) + dateStartStr.substring(13);
                Date startDate = sdf.parse(dateStartStr);
                Date endDate = sdf.parse(dateEndStr);
                
                if((start.compareTo(startDate) > 0 && start.compareTo(endDate) < 0) ||
                        (end.compareTo(startDate) > 0 && end.compareTo(endDate) < 0)){
                    notiFailDate();
                    return;
                }
                if(start.compareTo(startDate) < 0 && end.compareTo(endDate) > 0){
                    notiFailDate();
                    return;
                }
                if((start.compareTo(startDate) < 0 && end.compareTo(endDate) > 0) || 
                        (start.compareTo(endDate) < 0 && end.compareTo(endDate) > 0)){
                    notiFailDate();
                    return;
                }
            }
            validDateTime = true;
            transaction.commit();
            transaction = null;
            session.close();
            
            
        } catch (ParseException ex) {
            Logger.getLogger(AddConference.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(validDateTime);
        
    }
    
    private void notiFailDate(){
        if(!validDateTime){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Không hợp lệ");
            alert.setHeaderText(null);
            alert.setContentText("Thời gian lựa chọn đã có hội nghị diễn ra tại địa điểm!");
            alert.showAndWait();
        }
    }
    
}
