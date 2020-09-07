package com.web.blog.dao.study;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.blog.model.study.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, String>{
	Optional<Category> findCategoryByCategoryId(final int categoryId);
}
