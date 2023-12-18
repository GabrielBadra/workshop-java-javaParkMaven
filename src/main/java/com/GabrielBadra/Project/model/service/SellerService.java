package com.GabrielBadra.Project.model.service;

import java.util.List;

import com.GabrielBadra.Project.model.dao.DaoFactory;
import com.GabrielBadra.Project.model.dao.SellerDao;
import com.GabrielBadra.Project.model.entities.Seller;
import com.GabrielBadra.Project.model.exceptions.StringEqualsException;

public class SellerService {

	SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){		
		return dao.findAll();
	}
	
	public void saveOrUptade(Seller seller) {
		if(seller.getId() == null) {
			List<Seller> sellers = findAll();
			
			if(sellers == null){
				dao.insert(seller);
			}
			
			for(Seller obj: sellers) {
				if(seller.getPlaca().toLowerCase().equals(obj.getPlaca().toLowerCase())) {
					throw new StringEqualsException("Placa ja utilizada!");
				}
			}

			dao.insert(seller);
		}else {
			dao.update(seller);
		}
	}
	
	public void remove(Seller seller) {
		dao.deleteById(seller.getId());
	}
}
