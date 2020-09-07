
package com.web.blog.dao.user;

import java.util.Optional;

import com.web.blog.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String> {
	Optional<User> findUserByEmailAndPassword(String email, String password);
	Optional<User> findUserById(int id);
	Optional<User> findUserByEmail(final String email);
	
	@Query(value="select password from user where email=:email", nativeQuery=true)
	String findPasswordByEmail(final String email);
}
