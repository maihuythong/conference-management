package controller;



import account.StorageAccount;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pojos.Diadiem;
import pojos.Hoinghi;

public class MainScreenController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField search_txt;

    @FXML
    private ImageView search_btn;

    @FXML
    private CheckBox title_cb;

    @FXML
    private CheckBox description_cb;

    @FXML
    private DatePicker date_from;

    @FXML
    private DatePicker date_to;

    @FXML
    private ImageView card_view;

    @FXML
    private ImageView list_view;

    @FXML
    private Text account_name;

    @FXML
    private BorderPane account;

    @FXML
    private BorderPane conference;

    @FXML
    private BorderPane manage_account;

    @FXML
    private HBox cardview;

    @FXML
    private ImageView card_return;

    @FXML
    private VBox card1;

    @FXML
    private ImageView card_img1;

    @FXML
    private Text card_title1;

    @FXML
    private Text card_time1;

    @FXML
    private Text card_addr1;

    @FXML
    private TextArea card_des1;

    @FXML
    private Text card_detail1;

    @FXML
    private VBox card2;

    @FXML
    private ImageView card_img2;

    @FXML
    private Text card_title2;

    @FXML
    private Text card_time2;

    @FXML
    private Text card_addr2;

    @FXML
    private TextArea card_des2;

    @FXML
    private Text card_detail2;

    @FXML
    private ImageView card_next;

    @FXML
    private ListView<Hoinghi> listview;

    private ObservableList<Hoinghi> hoinghis;

    @FXML
    void initialize() {
        
        StorageAccount sa = StorageAccount.getInstance();
        if(!sa.getIsAdmin()){
            conference.setVisible(false);
//            manage_account.setVisible(false);
        }
        


        account.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("click account");
                try {
                    actionAccountClick(mouseEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
//        manage_account.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                System.out.println("click manage");
//                try {
//                    actionManageAccountClick(mouseEvent);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        
        
        card_view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                cardClick(mouseEvent);
            }

        });

        list_view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listClick(mouseEvent);
            }

        });
    }
    
    private void listClick(MouseEvent mouseEvent) {
        cardview.setVisible(false);
        listview.setVisible(true);
        initListView();
    }
    private void cardClick(MouseEvent mouseEvent) {
        listview.setVisible(false);
        cardview.setVisible(true);
    }
    
    private void initListView(){
        Hoinghi hoinghi1 = new Hoinghi();
        hoinghi1.setTenHoiNghi("Sinh hoạt công dân SV 2020");
        hoinghi1.setMoTaNgan("Sinh hoạt cho tân sinh viên 2020");
        hoinghi1.setThoiGian(new Date());
        hoinghi1.setNguoiThamDu(100);

        Diadiem d1 = new Diadiem("Hội trường I, Đại học Khoa học Tự nhiên","227, Nguyễn Văn Cừ, P4, Q5", 250, true);
        hoinghi1.setDiadiem(d1);

        Hoinghi hoinghi2 = new Hoinghi();
        hoinghi2.setTenHoiNghi("Sinh hoạt công dân SV 2020");
        hoinghi2.setMoTaNgan("Sinh hoạt cho tân sinh viên 2020");
        hoinghi2.setThoiGian(new Date());
        hoinghi2.setNguoiThamDu(100);

        Diadiem d2 = new Diadiem("Hội trường I, Đại học Khoa học Tự nhiên","227, Nguyễn Văn Cừ, P4, Q5", 250, true);
        hoinghi2.setDiadiem(d2);

        Hoinghi hoinghi3 = new Hoinghi();
        hoinghi3.setTenHoiNghi("Sinh hoạt công dân SV 2020");
        hoinghi3.setMoTaNgan("Sinh hoạt cho tân sinh viên 2020");
        hoinghi3.setThoiGian(new Date());
        hoinghi3.setNguoiThamDu(100);

        Diadiem d3 = new Diadiem("Hội trường I, Đại học Khoa học Tự nhiên","227, Nguyễn Văn Cừ, P4, Q5", 250, true);
        hoinghi3.setDiadiem(d3);

        hoinghis = FXCollections.observableArrayList();
        hoinghis.add(hoinghi1);
        hoinghis.add(hoinghi2);
        hoinghis.add(hoinghi3);

        listview.setItems(hoinghis);
        listview.setCellFactory(CardController -> new CardController());
    }

    private void actionAccountClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/Accountscreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            }
        });
        
//        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    private void actionManageAccountClick(MouseEvent mouseEvent) throws IOException {
        
        Parent parent = FXMLLoader.load(getClass().getResource("/view/manage_account.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            }
        });
        
//        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
