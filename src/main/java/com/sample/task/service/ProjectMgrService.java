package com.sample.task.service;

import com.sample.task.dao.ProjectMgrDao;
import com.sample.task.domain.Project;
import com.sample.task.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectMgrService {
    @Autowired
ProjectMgrDao projectMgrDao;


    @Transactional
    public void addProject(Project project) throws  NotFoundException {


        projectMgrDao.addProject(project);
    }

    @Transactional
    public void updateProject(Project project) {

        projectMgrDao.updateProject(project);
    }

    @Transactional
    public void deleteProject(Integer projectId){

        projectMgrDao.deleteProject(projectId);
    }


    @Transactional
    public Project viewProject(Integer projectId){
        return projectMgrDao.viewProject(projectId);
    }

    @Transactional
    public List<Project> listProject(){
        return projectMgrDao.listProject();
    }
}
