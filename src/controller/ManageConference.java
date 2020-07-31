package controller;

import connection.Connection;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;
import pojos.Diadiem;
import pojos.Hoinghi;


public class ManageConference {
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button add_btn;

    @FXML
    private TextField search_txt;

    @FXML
    private ImageView search_btn;

    @FXML
    private CheckBox cbId;

    @FXML
    private CheckBox cbName;

    @FXML
    private CheckBox cbPlace;

    @FXML
    private CheckBox cbSize;

    @FXML
    private CheckBox cbCurrentSize;

    @FXML
    private TableView<Hoinghi> table;

    @FXML
    private TableColumn<Hoinghi, Integer> id;

    @FXML
    private TableColumn<Hoinghi, String> name;

    @FXML
    private TableColumn<Hoinghi, String> place;

    @FXML
    private TableColumn<Hoinghi, String> time;

    @FXML
    private TableColumn<Hoinghi, Integer> duration;

    @FXML
    private TableColumn<Hoinghi, Integer> size;

    @FXML
    private TableColumn<Hoinghi, Integer> current;

    @FXML
    private TableColumn<Hoinghi, String> status;

    @FXML
    private ComboBox<String> cbbStatus;
    
    @FXML
    void addrAction(ActionEvent event) {
        searchKeyWord();
    }

    @FXML
    void currentSizeAction(ActionEvent event) {
        searchKeyWord();
    }

    @FXML
    void fromDateAction(ActionEvent event) {
        searchKeyWord();
    }

    @FXML
    void idAction(ActionEvent event) {
        searchKeyWord();
    }

    @FXML
    void nameAction(ActionEvent event) {
        searchKeyWord();
    }

    @FXML
    void searchKeyWord() {
        if(cbId.isSelected() || cbName.isSelected() || cbPlace.isSelected() || cbSize.isSelected() || cbCurrentSize.isSelected()){
            Runnable  search = new Runnable (){
                @Override
                public void run() {
                    filterItems();
                }
            };
        
            new Thread(search).start();
        }else{
            observableList.clear();
            observableList.addAll(conferences);
            table.setItems(observableList);
        }
    }
    
    
    private void filterItems(){
        String searchText = search_txt.getText().trim();
        if(searchText.equals("")){
            initialize();
            return;
        }
        
        List<Hoinghi> newConferences = new ArrayList<>(conferences);
        observableList.clear();
        
        if(cbId.isSelected()){
            searchbyId(newConferences, searchText);
        }
        
        if(cbName.isSelected()){
            searchByName(newConferences, searchText);
        }
         
        if(cbPlace.isSelected()){
            searchByPlace(newConferences, searchText);
        }
        
        if(cbSize.isSelected()){
            searchBySize(newConferences, searchText);
        }
        
        if(cbCurrentSize.isSelected()){
            searchByCurrentSize(newConferences, searchText);
        }

        table.setItems(observableList);

    }

    @FXML
    void sizeAction(ActionEvent event) {
        searchKeyWord();
    }

    @FXML
    void statusAction(ActionEvent event) throws ParseException {
        String itemSelected = cbbStatus.getValue();
        if(!itemSelected.equals("")){
            List<Hoinghi> newConferences = new ArrayList<>(conferences);
            observableList.clear();
            if(itemSelected.equals("Đã diễn ra")){
                List<Hoinghi> aIds = newConferences.stream().filter(conf -> conf.getStatus().equals("Đã diễn ra")).collect(Collectors.toList());
                for(int i = 0; i < aIds.size(); ++i){
                    Hoinghi temp = aIds.get(i);
                    if(!isContainById(temp.getIdHoiNghi())){
                        observableList.add(temp);
                    }
                }
            }

            if(itemSelected.equals("Chưa diễn ra")){
                List<Hoinghi> aIds = newConferences.stream().filter(conf -> conf.getStatus().equals("Chưa diễn ra")).collect(Collectors.toList());
                for(int i = 0; i < aIds.size(); ++i){
                    Hoinghi temp = aIds.get(i);
                    if(!isContainById(temp.getIdHoiNghi())){
                        observableList.add(temp);
                    }
                }
            }
        }else {
            initialize();
        }          
    }

    @FXML
    void toDateAction(ActionEvent event) {

    }
    
    ObservableList<Hoinghi> observableList;

    List<Hoinghi> conferences = new ArrayList();

