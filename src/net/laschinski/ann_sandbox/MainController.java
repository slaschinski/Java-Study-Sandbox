package net.laschinski.ann_sandbox;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {
	@FXML
	private void xorButtonPressed(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("XOR.fxml"));
		    Stage stage = new Stage();
		    stage.setScene(new Scene(root));
		    stage.show();
		}
		catch (IOException e) {
		    e.printStackTrace();
		}
	}
}