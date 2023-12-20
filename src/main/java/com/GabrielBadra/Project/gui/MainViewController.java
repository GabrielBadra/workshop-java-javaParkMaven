package com.GabrielBadra.Project.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.GabrielBadra.Project.gui.util.Alerts;
import com.GabrielBadra.Project.model.service.DepartmentService;
import com.GabrielBadra.Project.model.service.SellerService;

import application.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemCar;
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		loadView("/com/GabrielBadra/Project/gui/SellerList.fxml",(SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.uptadeTableView();
		});
		
		
	}
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/com/GabrielBadra/Project/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.uptadeTableView();
			});
		}
	
	@FXML
	public void onMenuItemCarAction() {
		loadView("/com/GabrielBadra/Project/gui/ParkingFine.fxml", x -> {});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/com/GabrielBadra/Project/gui/About.fxml", x -> {});
		
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
				
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			Alert alert = Alerts.showAlertVariavel("Loading ...", "Carregando ...", null, AlertType.INFORMATION);

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVbox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			
			Platform.runLater(alert::close);
		}catch(IOException e) {
			Alerts.showAlert("Error", "Error Loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
