package com.springapp.springapp.repository;


import com.springapp.springapp.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {

}
