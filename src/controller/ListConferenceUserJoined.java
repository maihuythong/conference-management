/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import pojos.Diadiem;
import pojos.Hoinghi;
import pojos.Thamgiahoinghi;

public class ListConferenceUserJoined {
    
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
