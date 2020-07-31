/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import account.StorageAccount;
import connection.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
import pojos.Thamgiahoinghi;
import pojos.ThamgiahoinghiId;

public class ListConferenceAccountJoined {
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Hoinghi> table;

    @FXML
    private TableColumn<Hoinghi, Integer> id;

    @FXML
    private TableColumn<Hoinghi, String> name;

    @FXML
    private TableColumn<Hoinghi, Diadiem> place;

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
    
    ObservableList<Hoinghi> observableList;

    List<Hoinghi> conferences = new ArrayList();
    
    private final StorageAccount sa = StorageAccount.getInstance();
    private final Account acc = sa.getAccount();


    @FXML
    void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("idHoiNghi"));
        name.setCellValueFactory(new PropertyValueFactory<>("tenHoiNghi"));
        place.setCellValueFactory(new PropertyValueFactory<>("diadiem"));
        time.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        duration.setCellValueFactory(new PropertyValueFactory<>("khoangThoiGian"));
        size.setCellValueFactory(new PropertyValueFactory<>("maxSize"));
        current.setCellValueFactory(new PropertyValueFactory<>("joined"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        table.setPlaceholder(new Label("Người dùng chưa tham gia hội nghị nào"));
        
        table.setRowFactory(account -> {
            final TableRow<Hoinghi> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem detailItem = new MenuItem("Chi tiết");
            MenuItem out = new MenuItem("Hủy tham dự");
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

            out.setOnAction(e -> {
                Hoinghi confSelected = table.getSelectionModel().getSelectedItem();
                if(confSelected.getStatus().equals("Đã diễn ra")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("");
                    alert.setContentText("Không thể hủy tham gia hội nghị đã diễn ra!");
                    alert.showAndWait();
                    return;
                }
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
                            if(tg.getHoinghi().getIdHoiNghi() == confSelected.getIdHoiNghi()){
                                ThamgiahoinghiId tgid = new ThamgiahoinghiId(acc.getIdAccount(), confSelected.getIdHoiNghi());
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
                                    initialize();
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

            rowMenu.getItems().addAll(detailItem, out, listAttend);
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

    
    public void getConfData(Set<Hoinghi> list){
        Iterator it = list.iterator();
        observableList = FXCollections.observableArrayList();
        while(it.hasNext()){
            Thamgiahoinghi hn = (Thamgiahoinghi) it.next();
            Hoinghi temp = hn.getHoinghi();
            Hoinghi n = new Hoinghi( temp.getIdHoiNghi(), temp.getDiadiem(), temp.getTenHoiNghi(), temp.getMoTaNgan(), temp.getMoTaChiTiet(), temp.getHinhAnh(), temp.getThoiGian(), temp.getKhoangThoiGian(), temp.getNguoiThamDu(), temp.isActive(), temp.getThamgiahoinghis());
            observableList.add(n);
        }
        
        if(observableList.size() != 0){
            table.setItems(observableList);
        }
    }
}
