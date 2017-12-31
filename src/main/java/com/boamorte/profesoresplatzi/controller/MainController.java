package com.boamorte.profesoresplatzi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	@RequestMapping("/")
	@ResponseBody
	public String index() {
		String response = "Bienvenido a la primera API Rest de Boamorte88 :)";
		return response;
	}
}
