package com.springapp.springapp.service;

import com.springapp.springapp.entity.User;
import com.springapp.springapp.entity.UserType;
import com.springapp.springapp.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserTypeService {

    private final UserTypeRepository userTypeRepository;
    private final UserService userService;

    @Autowired
    public UserTypeService(UserTypeRepository userTypeRepository, UserService userService){
        this.userTypeRepository = userTypeRepository;
        this.userService = userService;
    }

    public List<UserType> getAllUserTypes(){
        return userTypeRepository.findAll();
    }

    public UserType getUserTypeById(Integer id){
        return userTypeRepository.findById(id).orElse(null);
    }

    @Transactional
    public UserType createUserType(Integer id, int userTypeId){
        User user = userService.getUserById(id);
        UserType userType = userTypeRepository.findById(userTypeId)
                .orElseThrow(()->new RuntimeException("Usertype not found with id"+ userTypeId));
        if(user!=null){
            user.setUserType(userType);
            userService.saveUser(user);
            return userTypeRepository.save(userType);
        }
        else{
            return null;
        }
    }

    public void deleteUserType(Integer id){
        userTypeRepository.deleteById(id);
    }
}
