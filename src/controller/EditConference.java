package controller;


import connection.Connection;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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


public class EditConference {
    
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
    private Button save_btn;


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
    private boolean isChangeImage = false;
    
    private boolean validDateTime = false;


    @FXML
    void initialize() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.jpeg")
        );


        btn_choose_img.setOnAction(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            file = fileChooser.showOpenDialog(stage);
            if (file != null){
                isChangeImage = true;
                image = new Image(file.toURI().toString(), 330,265,true, true);
                img.setImage(image);
                img.setFitWidth(300);
                img.setFitHeight(250);
                img.setPreserveRatio(true);

            }
        });

        save_btn.setOnMouseClicked(e -> {
            try {
                getInfo();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
            
            try {
                timeCheck();
            } catch (ParseException ex) {
                Logger.getLogger(EditConference.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println(image == null);
            System.out.println(titleConf.equals("") );
            System.out.println(dateTime.equals("") );
            System.out.println(shortDesConf.equals("") );
            System.out.println(desConf.equals(""));
            System.out.println(durationConf == null);
            System.out.println(hourCof == null );
            System.out.println(minuteConf == null);
            System.out.println(placeSelected == null);
            if(!validDateTime){
                notiFailDate();
                return;
            }
            if (image == null || titleConf.equals("") || dateTime.equals("") || shortDesConf.equals("") ||
                desConf.equals("") || durationConf == null || hourCof == null || minuteConf == null || placeSelected == null){

                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Không hợp lệ");
                alert2.setHeaderText(null);
                alert2.setContentText("Vui lòng điền đẩy đủ thông tin và chọn hình ảnh!");
                alert2.showAndWait();
            }else{
                try {
                    Connection connection = new Connection();
                    SessionFactory sessionFactory = connection.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction transaction = null;
                    Query query;
                    if(isChangeImage){
                        byte[] imageConf = Files.readAllBytes(file.toPath());
                        query = session.createQuery("update Hoinghi set tenHoiNghi = :tenHoiNghi, hinhAnh = :hinhAnh, moTaNgan = :moTaNgan, moTaChiTiet = :moTaChiTiet, thoiGian = :thoiGian, khoangThoiGian = :khoangThoiGian, diadiem = :diadiem where idHoiNghi = :id");
                        query.setParameter("tenHoiNghi", titleConf);
                        query.setParameter("moTaNgan", shortDesConf);
                        query.setParameter("moTaChiTiet", desConf);
                        query.setParameter("thoiGian", dateConf);
                        query.setParameter("khoangThoiGian", durationConf);
                        query.setParameter("diadiem", placeSelected);
                        query.setParameter("id", currentConference.getIdHoiNghi());
                        query.setParameter("hinhAnh", imageConf);
                    }else{
                        query = session.createQuery("update Hoinghi set tenHoiNghi = :tenHoiNghi, moTaNgan = :moTaNgan, moTaChiTiet = :moTaChiTiet, thoiGian = :thoiGian, khoangThoiGian = :khoangThoiGian, diadiem = :diadiem where idHoiNghi = :id");
                        query.setParameter("tenHoiNghi", titleConf);
                        query.setParameter("moTaNgan", shortDesConf);
                        query.setParameter("moTaChiTiet", desConf);
                        query.setParameter("thoiGian", dateConf);
                        query.setParameter("khoangThoiGian", durationConf);
                        query.setParameter("diadiem", placeSelected);
                        query.setParameter("id", currentConference.getIdHoiNghi());
                    }

                    
                    transaction = session.beginTransaction();
                    int result = query.executeUpdate();
                    if(result > 0){
                        transaction.commit();
                        transaction = null;
                        session.close();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successful");
                        alert.setContentText("Thông tin hội nghi đã được chỉnh sửa!");

                        alert.showAndWait();

                        Stage stage = (Stage) save_btn.getScene().getWindow();
                        stage.close();
                    }else {
                        transaction.rollback();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Successful");
                        alert.setContentText("Có lỗi xảy ra!");
                        alert.showAndWait();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(AddConference.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    void getData(Hoinghi ConfSelected) {
        currentConference = ConfSelected;
        
        title.setText(currentConference.getTenHoiNghi());
//        time.setText(currentConference.getDateString());
        duration.setValue(currentConference.getKhoangThoiGian());
        diadiem.setValue(currentConference.getDiadiem());
        short_des.setText(currentConference.getMoTaNgan());
        description.setText(currentConference.getMoTaChiTiet());
        byte []ig = currentConference.getHinhAnh();
        if(ig != null){
            ByteArrayInputStream bais = new ByteArrayInputStream(currentConference.getHinhAnh());
            image = new Image(bais);
            img.setImage(image);
            img.setFitWidth(300);
            img.setFitHeight(250);
            img.setPreserveRatio(true);
        }
        
        setData();

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
        Date temp = currentConference.getThoiGian();
        LocalDateTime  localDate = Instant.ofEpochMilli(temp.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        date.setValue(Instant.ofEpochMilli(temp.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());

        hour.setValue(localDate.getHour());
        minute.setValue(localDate.getMinute());
    }

    private void getInfo() throws ParseException {
        titleConf = title.getText().trim();
        LocalDate localDate = date.getValue();
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
    
    private void notiFailDate(){
        if(!validDateTime){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Không hợp lệ");
            alert.setHeaderText(null);
            alert.setContentText("Thời gian lựa chọn đã có hội nghị diễn ra tại địa điểm!");
            alert.showAndWait();
        }
    }
    
    
    @FXML
    void dateChoose(ActionEvent event) throws ParseException {
        timeCheck();
    }

    @FXML
    void durationChoose(ActionEvent event) throws ParseException {
        timeCheck();
    }

    @FXML
    void hourChoose(ActionEvent event) throws ParseException {
        timeCheck();
    }

    @FXML
    void minuteChoose(ActionEvent event) throws ParseException {
        timeCheck();
    }

    @FXML
    void placeChoose(ActionEvent event) throws ParseException {
        timeCheck();
    }
    
    
    private void timeCheck() throws ParseException{
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
}
