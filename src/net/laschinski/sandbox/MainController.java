package net.laschinski.sandbox;

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
			Parent root = FXMLLoader.load(getClass().getResource("/net/laschinski/sandbox/ui/xor/XOR.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void conwayButtonPressed(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/net/laschinski/sandbox/ui/conway/Conway.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}