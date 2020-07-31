package controller;

import connection.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;
import pojos.Diadiem;

public class ManagePlace {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Diadiem> table;

    @FXML
    private TableColumn<Diadiem, Integer> idCol;

    @FXML
    private TableColumn<Diadiem, String> nameCol;

    @FXML
    private TableColumn<Diadiem, String> addrCol;

    @FXML
    private TableColumn<Diadiem, Integer> sizeCol;

    @FXML
    private TextField search_txt;

    @FXML
    private ImageView search_btn;

    @FXML
    private CheckBox cbId;

    @FXML
    private CheckBox cbName;

    @FXML
    private CheckBox cbAddr;

    @FXML
    private CheckBox cbSize;

    @FXML
    private Button add_btn;

    private ObservableList<Diadiem> observableList;

    @FXML
    private ContextMenu listContextMenu;
    
    private List<Diadiem> places;

    @FXML
    void initialize() {
        table.setPlaceholder(new Label("Chưa có dữ liệu địa điểm"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("idDiaDiem"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("tenDiaDiem"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("sucChua"));
        getPlaceData();

        table.setItems(observableList);

        table.setRowFactory(place -> {
            final TableRow<Diadiem> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem detailItem = new MenuItem("Sửa");
            detailItem.setOnAction(e -> {
                Diadiem placeSelected = table.getSelectionModel().getSelectedItem();
                Dialog<?> placeDialog = new Dialog<>();
                placeDialog.initOwner(anchorPane.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/place_info.fxml"));

                try {
                    placeDialog.getDialogPane().setContent(fxmlLoader.load());
                    PlaceInfo placeInfo = fxmlLoader.getController();
                    placeInfo.getData(placeSelected);
                    placeDialog.initStyle(StageStyle.DECORATED);
                    placeDialog.setResizable(false);
                    placeDialog.getDialogPane().setPrefSize(600, 700);


                } catch(IOException ex) {
                    System.out.println("Couldn't load the dialog");
                    ex.printStackTrace();
                    return;
                }

                Window window = placeDialog.getDialogPane().getScene().getWindow();
                window.setOnCloseRequest(event -> initialize());
                placeDialog.showAndWait();
                initialize();
            });


            rowMenu.getItems().addAll(detailItem);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu)null));
            return row;
        });
        
        
        add_btn.setOnMouseClicked(e ->{
            addPlace();
        });
    }

    private void getPlaceData(){
        observableList = FXCollections.observableArrayList();
        
       Connection connection = new Connection();
       SessionFactory sessionFactory = connection.getSessionFactory();
       Session session = sessionFactory.openSession();
       Transaction transaction = null;
       
       transaction = session.beginTransaction();
       String hql = "select a from Diadiem a where a.active = :active";
       Query query = session.createQuery(hql);
       query.setParameter("active", true);
       
       places = query.list();
       
       transaction.commit();
       transaction = null;
       
       for(int i = 0; i < places.size(); ++i){
           Diadiem place = places.get(i);
           observableList.add(place);
       }
    }

    private void addPlace() {
        Dialog<?> dialog = new Dialog<>();
        dialog.initOwner(anchorPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/place_add.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initStyle(StageStyle.DECORATED);
            dialog.setResizable(false);
            dialog.getDialogPane().setPrefSize(600, 400);
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> System.out.println("close"));

        dialog.showAndWait();
        initialize();
    }
    
    
    @FXML
    void searchKeyWord() {
        if(cbId.isSelected() || cbName.isSelected() || cbAddr.isSelected() || cbSize.isSelected()){
            Runnable  search = new Runnable (){
                @Override
                public void run() {
                    filterItems();
                }
            };
        
            new Thread(search).start();
        }else{
            observableList.clear();
            observableList.addAll(places);
            table.setItems(observableList);
        }
    }
    
    private void filterItems(){
        String searchText = search_txt.getText().trim();
        System.out.println(cbId.isSelected());
        if(searchText.equals("")){
            initialize();
            return;
        }
        
        observableList.clear();
        List<Diadiem> newPlaces = new ArrayList<>(places);
        
        if(cbId.isSelected()){
            searchbyId(newPlaces, searchText);
        }
        
        if(cbName.isSelected()){
            searchByName(newPlaces, searchText);
        }
         
        if(cbAddr.isSelected()){
            searchByAddress(newPlaces, searchText);
        }
        
        if(cbSize.isSelected()){
            searchBySize(newPlaces, searchText);
        }

        table.setItems(observableList);

    }
    
    private boolean isContainById(Integer id){
        for(Diadiem a: observableList){
            if(a.getIdDiaDiem()== id){
                return true;
            }
        }
        
        return false;
    }
    
    private void searchbyId(List<Diadiem> current, String searchText){
        try {
            List<Diadiem> aIds = current.stream().filter(place -> place.getIdDiaDiem()== Integer.valueOf(searchText)).collect(Collectors.toList());

            for(int i = 0; i < aIds.size(); ++i){
                Diadiem place = aIds.get(i);
//                Account temp = new Account(acc.getIdAccount(), acc.getHoTen(), acc.getUsername(), acc.getEmail(), acc.isActive());
                observableList.add(place);
            }

        }catch(NumberFormatException e){
            System.out.println("Lỗi số");
        }
    }

    
    
    
    private void searchByName(List<Diadiem> current, String searchText) {
        List<Diadiem> aIds = current.stream().filter(place -> place.getTenDiaDiem().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Diadiem place = aIds.get(i);
            if(!isContainById(place.getIdDiaDiem())){
//                Account temp = new Account(place.getIdDiaDiem(), place.getTenDiaDiem(), place.getDiaChi(), , acc.isActive());
                observableList.add(place);
            }
        }
    }

    private void searchByAddress(List<Diadiem> current, String searchText) {
        List<Diadiem> aIds = current.stream().filter(place -> place.getDiaChi().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Diadiem place = aIds.get(i);
            if(!isContainById(place.getIdDiaDiem())){
//                Account temp = new Account(acc.getIdAccount(), acc.getHoTen(), acc.getUsername(), acc.getEmail(), acc.isActive());
                observableList.add(place);
            }
        }
    }

    private void searchBySize(List<Diadiem> current, String searchText) {
        List<Diadiem> aIds = current.stream().filter(place -> place.getSucChua() == Integer.valueOf(searchText.toLowerCase())).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Diadiem place = aIds.get(i);
            if(!isContainById(place.getIdDiaDiem())){
//                Account temp = new Account(acc.getIdAccount(), acc.getHoTen(), acc.getUsername(), acc.getEmail(), acc.isActive());
                observableList.add(place);
            }
        }
    }
    
    
    @FXML
    void idAction(ActionEvent event) {
        searchKeyWord();
    }
    
    @FXML
    void nameAction(ActionEvent event){
        searchKeyWord();
    }
    
    @FXML
    void addrAction(ActionEvent event){
        searchKeyWord();
    }
    
    @FXML
    void sizeAction(ActionEvent event){
        searchKeyWord();
    }
    
            
}
