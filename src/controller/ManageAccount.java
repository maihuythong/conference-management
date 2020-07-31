package controller;

import account.StorageAccount;
import connection.Connection;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.Account;

/**
 *
 * @author Huy Thông
 */
public class ManageAccount {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Account> table;

    @FXML
    private TableColumn<Account, Integer> idCol;

    @FXML
    private TableColumn<Account, String> nameCol;

    @FXML
    private TableColumn<Account, String> usernameCol;

    @FXML
    private TableColumn<Account, String> emailCol;

    @FXML
    private TableColumn<Account, String> statusCol;

    @FXML
    private TextField search_txt;

    @FXML
    private ImageView search_btn;

    private ObservableList<Account> observableList;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private CheckBox cbId;

    @FXML
    private CheckBox cbName;

    @FXML
    private CheckBox cbUsername;

    @FXML
    private CheckBox cbEmail;
    
    private List<Account> accounts;
    private List<Account> listAccount = new ArrayList<>();

    @FXML
    void initialize() {

        table.setPlaceholder(new Label("Không có dữ liệu tài khoản"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("idAccount"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        getAccountData();

        table.setItems(observableList);

        table.setRowFactory(acc -> {
            final TableRow<Account> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem detailItem = new MenuItem("Chi tiết");
            MenuItem blockItem = new MenuItem("Chặn/Bỏ chặn");
            detailItem.setOnAction(e -> {
                Account accountSelected = table.getSelectionModel().getSelectedItem();
                Dialog<?> accountDialog = new Dialog<>();
                accountDialog.initOwner(anchorPane.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/user_info.fxml"));

                try {
                    accountDialog.getDialogPane().setContent(fxmlLoader.load());
                    UserInfo userInfo = fxmlLoader.getController();
                    userInfo.getData(accountSelected);
                    accountDialog.initStyle(StageStyle.DECORATED);
                    accountDialog.setResizable(false);
                    accountDialog.getDialogPane().setPrefSize(600, 300);


                } catch(IOException ex) {
                    System.out.println("Couldn't load the dialog");
                    ex.printStackTrace();
                    return;
                }

                Window window = accountDialog.getDialogPane().getScene().getWindow();
                window.setOnCloseRequest(event -> initialize());

                accountDialog.show();
            });

            blockItem.setOnAction(e -> {
                Account accountSelected = table.getSelectionModel().getSelectedItem();
                
                Connection connection = new Connection();
                SessionFactory sessionFactory = connection.getSessionFactory();
                Session session = sessionFactory.openSession();
                Transaction transaction = null;
       
                transaction = session.beginTransaction();
                String hql = "update Account set active = :status where idAccount = :id";
                Query query = session.createQuery(hql);
                query.setParameter("status", !accountSelected.isActive());
                query.setParameter("id", accountSelected.getIdAccount());

                int result = query.executeUpdate();
                if(result > 0){
                    transaction.commit();
                    getAccountData();
                    initialize();
                }else{
                    transaction.rollback();
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Fail");
                    alert2.setHeaderText("");
                    alert2.setContentText("Có lỗi gì đó xảy ra! Bạn vui lòng thử lại!");
                    alert2.showAndWait();
                }
                
                transaction = null;
                
            });

            rowMenu.getItems().addAll(detailItem);
            rowMenu.getItems().addAll(blockItem);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu)null));

//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
//                    System.out.println("click 2");
//                }
//            });

            return row;
        });

    }

    private void getAccountData(){
        
        observableList = FXCollections.observableArrayList();
        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        String hql = "select a from Account a where a.isAdmin = :isAdmin";
        Query query = session.createQuery(hql);
        query.setParameter("isAdmin", false);

        accounts = query.list();

        transaction.commit();
        transaction = null;

        for(int i = 0; i < accounts.size(); ++i){
            Account acc = accounts.get(i);
            Account temp = new Account(acc.getIdAccount(), acc.getHoTen(), acc.getUsername(), acc.getEmail(), acc.isActive(), acc.getThamgiahoinghis());
            observableList.add(temp);
            listAccount.add(temp);
        }
    }
    
     @FXML
    void emailAction(ActionEvent event) {
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
    void unameAction(ActionEvent event) {
        searchKeyWord();
    }

    
    @FXML
    void searchKeyWord() {
        if(cbId.isSelected() || cbName.isSelected() || cbUsername.isSelected() || cbEmail.isSelected()){
            Runnable  search = new Runnable (){
                @Override
                public void run() {
                    filterItems();
                }
            };
        
            new Thread(search).start();
        }else{
            observableList.clear();
            observableList.addAll(listAccount);
            table.setItems(observableList);
        }
    }
    
    private void filterItems(){
        String searchText = search_txt.getText().trim();
        if(searchText.equals("")){
            initialize();
            return;
        }
        
        observableList.clear();
        List<Account> newAccounts = new ArrayList<>(listAccount);
        
        if(cbId.isSelected()){
            searchbyId(newAccounts, searchText);
        }
        
        if(cbName.isSelected()){
            searchByName(newAccounts, searchText);
        }
         
        if(cbUsername.isSelected()){
            searchByUsername(newAccounts, searchText);
        }
        
        if(cbEmail.isSelected()){
            searchByEmail(newAccounts, searchText);
        }

        table.setItems(observableList);

    }
    
    private boolean isContainById(Integer id){
        for(Account a: observableList){
            if(a.getIdAccount() == id){
                return true;
            }
        }
        
        return false;
    }
    
    private void searchbyId(List<Account> current, String searchText){
        try {
            List<Account> aIds = current.stream().filter(account -> account.getIdAccount() == Integer.valueOf(searchText)).collect(Collectors.toList());

            for(int i = 0; i < aIds.size(); ++i){
                Account acc = aIds.get(i);
                Account temp = new Account(acc.getIdAccount(), acc.getHoTen(), acc.getUsername(), acc.getEmail(), acc.isActive(), acc.getThamgiahoinghis());
                observableList.add(temp);
            }

        }catch(NumberFormatException e){
            System.out.println("Lỗi số");
        }
    }

    
    
    
    private void searchByName(List<Account> current, String searchText) {
        List<Account> aIds = current.stream().filter(account -> account.getHoTen().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Account acc = aIds.get(i);
            if(!isContainById(acc.getIdAccount())){
                Account temp = new Account(acc.getIdAccount(), acc.getHoTen(), acc.getUsername(), acc.getEmail(), acc.isActive(), acc.getThamgiahoinghis());
                observableList.add(temp);
            }
        }
    }

    private void searchByUsername(List<Account> current, String searchText) {
        List<Account> aIds = current.stream().filter(account -> account.getUsername().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Account acc = aIds.get(i);
            if(!isContainById(acc.getIdAccount())){
                Account temp = new Account(acc.getIdAccount(), acc.getHoTen(), acc.getUsername(), acc.getEmail(), acc.isActive(), acc.getThamgiahoinghis());
                observableList.add(temp);
            }
        }
    }

    private void searchByEmail(List<Account> current, String searchText) {
        List<Account> aIds = current.stream().filter(account -> account.getEmail().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());

        for(int i = 0; i < aIds.size(); ++i){
            Account acc = aIds.get(i);
            if(!isContainById(acc.getIdAccount())){
                Account temp = new Account(acc.getIdAccount(), acc.getHoTen(), acc.getUsername(), acc.getEmail(), acc.isActive(), acc.getThamgiahoinghis());
                observableList.add(temp);
            }
        }
    }
}
