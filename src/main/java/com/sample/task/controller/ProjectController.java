
package com.sample.task.controller;

import com.sample.task.domain.Project;
import com.sample.task.exception.NotFoundException;
import com.sample.task.service.ProjectMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/projectMgr")
public class ProjectController {

    @Autowired
    private ProjectMgrService projectMgrService;

    @RequestMapping(value= "/**", method= RequestMethod.OPTIONS)
    public void corsHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
        response.addHeader("Access-Control-Max-Age", "3600");
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/addProject", method = RequestMethod.POST,headers = "Accept=application/json",produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Boolean> addProject(@RequestBody Project project) throws NotFoundException {

        projectMgrService.addProject(project);
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/updateProject", method = RequestMethod.PUT ,produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Boolean> updateProject(@RequestBody Project project)  throws NotFoundException  {

        projectMgrService.updateProject(project);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/viewProject/{projectId}", method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Project> viewProject(@PathVariable Integer projectId){
        Project project= null;
        project = projectMgrService.viewProject(projectId);
        return new ResponseEntity<>(project,HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/listProjects/", method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Project>> listProjects() {

        List<Project> projectList=projectMgrService.listProject();
        System.out.println("count of tasks" + projectList.get(0).getCountOfTasks());

        return new ResponseEntity<>(projectList,HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/deleteProject/{projectId}", method = RequestMethod.DELETE,produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Boolean> deleteProject(@PathVariable Integer projectId) {

        projectMgrService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
