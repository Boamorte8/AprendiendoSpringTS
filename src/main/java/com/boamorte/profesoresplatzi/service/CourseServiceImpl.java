package com.boamorte.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boamorte.profesoresplatzi.dao.CourseDao;
import com.boamorte.profesoresplatzi.model.Course;

@Service("courseService")
@Transactional
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseDao _courseDao;
	
	@Override
	public void saveCourse(Course course) {
		_courseDao.saveCourse(course);
	}

	@Override
	public void deleteCourse(Long idCourse) {
		_courseDao.deleteCourse(idCourse);
	}

	@Override
	public void deleteCourse(Course course) {
		_courseDao.deleteCourse(course);
	}

	@Override
	public void updateCourse(Course course) {
		_courseDao.updateCourse(course);
	}

	@Override
	public List<Course> findAllCourse() {
		return _courseDao.findAllCourse();
	}

	@Override
	public Course findById(Long idCourse) {
		return _courseDao.findById(idCourse);
	}

	@Override
	public Course findByName(String name) {
		return _courseDao.findByName(name);
	}

	@Override
	public List<Course> findByTeacher(Long idTeacher) {
		return _courseDao.findByTeacher(idTeacher);
	}

	
}
