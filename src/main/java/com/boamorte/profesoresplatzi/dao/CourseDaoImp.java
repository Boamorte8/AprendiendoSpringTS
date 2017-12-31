package com.boamorte.profesoresplatzi.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.boamorte.profesoresplatzi.model.Course;
import com.boamorte.profesoresplatzi.model.Teacher;

@Repository
@Transactional
public class CourseDaoImp extends AbstractSession implements CourseDao {

	@Override
	public void saveCourse(Course course) {
		try {
			getSession().persist(course);
		} catch(Exception e) {
			System.out.println("Error al crear al course: " + e.getMessage());
		}
	}

	@Override
	public void deleteCourse(Long idCourse) {
		try {
			Course course = findById(idCourse);
			if (course != null) {
				getSession().delete(course);
			}			
		} catch(Exception e) {
			System.out.println("Error al borrar el course: " + e.getMessage());
		}
	}
	
	@Override
	public void deleteCourse(Course course) {
		try {
			Course courseResponse = findById(course.getIdCourse());
			if (courseResponse != null) {
				getSession().delete(course);
			}			
		} catch(Exception e) {
			System.out.println("Error al borrar el course: " + e.getMessage());
		}
	}

	@Override
	public void updateCourse(Course course) {
		try {
			getSession().update(course);
		} catch(Exception e) {
			System.out.println("Error al actualizar el course: " + e.getMessage());
		}
	}

	@Override
	public List<Course> findAllCourse() {
		return getSession().createQuery("from Course").list();
	}

	@Override
	public Course findById(Long idCourse) {
		return (Course) getSession().get(Course.class, idCourse);
	}

	@Override
	public Course findByName(String name) {
		return (Course) getSession().createQuery(
				"from Course where name = :name")
				.setParameter("name", name).uniqueResult();
	}

	@Override
	public List<Course> findByTeacher(Long idTeacher) {
		return (List<Course>) getSession().createQuery(
				"from Course c join c.teacher t where t.id = :idTeacher")
				.setParameter("idTeacher", idTeacher).list();
	}
	

}
