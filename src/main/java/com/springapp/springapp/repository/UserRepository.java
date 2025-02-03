package com.springapp.springapp.repository;

import com.springapp.springapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    //JPA uses findBy...... to create SQL queries. In this case: Select * FROM user WHERE email = ...
    User findByUserId(int userId);
    User findByEmail(String email);

}
