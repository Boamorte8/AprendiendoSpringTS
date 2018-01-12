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
	
	@RequestMapping("/error404")
	@ResponseBody
	public String error() {
		String response = "Tenemos problemas, alguien se nos metió al chuzo :)";
		return response;
	}
}
