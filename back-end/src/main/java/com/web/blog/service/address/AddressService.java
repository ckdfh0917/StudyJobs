package com.web.blog.service.address;

import java.util.List;

import com.web.blog.model.address.Address;

public interface AddressService {
	public Address findAddressBySiAndGu(final String si, final String gu);
	public List<String> findSiList();
	public List<String> findGuListBySi(final String si);
}