    @FXML
    void initialize(){
        table.setPlaceholder(new Label("Không có dữ liệu hội nghị"));
        id.setCellValueFactory(new PropertyValueFactory<>("idHoiNghi"));
        name.setCellValueFactory(new PropertyValueFactory<>("tenHoiNghi"));
        place.setCellValueFactory(new PropertyValueFactory<>("fullAddress"));
        time.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        duration.setCellValueFactory(new PropertyValueFactory<>("khoangThoiGian"));
        size.setCellValueFactory(new PropertyValueFactory<>("maxSize"));
        current.setCellValueFactory(new PropertyValueFactory<>("joined"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        getConferenceData();

        table.setItems(observableList);

        table.setRowFactory(acc -> {
            final TableRow<Hoinghi> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem detailItem = new MenuItem("Chi tiết");
            MenuItem editItem = new MenuItem("Sửa");
            MenuItem listAttend = new MenuItem("Danh sách tham dự");
            detailItem.setOnAction(e -> {
                Hoinghi confSelected = table.getSelectionModel().getSelectedItem();
                Dialog<?> confDialog = new Dialog<>();
                confDialog.getDialogPane().setPrefSize(900, 700);
                confDialog.initOwner(anchorPane.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/conference_info.fxml"));

                try {
                    confDialog.getDialogPane().setContent(fxmlLoader.load());
                    ConferenceInfo confInfo = fxmlLoader.getController();
                    confInfo.getData(confSelected);
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

                confDialog.show();
            });

            editItem.setOnAction(e -> {
                Hoinghi confSelected = table.getSelectionModel().getSelectedItem();
                if(confSelected.getStatus().equals("Đã diễn ra")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("");
                    alert.setContentText("Không thể sửa đổi hội nghị đã diễn ra!");
                    alert.showAndWait();
                    return;
                }
                Dialog<?> confDialog = new Dialog<>();
                confDialog.initOwner(anchorPane.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/edit_conference.fxml"));

                try {
                    confDialog.getDialogPane().setContent(fxmlLoader.load());
                    EditConference confInfo = fxmlLoader.getController();
                    confInfo.getData(confSelected);
                    confDialog.initStyle(StageStyle.DECORATED);
                    confDialog.setResizable(false);
                    confDialog.getDialogPane().setPrefSize(900, 700);
                } catch(IOException ex) {
                    System.out.println("Couldn't load the dialog");
                    ex.printStackTrace();
                    return;
                }

                Window window = confDialog.getDialogPane().getScene().getWindow();
                window.setOnCloseRequest(event -> initialize());

                confDialog.show();
            });

            listAttend.setOnAction(e -> {
                Dialog<?> list = new Dialog<>();
                list.initOwner(anchorPane.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/list_joined.fxml"));

                try {
                    Hoinghi confSelected = table.getSelectionModel().getSelectedItem();
                    list.getDialogPane().setContent(fxmlLoader.load());
                    ListJoined listJoined = fxmlLoader.getController();
                    listJoined.getData(confSelected.getThamgiahoinghis(), confSelected);
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
            });
            
            rowMenu.getItems().addAll(detailItem, editItem, listAttend);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu)null));
            

            return row;
        });
        
        //Combo status
        ObservableList<String> observableListStatus = FXCollections.observableArrayList();
        observableListStatus.addAll("", "Đã diễn ra", "Chưa diễn ra");
        cbbStatus.setItems(observableListStatus);
        
        
       
    }

    @FXML
    void add_btn_click(ActionEvent event) {
        Dialog<?> dialog = new Dialog<>();
        dialog.initOwner(anchorPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/conference_add.fxml"));
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
        window.setOnCloseRequest(e -> initialize());

        dialog.show();
        initialize();
    }
    
    private void getConferenceData(){
        observableList = FXCollections.observableArrayList();
        conferences.clear();
        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        String hql = "from Hoinghi hn left join fetch hn.diadiem";
        Query query = session.createQuery(hql);

        List<?> list = query.list();
        transaction.commit();
        transaction = null;
        session.close();

        for(int i = 0; i < list.size(); ++i){
            Hoinghi conf = (Hoinghi)list.get(i);
            Diadiem temp = conf.getDiadiem();
            Hoinghi tempConf = new Hoinghi( conf.getIdHoiNghi(), temp, conf.getTenHoiNghi(), conf.getMoTaNgan(), conf.getMoTaChiTiet(), conf.getHinhAnh(), conf.getThoiGian(), conf.getKhoangThoiGian(), conf.getNguoiThamDu(), conf.isActive(), conf.getThamgiahoinghis());
            observableList.add(tempConf);
            conferences.add(tempConf);
        }
    }

    private void searchbyId(List<Hoinghi> newConferences, String searchText) {
        try {
            List<Hoinghi> aIds = newConferences.stream().filter(conf -> conf.getIdHoiNghi()== Integer.valueOf(searchText)).collect(Collectors.toList());

            for(int i = 0; i < aIds.size(); ++i){
                Hoinghi conf = aIds.get(i);
                if(!isContainById(conf.getIdHoiNghi()))
                    observableList.add(conf);
            }

        }catch(NumberFormatException e){
            System.out.println("Lỗi số");
        }
    }
    
    private boolean isContainById(Integer id){
        for(Hoinghi a: observableList){
            if(a.getIdHoiNghi()== id){
                return true;
            }
        }
        
        return false;
    }

    private void searchByName(List<Hoinghi> newConferences, String searchText) {
        List<Hoinghi> aIds = newConferences.stream().filter(conf -> conf.getTenHoiNghi().toLowerCase().contains(searchText)).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Hoinghi temp = aIds.get(i);
            if(!isContainById(temp.getIdHoiNghi())){
                observableList.add(temp);
            }
        }
    }

    private void searchByPlace(List<Hoinghi> newConferences, String searchText) {
        List<Hoinghi> aIds = newConferences.stream().filter(conf -> conf.getFullAddress().toLowerCase().contains(searchText)).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Hoinghi temp = aIds.get(i);
            if(!isContainById(temp.getIdHoiNghi())){
                observableList.add(temp);
            }
        }
    }

    private void searchBySize(List<Hoinghi> newConferences, String searchText) {
         try {
            List<Hoinghi> aIds = newConferences.stream().filter(conf -> Objects.equals(conf.getMaxSize(), Integer.valueOf(searchText))).collect(Collectors.toList());

            for(int i = 0; i < aIds.size(); ++i){
                Hoinghi temp = aIds.get(i);
                if(!isContainById(temp.getIdHoiNghi())){
                    observableList.add(temp);
                }
            }
        }catch(NumberFormatException e){
            System.out.println("Lỗi số");
        }
    }

    private void searchByCurrentSize(List<Hoinghi> newConferences, String searchText) {
        List<Hoinghi> aIds = newConferences.stream().filter(conf -> Objects.equals(conf.getCurrentSize(), Integer.valueOf(searchText))).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Hoinghi temp = aIds.get(i);
            if(!isContainById(temp.getIdHoiNghi())){
                observableList.add(temp);
            }
        }
    }

}
