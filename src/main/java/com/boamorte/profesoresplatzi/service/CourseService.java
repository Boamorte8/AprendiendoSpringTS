package com.boamorte.profesoresplatzi.service;

import java.util.List;

import com.boamorte.profesoresplatzi.model.Course;

public interface CourseService {
	
	void saveCourse(Course course);
	
	void deleteCourse(Long idCourse);
	
	void deleteCourse(Course course);
	
	void updateCourse(Course course);
	
	List<Course> findAllCourse();
	
	Course findById(Long idCourse);
	
	Course findByName(String name);
	
	List<Course> findByTeacher(Long idTeacher);
}
