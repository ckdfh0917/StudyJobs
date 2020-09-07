package com.web.blog.dao.study;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.blog.model.study.Place;

@Repository
public interface PlaceDao extends JpaRepository<Place, String>{
	Optional<Place> findPlaceByPlaceId(final int placeId);
}
