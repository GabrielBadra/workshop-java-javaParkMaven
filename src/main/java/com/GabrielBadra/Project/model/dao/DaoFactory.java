package com.GabrielBadra.Project.model.dao;

import com.GabrielBadra.Project.db.DB;
import com.GabrielBadra.Project.model.dao.impl.DepartmentDaoJDBC;
import com.GabrielBadra.Project.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
