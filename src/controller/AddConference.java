package controller;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pojos.Diadiem;

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
    private Diadiem diadiemConf;
    private String shortDesConf;
    private String desConf;
    private String dateTime;

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
            System.out.println(file.toURI().toString());
            if (file != null){
                image = new Image(file.toURI().toString(), 330,265,true, true);
                img.setImage(image);
                img.setFitWidth(330);
                img.setFitHeight(265);
                img.setPreserveRatio(true);

            }
        });

        add_btn.setOnMouseClicked(e -> {
            try {
                getInfo();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }

            if (image == null || titleConf.equals("") || dateTime.equals("") || shortDesConf.equals("") ||
                desConf.equals("") || durationConf == null || hourCof == null || minuteConf == null){
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Không hợp lệ");
                alert2.setHeaderText(null);
                alert2.setContentText("Vui lòng điền đẩy đủ thông tin và chọn hình ảnh!");
                alert2.showAndWait();
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
    }

    private void getInfo() throws ParseException {
        titleConf = title.getText().trim();
        LocalDate localDate = date.getValue();
        if (localDate == null){
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Không hợp lệ");
            alert2.setHeaderText(null);
            alert2.setContentText("Vui lòng chọn thời gian bắt đầu hội nghị!");
            alert2.showAndWait();
        }
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        dateConf = Date.from(instant);
        hourCof = hour.getValue();
        minuteConf = minute.getValue();
        durationConf = duration.getValue();
        shortDesConf = short_des.getText();
        desConf = description.getText();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String temp = sdf.format(dateConf);
        temp = temp.substring(0, 10);
        temp += " "+ hourCof +":"+minuteConf+":00";
        dateConf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(temp);
        dateTime = sdf.format(dateConf);

        // Dia diem
    }
}
