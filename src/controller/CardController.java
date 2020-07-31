package controller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import pojos.Hoinghi;

public class CardController extends ListCell<Hoinghi> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView conf_img;

    @FXML
    private Text conf_name;

    @FXML
    private TextArea conf_short_des;

    @FXML
    private Text conf_time;

    @FXML
    private Text conf_location;

    @FXML
    private Text conf_max;

    @FXML
    private Text conf_current;

    @FXML
    private Text detail;

    private FXMLLoader fxmlLoader;
    
    private Image image;
    
    @FXML
    private Text duration;

    @FXML
    void initialize() {

    }
    
    @FXML
    void detail(MouseEvent event) {

    }

    @Override
    protected void updateItem(Hoinghi item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null){
            setText(null);
            setGraphic(null);
        }else {
            if (fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/conference_card.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            conf_name.setText(item.getTenHoiNghi());
            conf_short_des.setText(item.getMoTaNgan());
            conf_location.setText(item.getFullAddress());
            conf_max.setText(String.valueOf(item.getDiadiem().getSucChua()));
            conf_current.setText(String.valueOf(item.getJoined()));
            duration.setText(String.valueOf(item.getKhoangThoiGian()) + " giờ");
            conf_time.setText(item.getDateString());
            conf_max.setText(String.valueOf(item.getDiadiem().getSucChua()));
            
            System.out.println(item.getThamgiahoinghis().size());
            
            byte []ig = item.getHinhAnh();
            if(ig != null){
                ByteArrayInputStream bais = new ByteArrayInputStream(item.getHinhAnh());
                image = new Image(bais);
                conf_img.setImage(image);
                conf_img.setFitWidth(300);
                conf_img.setFitHeight(250);
                conf_img.setPreserveRatio(true);
            }

            setText(null);
            setGraphic(anchorPane);
        }
    }
}
