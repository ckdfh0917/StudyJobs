package com.web.blog.controller.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.web.blog.service.address.AddressService;

import io.swagger.annotations.ApiOperation;

@Controller
public class AddressController {
	@Autowired
	AddressService addressService;
	
	@GetMapping("/address")
	@ApiOperation(value="모든 도/시의 리스트를 반환합니다.")
	public ResponseEntity findSiList() {
		List<String> ret = addressService.findSiList();
		if(ret==null || ret.size()==0) return new ResponseEntity("주소 데이터를 불러올 수 없습니다. 관리자에게 문의바랍니다.", HttpStatus.NOT_FOUND);
		return new ResponseEntity(ret, HttpStatus.OK);
	}
	
	@GetMapping("/address/{si}")
	@ApiOperation(value="도/시를 입력받아 일치하는 시/군/구의 리스트를 반환합니다.")
	public ResponseEntity findGuList(@PathVariable final String si) {
		List<String> ret = addressService.findGuListBySi(si);
		if(ret==null || ret.size()==0) return new ResponseEntity("일치하는 주소 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
		return new ResponseEntity(ret, HttpStatus.OK);
	}
}
