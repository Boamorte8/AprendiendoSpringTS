package com.boamorte.profesoresplatzi.service;

import java.util.List;

import com.boamorte.profesoresplatzi.model.Teacher;

public interface TeacherService {

	void saveTeacher(Teacher teacher);
	
	void deleteTeacher(Teacher teacher);
	
	void deleteTeacher(Long idTeacher);
	
	void updateTeacher(Teacher teacher);
	
	List<Teacher> findAllTeacher();
	
	Teacher findById(Long idTeacher);
	
	Teacher findByName(String name);
}
