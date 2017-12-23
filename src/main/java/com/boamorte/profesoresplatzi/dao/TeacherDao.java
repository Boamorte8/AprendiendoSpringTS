package com.boamorte.profesoresplatzi.dao;

import java.util.List;

import com.boamorte.profesoresplatzi.model.Teacher;

public interface TeacherDao {

	void saveTeacher(Teacher teacher);
	
	void deleteTeacher(Long idTeacher);
	
	void updateTeacher(Teacher teacher);
	
	List<Teacher> findAllTeacher();
	
	Teacher findById(Long idTeacher);
	
	Teacher findByName(String name);
}
