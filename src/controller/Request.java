
package controller;

import connection.Connection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pojos.RequestModel;
import pojos.Thamgiahoinghi;

public class Request {
 
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<RequestModel> table;

    @FXML
    private TableColumn<RequestModel, Integer> idCol;

    @FXML
    private TableColumn<RequestModel, String> nameCol;

    @FXML
    private TableColumn<RequestModel, String> emailCol;

    @FXML
    private TableColumn<RequestModel, String> confName;

    @FXML
    private TableColumn<RequestModel, String> time;
    
    private ObservableList<RequestModel> observableList;
    
    private List<RequestModel> listData = new ArrayList();

    @FXML
    void initialize() {
        table.setPlaceholder(new Label("Không có yêu cầu tham gia hội nghị"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        confName.setCellValueFactory(new PropertyValueFactory<>("confName"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        getRequestData();
        
        table.setRowFactory(acc -> {
            final TableRow<RequestModel> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem accept = new MenuItem("Chấp nhận");
            MenuItem delete = new MenuItem("Xóa");
            
            accept.setOnAction(e -> {
                Connection connection = new Connection();
                SessionFactory sessionFactory = connection.getSessionFactory();
                Session session = sessionFactory.openSession();
                RequestModel rm = table.getSelectionModel().getSelectedItem();
                Transaction transaction = null;
                Query query = session.createQuery("UPDATE Thamgiahoinghi SET active = true WHERE id = :id");
                query.setParameter("id", rm.getTghoinghiId());
                
                transaction = session.beginTransaction();
                int result = query.executeUpdate();
                    if(result > 0){
                        transaction.commit();
                        transaction = null;

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thành công");
                        alert.setContentText("Đã chấp nhận yêu cầu tham gia");
                        alert.showAndWait();
                        
                        getRequestData();
                        
                    }else {
                        transaction.rollback();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setContentText("Có lỗi xảy ra! Vui lòng thử lại");
                        alert.showAndWait();
                    }
                session.close();
            });

            delete.setOnAction(e -> {
                Connection connection = new Connection();
                SessionFactory sessionFactory = connection.getSessionFactory();
                Session session = sessionFactory.openSession();
                RequestModel req = table.getSelectionModel().getSelectedItem();
                Transaction transaction = null;
                Query query = session.createQuery("DELETE Thamgiahoinghi WHERE id = :id");
                query.setParameter("id", req.getTghoinghiId());
                
                transaction = session.beginTransaction();
                int result = query.executeUpdate();
                    if(result > 0){
                        transaction.commit();
                        transaction = null;

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thành công");
                        alert.setContentText("Đã từ chối yêu cầu tham gia hội nghị");
                        alert.showAndWait();
                        getRequestData();
                    }else {
                        transaction.rollback();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setContentText("Có lỗi xảy ra! Vui lòng thử lại");
                        alert.showAndWait();
                    }
            });

            rowMenu.getItems().addAll(accept, delete);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu)null));

            return row;
        });
    }
    
    private void getRequestData(){
        observableList = FXCollections.observableArrayList();
        
        Connection connection = new Connection();
        SessionFactory sessionFactory = connection.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        String hql = "from Thamgiahoinghi where active = :active";
        Query query = session.createQuery(hql);

        query.setBoolean("active", false);
        
        List<?> list = query.list();
        transaction.commit();
        transaction = null;
        session.close();

        for(int i = 0; i < list.size(); ++i){
            Thamgiahoinghi tg = (Thamgiahoinghi) list.get(i);
            RequestModel req = new RequestModel(tg.getAccount(), tg.getHoinghi());
            listData.add(req);
            observableList.add(req);
        }
        
        table.setItems(observableList);
        
    }
}
