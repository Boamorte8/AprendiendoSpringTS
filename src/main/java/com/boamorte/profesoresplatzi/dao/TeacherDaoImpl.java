package com.boamorte.profesoresplatzi.dao;

import java.util.List;

import com.boamorte.profesoresplatzi.model.Teacher;

public class TeacherDaoImpl extends PlatziSession implements TeacherDao {

	public void saveTeacher(Teacher teacher) {
		try {
					
		} catch(Exception e) {
			System.out.println("Error al crear al teacher: " + e.getMessage());
		}
		
	}

	public void deleteTeacher(Teacher teacher) {
		try {
						
		} catch(Exception e) {
			System.out.println("Error al actualizar teacher: " + e.getMessage());
		}
	}
	
	public void deleteTeacher(Long idTeacher) {
		try {
					
		} catch(Exception e) {
			System.out.println("Error al actualizar teacher: " + e.getMessage());
		}
	}

	public void updateTeacher(Teacher teacher) {
		try {
						
		} catch(Exception e) {
			System.out.println("Error al actualizar teacher: " + e.getMessage());
		}
	}

	public List<Teacher> findAllTeacher() {
		return null;
	}

	public Teacher findById(Long idTeacher) {
		return null;
	}

	public Teacher findByName(String name) {
		return null;
	}

}
