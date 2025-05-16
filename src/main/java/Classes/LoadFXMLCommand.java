package Classes;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LoadFXMLCommand implements Command {
    private String fxmlPath;
    private BorderPane mainPane;

    public LoadFXMLCommand(String fxmlPath, BorderPane mainPane) {
        this.fxmlPath = fxmlPath;
        this.mainPane = mainPane;
    }

    @Override
    public void execute() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/pharmacyv1/" + fxmlPath + ".fxml"));
            BorderPane content = loader.load();
            mainPane.setCenter(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}