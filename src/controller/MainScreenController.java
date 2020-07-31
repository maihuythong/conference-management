package controller;

import account.StorageAccount;
import connection.Connection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;
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
    private AnchorPane card_view;
    
    @FXML
    private AnchorPane list_view;

    @FXML
    private Text account_name;

    @FXML
    private BorderPane account;

    @FXML
    private BorderPane manage;

    @FXML
    private BorderPane about;
    
    @FXML
    private BorderPane dashboard;


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
    private Text duration1;

    @FXML
    private Text des1;

    @FXML
    private Text card_detail1;

    @FXML
    private VBox card11;

    @FXML
    private ImageView card_img2;

    @FXML
    private Text card_title2;

    @FXML
    private Text card_time2;

    @FXML
    private Text card_addr2;

    @FXML
    private Text duration2;

    @FXML
    private Text des2;

    @FXML
    private Text card_detail2;

    @FXML
    private ImageView card_next;

    @FXML
    private ListView<Hoinghi> listview;

    private ObservableList<Hoinghi> hoinghis = FXCollections.observableArrayList();
    private List<Hoinghi> confs = new ArrayList();
    private List<Hoinghi> confsFilter = new ArrayList();
    
    private boolean isCard = true;

    @FXML
    void initialize() {
        
        getConferenceData();
        loadCard();
        StorageAccount sa = StorageAccount.getInstance();
        if(sa.getAccount() != null){
            Account acc = sa.getAccount();
            account_name.setText(acc.getHoTen());
        }
        if(!sa.getIsAdmin()){
            manage.setVisible(false);
        }else{
            manage.setVisible(true);
        }
        
        manage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Parent parent = null;
                try {
                    parent = FXMLLoader.load(getClass().getResource("/view/admin_manage2.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            }
        });


        account.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    actionAccountClick(mouseEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        about.setOnMouseClicked(e -> {
            Parent parent = null;
            try {
                parent = FXMLLoader.load(getClass().getResource("/view/about.fxml"));
            } catch (IOException ev) {
                ev.printStackTrace();
            }
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            stage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
                }
            });

            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        });


        card_view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                isCard = true;
                cardClick(mouseEvent);
            }

        });

        list_view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                isCard = false;
                listClick(mouseEvent);
            }

        });
        
        listview.setBackground(new Background(new BackgroundFill(Color.valueOf("F4F4F4"), null, null))
);
    }

    private void listClick(MouseEvent mouseEvent) {
        cardview.setVisible(false);
        listview.setVisible(true);
        initListView();
    }
    private void cardClick(MouseEvent mouseEvent) {
        listview.setVisible(false);
        cardview.setVisible(true);
        loadCard();
    }

    private void initListView(){

        hoinghis = FXCollections.observableArrayList();
        hoinghis.clear();
        hoinghis.addAll(confsFilter);
        listview.setItems(null);
        listview.setItems(hoinghis);
        listview.setPlaceholder(new Label("Không có hội nghị nào!"));
        listview.setCellFactory(CardController -> new CardController());
        
        listview.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    Hoinghi conf = listview.getSelectionModel().getSelectedItem();
                    Dialog<?> confDialog = new Dialog<>();
                    confDialog.getDialogPane().setPrefSize(900, 700);
                    confDialog.initOwner(borderPane.getScene().getWindow());
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/view/conference_info_main.fxml"));

                    try {
                        confDialog.getDialogPane().setContent(fxmlLoader.load());
                        ConferenceInfoMain confInfo = fxmlLoader.getController();
                        confInfo.getData(conf);
                        confDialog.initStyle(StageStyle.DECORATED);
                        confDialog.setResizable(false);
                        confDialog.getDialogPane().setPrefSize(800,
                                700);
                    } catch(IOException ex) {
                        System.out.println("Couldn't load the dialog");
                        ex.printStackTrace();
                        return;
                    }

                    Window window = confDialog.getDialogPane().getScene().getWindow();
                    window.setOnCloseRequest(ev -> initialize());

                    confDialog.show();
                }
            }
            
        });
    }
    
    private void actionAccountClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/Accountscreen2.fxml"));
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


        Dialog<?> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/manage_account.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initStyle(StageStyle.DECORATED);
            dialog.setResizable(false);
            dialog.getDialogPane().setPrefSize(900, 700);


        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> System.out.println());

        dialog.show();
    }

    private void getConferenceData() {
        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        String hql = "from Hoinghi hn left join fetch hn.diadiem order by hn.thoiGian DESC";
        Query query = session.createQuery(hql);

        List<?> list = query.list();
        transaction.commit();
        transaction = null;
        session.close();

        for(int i = 0; i < list.size(); ++i){
            Hoinghi conf = (Hoinghi)list.get(i);
            Diadiem temp = conf.getDiadiem();
            Set setAccount = conf.getThamgiahoinghis();
            Hoinghi tempConf = new Hoinghi( conf.getIdHoiNghi(), temp, conf.getTenHoiNghi(), conf.getMoTaNgan(), conf.getMoTaChiTiet(), conf.getHinhAnh(), conf.getThoiGian(), conf.getKhoangThoiGian(), conf.getNguoiThamDu(), conf.isActive(), conf.getThamgiahoinghis());
//            if(tempConf.getStatus().equals("Chưa diễn ra")){
                confs.add(tempConf);
                confsFilter.add(tempConf);
//            }
        }
    }
    
    private int pos = 0;
    private Image image1;
    private Image image2;
    
    private void loadCard(){
       if(confsFilter.size() == 0){
           loadDefaultCard1();
           loadDefaultCard2();
       }
       if(confsFilter.size() == 1){
            loadCard1(confsFilter.get(0));
            loadDefaultCard2();
       }
       if(confsFilter.size() >= 2){
            loadCard1(confsFilter.get(0));
            loadCard2(confsFilter.get(1));
       }
       
       card_return.setOnMouseClicked(e -> {
           if(pos != 0){
               loadCard1(confsFilter.get(pos-1));
               loadCard2(confsFilter.get(pos));
               pos--;
           }
       });
       
       card_next.setOnMouseClicked(e -> {
          if(pos < confsFilter.size()-2){
                loadCard1(confsFilter.get(pos+1));
                loadCard2(confsFilter.get(pos+2));
                pos++;
          } 
       });
       
       
    }
    
    private void loadCard1(Hoinghi hn){
        
        ByteArrayInputStream bais = new ByteArrayInputStream(hn.getHinhAnh());
        image1 = new Image(bais, 270, 200, false, true);
        card_img1.setImage(image1);
        card_img1.setFitWidth(270);
        card_img1.setFitHeight(200);
        
        card_img1.setPreserveRatio(true);
        
        card_title1.setText(hn.getTenHoiNghi());
        card_addr1.setText(hn.getFullAddress());
        card_time1.setText(hn.getDateString());
        duration1.setText(String.valueOf(hn.getKhoangThoiGian()));
        des1.setText(hn.getMoTaNgan());
        
        card_detail1.setOnMouseClicked(e -> {
            showConf(hn);
        });
    }
    
    private void loadCard2(Hoinghi hn){
        
        ByteArrayInputStream bais = new ByteArrayInputStream(hn.getHinhAnh());
        image2 = new Image(bais, 270, 200, false, true);
        card_img2.setImage(image2);
        card_img2.setFitWidth(270);
        card_img2.setFitHeight(200);
        card_img2.setPreserveRatio(true);
        card_addr2.setText(hn.getFullAddress());
        card_title2.setText(hn.getTenHoiNghi());
        card_time2.setText(hn.getDateString());
        duration2.setText(String.valueOf(hn.getKhoangThoiGian()));
        des2.setText(hn.getMoTaNgan());
        
        card_detail2.setOnMouseClicked(e -> {
            showConf(hn);
        });
    }
    
    private void loadDefaultCard1(){
        image1 = new Image("/img/default.png");
        card_img1.setImage(image1);
        card_img1.setFitWidth(270);
        card_img1.setFitHeight(200);
        
        card_img1.setPreserveRatio(true);
        
        card_title1.setText("No title");
        card_addr1.setText("");
        card_time1.setText("");
        duration1.setText("");
        des1.setText("");
        
        card_detail1.setOnMouseClicked(e -> {
            return;
        });
    }
    
    private void loadDefaultCard2(){
        image2 = new Image("/img/default.png");
        card_img2.setImage(image2);
        card_img2.setFitWidth(270);
        card_img2.setFitHeight(200);
        
        card_img2.setPreserveRatio(true);
        
        card_title2.setText("No title");
        card_addr2.setText("");
        card_time2.setText("");
        duration2.setText("");
        des2.setText("");
        
        card_detail2.setOnMouseClicked(e -> {
            return;
        });
    }
    
    private void showConf(Hoinghi hn){
        Dialog<?> confDialog = new Dialog<>();
        confDialog.getDialogPane().setPrefSize(900, 700);
        confDialog.initOwner(borderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/conference_info_main.fxml"));

        try {
            confDialog.getDialogPane().setContent(fxmlLoader.load());
            ConferenceInfoMain confInfo = fxmlLoader.getController();
            confInfo.getData(hn);
            confDialog.initStyle(StageStyle.DECORATED);
            confDialog.setResizable(false);
            confDialog.getDialogPane().setPrefSize(800, 700);
        } catch(IOException ex) {
            System.out.println("Couldn't load the dialog");
            ex.printStackTrace();
            return;
        }

        Window window = confDialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> initialize());

        confDialog.showAndWait();
    }
    
    
        @FXML
    void desAction(ActionEvent event) {
        searchKeyWord(null);
    }

    @FXML
    void titleAction(ActionEvent event) {
        searchKeyWord(null);
    }
    
    @FXML
    void searchKeyWord(KeyEvent event) {
        if(title_cb.isSelected() || description_cb.isSelected()){
//            Runnable  search = new Runnable (){
//                @Override
//                public void run() {
                    filterItems();
                    return;
//                }
//            };
//            new Thread(search).start();
        }
            if(confsFilter.size() > 0){
                hoinghis.clear();
                confsFilter.clear();
                confsFilter.addAll(confs);
                hoinghis.addAll(confs);
                if(isCard){
                    listview.setVisible(false);
                    cardview.setVisible(true);
                    loadCard();
                }else{
                    cardview.setVisible(false);
                    listview.setVisible(true);
                    initListView();
                }
            }
        
    }
    
    private void filterItems(){
        String searchText = search_txt.getText().trim();
        if(searchText.equals("")){
            hoinghis.clear();
            confsFilter.clear();
            confsFilter.addAll(confs);
            hoinghis.addAll(confs);
            if(isCard){
                listview.setVisible(false);
                cardview.setVisible(true);
                loadCard();
            }else{
                cardview.setVisible(false);
                listview.setVisible(true);
                initListView();
            }
            return;
        }
        
        List<Hoinghi> newConferences = new ArrayList<>(confs);
        hoinghis.clear();
        confsFilter.clear();

        if(title_cb.isSelected()){
            searchbyTitle(newConferences, searchText);
        }
        
        if(description_cb.isSelected()){
            searchByName(newConferences, searchText);
        }

        if(isCard){
            listview.setVisible(false);
            cardview.setVisible(true);
            loadCard();
        }else{
            cardview.setVisible(false);
            listview.setVisible(true);
            initListView();
        }
        
    }
    
    
    private boolean isContainById(Integer id){
        for(Hoinghi a: confsFilter){
            if(a.getIdHoiNghi()== id){
                return true;
            }
        }
        
        return false;
    }

    private void searchbyTitle(List<Hoinghi> newConferences, String searchText) {
        List<Hoinghi> aIds = newConferences.stream().filter(conf -> conf.getTenHoiNghi().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Hoinghi temp = aIds.get(i);
            if(!isContainById(temp.getIdHoiNghi())){
                confsFilter.add(temp);
            }
        }
    }
    
    private void searchByName(List<Hoinghi> newConferences, String searchText) {
        List<Hoinghi> aIds = newConferences.stream().filter(conf -> conf.getTenHoiNghi().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Hoinghi temp = aIds.get(i);
            if(!isContainById(temp.getIdHoiNghi())){
                confsFilter.add(temp);
            }
        }
    }
    
}
