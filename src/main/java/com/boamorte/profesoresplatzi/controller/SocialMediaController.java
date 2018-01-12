package com.boamorte.profesoresplatzi.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.boamorte.profesoresplatzi.model.SocialMedia;
import com.boamorte.profesoresplatzi.service.SocialMediaService;
import com.boamorte.profesoresplatzi.utils.CustomErrorType;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class SocialMediaController {

	@Autowired
	SocialMediaService _socialMediaService;
	
	private boolean validateIdSocialMedia(Long idSocialMedia) {
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean validateSocialMedia(SocialMedia socialMedia) {
		if (socialMedia.getName().equals(null) || socialMedia.getName().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	//GET
	@RequestMapping(value="/socialMedias", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<SocialMedia>> getSocialMedias(@RequestParam(value="name", required=false) String name) {
		List<SocialMedia> socialMedias = new ArrayList<>();
		
		if (name == null) {
			socialMedias = _socialMediaService.findAllSocialMedia();
			if (socialMedias.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);
		} else {
			SocialMedia socialMedia = _socialMediaService.findByName(name);
			if (socialMedia == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			socialMedias.add(socialMedia);
			return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);
		}
		
		
	}
	
	//GET
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<SocialMedia> getSocialMediaById(@PathVariable("id") Long idSocialMedia) {
		if (validateIdSocialMedia(idSocialMedia) == false) {
			return new ResponseEntity(new CustomErrorType("IdSocialMedia is required"), HttpStatus.CONFLICT);
		}
		SocialMedia socialMedia = _socialMediaService.findById(idSocialMedia);
		if (socialMedia == null) {
			return new ResponseEntity(new CustomErrorType("No exist that id"), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<SocialMedia>(socialMedia, HttpStatus.OK);
	}
	
	//POST
	@RequestMapping(value="/socialMedias", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createSocialMedias(@RequestBody SocialMedia socialMedia, UriComponentsBuilder uriComponentsBuilder) {
		if (validateSocialMedia(socialMedia) == false) {
			return new ResponseEntity(new CustomErrorType("SocialMedia is required"), HttpStatus.NO_CONTENT);
		}
		
		if (_socialMediaService.findByName(socialMedia.getName()) != null) {
			return new ResponseEntity(new CustomErrorType("A social media with that name already exist"), HttpStatus.CONFLICT);
		}
		
		_socialMediaService.saveSocialMedia(socialMedia);
		SocialMedia socialMediaCreated = _socialMediaService.findByName(socialMedia.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentsBuilder.path("/v1/socialMedias/{id}")
				.buildAndExpand(socialMediaCreated.getId()).toUri()
				);
		
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	//UPDATE
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<SocialMedia> updateSocialMedia(@PathVariable("id") Long idSocialMedia, @RequestBody SocialMedia socialMedia) {
		if (validateIdSocialMedia(idSocialMedia) == false || validateSocialMedia(socialMedia) == false) {
			return new ResponseEntity(new CustomErrorType("IdSocialMedia and socialMedia is required"), HttpStatus.CONFLICT);
		}
		
		SocialMedia currentSocialMedia = _socialMediaService.findById(idSocialMedia);
		if (currentSocialMedia == null) {
			return new ResponseEntity(new CustomErrorType("No exist that social media"), HttpStatus.NO_CONTENT);
		}
		currentSocialMedia.setName(socialMedia.getName());
		currentSocialMedia.setIcon(socialMedia.getIcon());
		currentSocialMedia.setTeacherSocialMedias(socialMedia.getTeacherSocialMedias());
		_socialMediaService.updateSocialMedia(currentSocialMedia);
		return new ResponseEntity<SocialMedia>(currentSocialMedia, HttpStatus.OK);
	}
	
	//DELETE
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteSocialMedia(@PathVariable("id") Long idSocialMedia) {
		if (validateIdSocialMedia(idSocialMedia) == false) {
			return new ResponseEntity(new CustomErrorType("IdSocialMedia is required"), HttpStatus.CONFLICT);
		}
		
		SocialMedia currentSocialMedia = _socialMediaService.findById(idSocialMedia);
		if (currentSocialMedia == null) {
			return new ResponseEntity(new CustomErrorType("No exist that social media"), HttpStatus.NO_CONTENT);
		}
		_socialMediaService.deleteSocialMedia(idSocialMedia);
		return new ResponseEntity<SocialMedia>(HttpStatus.OK);
	}
	
	
}


