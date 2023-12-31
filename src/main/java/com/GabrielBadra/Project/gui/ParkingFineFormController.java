package com.GabrielBadra.Project.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import com.GabrielBadra.Project.db.DbException;
import com.GabrielBadra.Project.db.DbIntegrityException;
import com.GabrielBadra.Project.gui.listeners.DataChangeListener;
import com.GabrielBadra.Project.gui.util.Alerts;
import com.GabrielBadra.Project.gui.util.JavaMail;
import com.GabrielBadra.Project.gui.util.Utils;
import com.GabrielBadra.Project.model.entities.Car;
import com.GabrielBadra.Project.model.exceptions.ValidationException;

import application.Main;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ParkingFineFormController implements Initializable, DataChangeListener{
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtCarModel;
	
	@FXML
	private TextField txtColor;
	
	@FXML
	private TextField txtPlate;
	
	@FXML
	private  TableView<Car> tableViewCar;

	@FXML
	private TableColumn<Car, String> tableColumnCarModel;

	@FXML
	private TableColumn<Car, String> tableColumnColor;
	
	@FXML
	private TableColumn<Car, String> tableColumnPlate;

	@FXML
	private  TableColumn<Car, Car> tableColumnEDIT;

	@FXML
	private  TableColumn<Car, Car> tableColumnREMOVE;

	private  ObservableList<Car> obsList;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btSendEmail;
	
	private  List<Car> list = new ArrayList<>();
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSendEmail(ActionEvent event) {
		JavaMail javamail = new JavaMail();
		Alert alert = Alerts.showAlertVariavel("Loading", "Enviando...", null, AlertType.INFORMATION);
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
  	    okButton.setDisable(true);
		
			CompletableFuture.runAsync(() -> {	
				try {
					javamail.sendMail(getList(), alert);
					// Fecha o alerta na thread da interface gráfica
					Platform.runLater(alert::close);
				}catch(Exception e) {
					 Platform.runLater(() -> {
			    		  alert.setTitle("Error");
			    		  alert.setHeaderText("Error to sendMail");
			              alert.setContentText(e.getMessage());
			              alert.setAlertType(AlertType.ERROR);
			          });
				}
	        });
		tableViewCar.setItems(null);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		try {
			System.out.println("Entrou");
			addCar(new Car(txtCarModel.getText(), txtColor.getText(), txtPlate.getText()));
			notifyDataChangeListeners();
			removeTextInTextField();
			uptadeTableView();
		}catch(ValidationException e) {
			Alerts.showAlert("Error Saving object" , null, e.getMessage(), AlertType.ERROR);
		}catch(DbException e) {
			Alerts.showAlert("Error Saving object" , null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void removeTextInTextField() {
		txtCarModel.setText("");
		txtColor.setText("");
		txtPlate.setText("");
	}

	private void notifyDataChangeListeners() {
		
		for(DataChangeListener dataChange : dataChangeListeners) {
			dataChange.onDataChanged();
		}
		
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private void initializeNodes() {
		tableColumnCarModel.setCellValueFactory(new PropertyValueFactory<>("carModel"));
		tableColumnColor.setCellValueFactory(new PropertyValueFactory<>("color"));
		tableColumnPlate.setCellValueFactory(new PropertyValueFactory<>("plate"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCar.prefHeightProperty().bind(stage.heightProperty());

	}
	
	public void addCar(Car car) {
		ValidationException error = new ValidationException("Validation error");
		if(car.getCarModel() == null || car.getCarModel().trim().equals("")) {
			error.addErrors("carModel", null);
		}
		if(car.getColor() == null || car.getColor().trim().equals("")) {
			error.addErrors("color", null);
		}
		if(car.getPlate() == null || car.getPlate().trim().equals("")) {
			error.addErrors("plate", null);
		}
		list.add(car);
	}
	public void removeCar(Car car) {
				list.remove(car);
	}
	public List<Car> getList(){
		return list;
	}

	public void uptadeTableView() {
		obsList = FXCollections.observableArrayList(getList());
		tableViewCar.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Car obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CarEditController controller = loader.getController();
			controller.setCar(obj);
			controller.setController(this);
			controller.uptadeCar();
			controller.subscribeDataChangeListener(this);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	public void onDataChanged() {
		uptadeTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Car, Car>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Car obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/CarEdit.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Car, Car>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Car obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	 private void removeEntity(Car obj) {
		Optional<ButtonType> result =Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		if(result.get() == ButtonType.OK) {
			try {
				removeCar(obj);
				uptadeTableView();
			}catch(DbIntegrityException e) {
				Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
				System.out.print(e.getMessage());
			}
		}
	}

}
