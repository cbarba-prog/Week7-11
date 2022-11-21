package projects;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.exception.DbException;
import projects.service.ProjectService;
import java.math.BigDecimal;
import projects.entity.Project;

//Menu-Driven application that takes user input from the console.
//Class performs CRUD operations on the project tables.
@SuppressWarnings("unused")
public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;
	
	//@formatter:off
	private List<String> operations = List.of(
			"1) Add a project",		
			"2) List projects",
			"3) Select a project",
			"4) Update project details",
			"5) Delete a project"
	);
	//@formatter:on
	
	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	}
//Prints the operations, gets user menu selection, performs operation.
//Repeats steps until application is ended by user
	private void processUserSelections() {
		boolean done = false;
		
		while(!done) {
			try {
				int selection = getUserSelection();
				
				switch(selection) {
				case -1:
					done = exitMenu();
					break;
				
				case 1:
					createProject();
					break;
					
				case 2:
					listProjects();
					break;
					
				case 3:
					selectProject();
					break;
					
				case 4:
					updateProjectDetails();
					break;
				
				case 5:
					deleteProject();
					break;
					
				default:
					System.out.println("\n" + selection + "is not a valid selection. Try again.");
					break;
				}
			}
			catch(Exception e) {
				System.out.println("\nError: " + e + " Try again.");
			}
		}
		
	}
	//Deletes project from current project list
private void deleteProject() {
	listProjects();
	
	Integer projectId = getIntInput("Enter the ID of the project to delete");
	
	projectService.deleteProject(projectId);
	System.out.println("Project " + projectId + " was deleted successfully.");
	
	if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId));
	curProject = null;
	
}
//Updates project from current database
private void updateProjectDetails() {
	if(Objects.isNull(curProject)) {
		System.out.println("\nPlease select a project.");
		return;
	}
	
	String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
	BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours[" + curProject.getEstimatedHours() + "]");
	BigDecimal actualHours = getDecimalInput("Enter the actual hours + [" + curProject.getActualHours() +  "]");
	Integer difficulty = getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
	String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
	
	Project project = new Project();
	
	project.setProjectId(curProject.getProjectId());
	project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
	project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
	project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
	project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
	project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
	
	projectService.modifyProjectDetails(project);
	
	curProject = projectService.fetchProjectById(curProject.getProjectId());
}
// Lists project IDs and names so user can select, project details are returned to user
	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		
		curProject = null;
		curProject = projectService.fetchProjectById(projectId);
	}
//Prints current list of projects, calls on fetchAllProjects() used in ProjectService.java
	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out.println("  " + project.getProjectId() + ": " + project.getProjectName()));
	}
//User input for project row and calls on the project service to start a row.
	private void createProject() {
			String projectName = getStringInput("Enter the project name");
			BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
			BigDecimal actualHours = getDecimalInput("enter the actual hours");
			Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
			String notes = getStringInput("Enter the project notes");
			
			Project project = new Project();
			
			project.setProjectName(projectName);
			project.setEstimatedHours(estimatedHours);
			project.setActualHours(actualHours);
			project.setDifficulty(difficulty);
			project.setNotes(notes);
			
			Project dbProject = projectService.addProject(project);
			System.out.println("You have successfully created project: " + dbProject);
	}
//User input converted to BigDecimal
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {	
			return null;
		}
		
		try {
			return new BigDecimal(input).setScale(2);
			}
		catch(NumberFormatException e){
			throw new DbException(input + " is not a valid decimal number.");
		}
	}
//User exits application, prints a message and terminates application
	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}
//Prints the available menu selections. Converts user input to an int
//Returns selection as int or -1 if not selected
	private int getUserSelection(){
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");
		
		return Objects.isNull(input) ? -1 : input;
		
	}
//Prints a prompt to the console, gets the input and converts to Integer
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {	
			return null;
		}
		
		try {
			return Integer.valueOf(input);
		}
		catch(NumberFormatException e){
			throw new DbException(input + " is not a valid number.");
		}
	}

	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}

//Prints selection on a separate line in console
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:"); 
		
		operations.forEach(line -> System.out.println(" " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		}
		else {
			System.out.println("\nYou are working with project: " + curProject);
		}	
	}
}
