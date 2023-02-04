package com.pixeltrice.springbootOTPenabledapp.Repository;

import com.pixeltrice.springbootOTPenabledapp.Entity.UserPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserPojo, Integer>{

	Optional<UserPojo> findByUsername(String username);

} 

