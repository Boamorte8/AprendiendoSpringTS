package com.boamorte.profesoresplatzi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.boamorte.profesoresplatzi.model.Course;
import com.boamorte.profesoresplatzi.service.CourseService;
import com.boamorte.profesoresplatzi.utils.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class CourseController {

	@Autowired
	CourseService _courseService;
	
	private boolean validateIdCourse(Long idCourse) {
		if (idCourse == null || idCourse <= 0) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean validateCourse(Course course) {
		if (course.getName().equals(null) || course.getName().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	//GET
	@RequestMapping(value="/courses", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Course>> getCourses(@RequestParam(value="name", required=false) String name, @RequestParam(value = "id_teacher",required = false) Long id_teacher) {
		List<Course> courses = new ArrayList<>();
		
		if (id_teacher != null) {
			courses = _courseService.findByTeacher(id_teacher);
			if (courses.isEmpty()) {
	            return new ResponseEntity(HttpStatus.NO_CONTENT);
	        }
		}
		
		if (name != null) {
			Course course = _courseService.findByName(name);
			if (course == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			courses.add(course);
			return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		}	
		
		if (name == null && id_teacher == null) {
			courses = _courseService.findAllCourse();
			if (courses.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		} 
		return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
	}
	
	//GET BY ID
	@RequestMapping(value="/courses/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Course> getCourseById(@PathVariable("id") Long idCourse) {
		if (validateIdCourse(idCourse) == false) {
			return new ResponseEntity(new CustomErrorType("IdCourse is required"), HttpStatus.CONFLICT);
		}
		Course course = _courseService.findById(idCourse);
		if (course == null) {
			return new ResponseEntity(new CustomErrorType("No exist that id"), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}
	
	//POST
	@RequestMapping(value="/courses", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentsBuilder) {
		if (validateCourse(course) == false) {
			return new ResponseEntity(new CustomErrorType("Course is required"), HttpStatus.CONFLICT);
		}
		
		if (_courseService.findByName(course.getName()) != null) {
			return new ResponseEntity(new CustomErrorType("A course with name " + course.getName() + "already exist"), HttpStatus.NO_CONTENT);
		}
		
		_courseService.saveCourse(course);
		
		Course courseCreated = _courseService.findByName(course.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
			uriComponentsBuilder.path("/v1/courses/{id}")
			.buildAndExpand(courseCreated.getIdCourse()).toUri()
			);
			
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
		
	//UPDATE
	@RequestMapping(value="/courses/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<Course> updateCourse(@PathVariable("id") Long idCourse, @RequestBody Course course) {
		if (validateIdCourse(idCourse) == false || validateCourse(course) == false) {
			return new ResponseEntity(new CustomErrorType("IdCourse and course is required"), HttpStatus.CONFLICT);
		}
		
		Course currentCourse = _courseService.findById(idCourse);
		if (currentCourse == null) {
			return new ResponseEntity(new CustomErrorType("Unable to upate. Social Media with id " + idCourse + " not found."), HttpStatus.NO_CONTENT);
		}
		
		currentCourse.setName(course.getName());
		currentCourse.setTeacher(course.getTeacher());
		currentCourse.setProject(course.getProject());
		currentCourse.setThemes(course.getThemes());
		
		_courseService.updateCourse(currentCourse);
		return new ResponseEntity<Course>(currentCourse, HttpStatus.OK);
	}
	
	//DELETE
	@RequestMapping(value="/courses/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteCourse(@PathVariable("id") Long idCourse) {
		if (validateIdCourse(idCourse) == false) {
			return new ResponseEntity(new CustomErrorType("IdCourse is required"), HttpStatus.CONFLICT);
		}
		
		Course currentCourse = _courseService.findById(idCourse);
		if (currentCourse == null) {
			return new ResponseEntity(new CustomErrorType("Unable to delete. course with id " + idCourse + " not found."), HttpStatus.NO_CONTENT);
		}
		_courseService.deleteCourse(idCourse);
		return new ResponseEntity<Course>(HttpStatus.OK);
	}
}
