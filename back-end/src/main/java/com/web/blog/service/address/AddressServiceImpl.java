package com.web.blog.service.address;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.blog.dao.address.AddressDao;
import com.web.blog.model.address.Address;

@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	private AddressDao addrDao;
	
	@Override
	public Address findAddressBySiAndGu(final String si, final String gu) {
		Optional<Address> addrOpt = addrDao.findBySiAndGu(si, gu);
		return addrOpt.isPresent() ? addrOpt.get() : null;
	}

	@Override
	public List<String> findSiList() {
		return addrDao.findSiList();
	}

	@Override
	public List<String> findGuListBySi(String si) {
		return addrDao.findGuListBySi(si);
	}
}
