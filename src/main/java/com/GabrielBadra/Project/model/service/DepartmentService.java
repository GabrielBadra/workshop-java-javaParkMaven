package com.GabrielBadra.Project.model.service;

import java.util.List;

import com.GabrielBadra.Project.model.dao.DaoFactory;
import com.GabrielBadra.Project.model.dao.DepartmentDao;
import com.GabrielBadra.Project.model.entities.Department;

public class DepartmentService {

	DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){		
		return dao.findAll();
	}
	
	public void saveOrUptade(Department department) {
		if(department.getId() == null) {
			dao.insert(department);
		}else {
			dao.update(department);
		}
	}
	
	public void remove(Department department) {
		dao.deleteById(department.getId());
	}
}
