package projects.service;

import java.util.List;
import java.util.NoSuchElementException;

import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();
//Calls DAO class to insert new project row
	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}
//Returns method in the ProjectDao.java
	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}
//Returns result from method in the ProjectDao.java, or else returns no project
	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException("Project with project ID=" + projectId + " does not exist"));
	}
//Returns result from method in the ProjectDao.java
	public void modifyProjectDetails(Project project) {
		if(!projectDao.modifyProjectDetails(project)) {
			throw new DbException("Project with ID= " + project.getProjectId() + " does not exist.");
		}
		
	}
//Returns result from method from ProjectDao.java
	public void deleteProject(Integer projectId) {
		if(!projectDao.deleteProject(projectId)) {
			throw new DbException("Project with ID=" + projectId + " does not exist");
		}
	}
}
