package com.boamorte.profesoresplatzi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boamorte.profesoresplatzi.service.TeacherService;

@Controller
@RequestMapping("/v1")
public class TeacherController {

	@Autowired
	TeacherService _teacherService;
	
	
}
