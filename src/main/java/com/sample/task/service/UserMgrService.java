package com.sample.task.service;

import com.sample.task.dao.UserMgrDao;
import com.sample.task.domain.User;
import com.sample.task.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserMgrService {
    @Autowired
    UserMgrDao userMgrDao;


    @Transactional
    public User addUser(User user) throws  NotFoundException {


         return userMgrDao.addUser(user);
    }

    @Transactional
    public void updateUser(User user) {

        userMgrDao.updateUser(user);
    }

    @Transactional
    public void deleteUser(Integer userId){

        userMgrDao.deleteUser(userId);
    }


    @Transactional
    public User viewUser(Integer userId){
        return userMgrDao.viewUser(userId);
    }

    @Transactional
    public List<User> listUser(){
        return userMgrDao.listUser();
    }
}
