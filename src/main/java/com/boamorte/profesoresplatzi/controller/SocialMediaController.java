package com.boamorte.profesoresplatzi.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.boamorte.profesoresplatzi.model.SocialMedia;
import com.boamorte.profesoresplatzi.service.SocialMediaService;
import com.boamorte.profesoresplatzi.utils.CustomErrorType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	
	public static final String SOCIALMEDIA_UPLOADED_FOLDER = "images/social_medias/";
	
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
	
	//POST IMAGE
	@RequestMapping(value="/socialMedias/images", method = RequestMethod.POST, headers = ("content-type=multipart/form-data"))
	public ResponseEntity<byte[]> uploadSocialMediaImage(@RequestParam("id_social_media") Long idSocialMedia, 
			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder componentsBuilder) {
		if (null == idSocialMedia) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required, please set it"), HttpStatus.CONFLICT);
		}
		
		if (multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please select a file to upload"), HttpStatus.CONFLICT);
		}
		
		SocialMedia socialMedia = _socialMediaService.findById(idSocialMedia);
		if (null == socialMedia) {
			return new ResponseEntity(new CustomErrorType("No exist social media with id: " + idSocialMedia), HttpStatus.NO_CONTENT);
		}
		
		if (socialMedia.getIcon().isEmpty() || socialMedia.getIcon() != null) {
			String filename = socialMedia.getIcon();
			Path path = Paths.get(filename);
			File file = path.toFile();
			if (file.exists()) {
				file.delete();
			}
		}
		
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String dateName = dateFormat.format(date);
			
			String fileName = String.valueOf(idSocialMedia) + "-pictureSocialMedia-" + dateName + "." + multipartFile.getContentType().split("/")[1];
			socialMedia.setIcon(SOCIALMEDIA_UPLOADED_FOLDER + fileName);
			
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(SOCIALMEDIA_UPLOADED_FOLDER  + fileName);
			Files.write(path, bytes);
			
			_socialMediaService.updateSocialMedia(socialMedia);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error during upload icon image: " + multipartFile.getOriginalFilename()), HttpStatus.NO_CONTENT);
		}
	}
	
	//GET IMAGE
	@RequestMapping(value="/socialMedias/{id_social_media}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getSocialMediaImage(@PathVariable("id_social_media") Long idSocialMedia){
		if (null == idSocialMedia) {
			return new ResponseEntity(new CustomErrorType("id_social_media is required, please set it "), HttpStatus.NO_CONTENT);
		}
		
		SocialMedia socialMedia = _socialMediaService.findById(idSocialMedia);
		if (null == socialMedia) {
			return new ResponseEntity(new CustomErrorType("No exist social media with id: " + idSocialMedia), HttpStatus.NO_CONTENT);
		}
		
		try {
			
			String fileName = socialMedia.getIcon();
			Path path = Paths.get(fileName);
			File f = path.toFile();
			if (!f.exists()) {
				return new ResponseEntity(new CustomErrorType("Image not found"),HttpStatus.CONFLICT);
			}
			
			byte[] image = Files.readAllBytes(path);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error to show image"),HttpStatus.CONFLICT);
		}
		
	}
	
	//DELETE IMAGE
	@RequestMapping(value="/socialMedias/{id_social_media}/images", method = RequestMethod.DELETE, headers = ("Accept=application/json"))
	public ResponseEntity<?> deleteSocialMediaImage(@PathVariable("id_social_media") Long idSocialMedia) {
		if (null == idSocialMedia) {
			return new ResponseEntity(new CustomErrorType("id_social_media is required, please set it "), HttpStatus.NO_CONTENT);
		}
		
		SocialMedia socialMedia = _socialMediaService.findById(idSocialMedia);
		if (null == socialMedia) {
			return new ResponseEntity(new CustomErrorType("No exist social media with id: " + idSocialMedia), HttpStatus.NO_CONTENT);
		}
		
		if (socialMedia.getIcon().isEmpty() || socialMedia.getIcon() == null){
			return new ResponseEntity(new CustomErrorType("This social media doesn`t have image assigned"), HttpStatus.NOT_FOUND);
		}
		
		String fileName = socialMedia.getIcon();
		Path path = Paths.get(fileName);
		File file = path.toFile();
		if (file.exists()) {
			file.delete();
		}
		
		socialMedia.setIcon("");;
		_socialMediaService.updateSocialMedia(socialMedia);
		
		return new ResponseEntity<SocialMedia> (HttpStatus.OK);
	}
}


