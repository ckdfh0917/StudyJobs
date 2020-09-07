package com.web.blog.dao.address;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.blog.model.address.Address;

@Repository
public interface AddressDao extends JpaRepository<Address, String>{
	Optional<Address> findBySiAndGu(final String si, final String gu);
	
	@Query(value="SELECT DISTINCT si FROM ssafyweb.address", nativeQuery=true)
	public List<String> findSiList();
	@Query(value="SELECT DISTINCT gu FROM ssafyweb.address WHERE si=:si", nativeQuery=true)
	public List<String> findGuListBySi(final String si);
}
