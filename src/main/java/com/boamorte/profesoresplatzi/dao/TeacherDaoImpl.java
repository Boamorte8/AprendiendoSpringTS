package com.boamorte.profesoresplatzi.dao;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.boamorte.profesoresplatzi.model.Teacher;
import com.boamorte.profesoresplatzi.model.TeacherSocialMedia;

@Repository
@Transactional
public class TeacherDaoImpl extends AbstractSession implements TeacherDao {

	@Override
	public void saveTeacher(Teacher teacher) {
		try {
			getSession().persist(teacher);
		} catch(Exception e) {
			System.out.println("Error al crear al teacher: " + e.getMessage());
		}
	}

	@Override
	public void deleteTeacher(Teacher teacher) {
		try {
			Teacher teacherResponse = findById(teacher.getId());
			if (teacherResponse != null) {
				Iterator<TeacherSocialMedia> iterator = teacher.getTeacherSocialMedias().iterator();
				while(iterator.hasNext()) {
					TeacherSocialMedia teacherSocialMedia = iterator.next();
					iterator.remove();
					getSession().delete(teacherSocialMedia);
				}
				teacher.getTeacherSocialMedias().clear();
				getSession().delete(teacher);
			}			
		} catch(Exception e) {
			System.out.println("Error al borrar teacher: " + e.getMessage());
		}
	}
	
	@Override
	public void deleteTeacher(Long idTeacher) {
		try {
			Teacher teacherResponse = findById(idTeacher);
			if (teacherResponse != null) {
				Iterator<TeacherSocialMedia> iterator = teacherResponse.getTeacherSocialMedias().iterator();
				while(iterator.hasNext()) {
					TeacherSocialMedia teacherSocialMedia = iterator.next();
					iterator.remove();
					getSession().delete(teacherSocialMedia);
				}
				teacherResponse.getTeacherSocialMedias().clear();
				getSession().delete(teacherResponse);
			}
		} catch(Exception e) {
			System.out.println("Error al borrar teacher: " + e.getMessage());
		}
	}

	@Override
	public void updateTeacher(Teacher teacher) {
		try {
			getSession().update(teacher);
		} catch(Exception e) {
			System.out.println("Error al actualizar teacher: " + e.getMessage());
		}
	}

	@Override
	public List<Teacher> findAllTeacher() {
		return getSession().createQuery("from Teacher").list();
	}

	@Override
	public Teacher findById(Long idTeacher) {
		return (Teacher) getSession().get(Teacher.class, idTeacher);
	}

	@Override
	public Teacher findByName(String name) {
		return (Teacher) getSession().createQuery(
				"from Teacher where name = :name")
				.setParameter("name", name).uniqueResult();
	}

}
