package com.GabrielBadra.Project.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.GabrielBadra.Project.db.DbException;
import com.GabrielBadra.Project.gui.listeners.DataChangeListener;
import com.GabrielBadra.Project.gui.util.Alerts;
import com.GabrielBadra.Project.gui.util.Constraints;
import com.GabrielBadra.Project.gui.util.Utils;
import com.GabrielBadra.Project.model.entities.Department;
import com.GabrielBadra.Project.model.exceptions.ValidationException;
import com.GabrielBadra.Project.model.service.DepartmentService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DepartmentFormController implements Initializable{

	private Department department;
	
	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private javafx.scene.control.TextField txtId;
	
	@FXML
	private javafx.scene.control.TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(javafx.event.ActionEvent event) {
		if(department == null) {
			throw new IllegalStateException("Department was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			department = getFormData();
			service.saveOrUptade(department);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}catch(DbException e) {
			Alerts.showAlert("Error Saving object" , null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		
		for(DataChangeListener dataChange : dataChangeListeners) {
			dataChange.onDataChanged();
		}
		
	}

	private Department getFormData() {
		Integer id = Utils.tryParseToInt(txtId.getText());
		
		ValidationException error = new ValidationException("Validation error");
		
		//.Trim é para eliminar todos os espaços em branco
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			error.addErrors("name", "Field can't be empty");
		}
		
		if(error.getErrors().size() > 0) {
			throw error;
		}
		
		return new Department(id, txtName.getText());
	}

	@FXML
	public void onBtCancelAction(javafx.event.ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {	
		initializeNodes();
	}
	
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void uptadeFormData() {
		if(department == null) {
			throw new IllegalStateException("Entity was null.");
		}
		txtId.setText(String.valueOf(department.getId()));
		txtName.setText(department.getName());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}
