package quanlyhoinghi;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Huy Thông
 */
public class QuanLyHoiNghi extends Application {
    
    @Override
    public void start(Stage stage) throws Exception { 
        System.out.println(FXMLLoader.load(getClass().getResource("/view/Mainscreen.fxml")).toString());
        Parent root2 = FXMLLoader.load(getClass().getResource("/view/Mainscreen.fxml"));

        stage.setTitle("Quản lý hội nghị");
        stage.setScene(new Scene(root2, 990, 700));
        
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            }
        });
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    
}
