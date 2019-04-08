package com.sample.task.controller;

import com.sample.task.domain.User;
import com.sample.task.exception.NotFoundException;
import com.sample.task.service.UserMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

@RestController

@RequestMapping("/userMgr")
public class UserMgrController {

    @Autowired
    private UserMgrService userMgrService;

    @RequestMapping(value= "/**", method= RequestMethod.OPTIONS)
    public void corsHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
        response.addHeader("Access-Control-Max-Age", "3600");
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Boolean> addUser(@RequestBody User user) throws NotFoundException {

        userMgrService.addUser(user);
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/updateUser", method = RequestMethod.PUT ,produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Boolean> updateUser(@RequestBody User user) {

        userMgrService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/viewUser/{userId}", method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> viewUser(@PathVariable Integer userId){
        User user= null;
        user = userMgrService.viewUser(userId);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/listUsers/", method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<User>> listUsers() {

        List<User> userList=userMgrService.listUser();
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/deleteUser/{userId}", method = RequestMethod.DELETE,produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Boolean> deleteUser(@PathVariable Integer userId) {

        userMgrService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
