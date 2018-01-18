package com.boamorte.profesoresplatzi.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.boamorte.profesoresplatzi.model.SocialMedia;
import com.boamorte.profesoresplatzi.model.Teacher;
import com.boamorte.profesoresplatzi.model.TeacherSocialMedia;
import com.boamorte.profesoresplatzi.service.SocialMediaService;
import com.boamorte.profesoresplatzi.service.TeacherService;
import com.boamorte.profesoresplatzi.utils.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class TeacherController {

	@Autowired
	private TeacherService _teacherService;
	
	@Autowired
	private SocialMediaService _socialMediaService;
	
	public static final String TEACHER_UPLOADED_FOLDER = "images/teachers/";
	
	private boolean validateIdTeacher(Long idTeacher) {
		if (null == idTeacher || idTeacher <= 0) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean validateTeacher(Teacher teacher) {
		if (teacher.getName().equals(null) || teacher.getName().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	//GET
	@RequestMapping(value="/teachers", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(value="name", required=false) String name) {
		List<Teacher> teachers = new ArrayList<>();
		
		if (null == name) {
			teachers = _teacherService.findAllTeacher();
			if (teachers.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		} else {
			Teacher teacher = _teacherService.findByName(name);
			if (null == teacher) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			teachers.add(teacher);
		}	
		return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
	}
	
	//GET BY ID
	@RequestMapping(value="/teachers/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") Long idTeacher) {
		if (validateIdTeacher(idTeacher) == false) {
			return new ResponseEntity(new CustomErrorType("idTeacher is required"), HttpStatus.CONFLICT);
		}
		Teacher teacher = _teacherService.findById(idTeacher);
		if (null == teacher) {
			return new ResponseEntity(new CustomErrorType("No exist teacher with id: " + idTeacher), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
	}
	
	//POST
	@RequestMapping(value="/teachers", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder) {
		if (validateTeacher(teacher) == false) {
			return new ResponseEntity(new CustomErrorType("Teacher is required"), HttpStatus.CONFLICT);
		}
		
		if (_teacherService.findByName(teacher.getName()) != null) {
			return new ResponseEntity(new CustomErrorType("A teacher with that name already exist"), HttpStatus.NO_CONTENT);
		}
		
		_teacherService.saveTeacher(teacher);
		Teacher teacherCreated = _teacherService.findByName(teacher.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentsBuilder.path("/v1/courses/{id}")
				.buildAndExpand(teacherCreated.getId()).toUri()
				);
		
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	//POST IMAGE
	@RequestMapping(value="/teachers/images", method = RequestMethod.POST, headers = ("content-type=multipart/form-data"))
	public ResponseEntity<byte[]> uploadTeacherImage(@RequestParam("id_teacher") Long idTeacher, 
			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder componentsBuilder) {
		if (null == idTeacher) {
			return new ResponseEntity(new CustomErrorType("idTeacher is required, please set it"), HttpStatus.CONFLICT);
		}
		
		if (multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please select a file to upload"), HttpStatus.CONFLICT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if (null == teacher) {
			return new ResponseEntity(new CustomErrorType("No exist teacher with id: " + idTeacher), HttpStatus.NO_CONTENT);
		}
		
		if (teacher.getAvatar().isEmpty() || teacher.getAvatar() != null) {
			String filename = teacher.getAvatar();
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
			
			String fileName = String.valueOf(idTeacher) + "-pictureTeacher-" + dateName + "." + multipartFile.getContentType().split("/")[1];
			teacher.setAvatar(TEACHER_UPLOADED_FOLDER + fileName);
			
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(TEACHER_UPLOADED_FOLDER + fileName);
			Files.write(path, bytes);
			
			_teacherService.updateTeacher(teacher);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error during upload avatar image: " + multipartFile.getOriginalFilename()), HttpStatus.NO_CONTENT);
		}
	}
	
	//GET IMAGE
	@RequestMapping(value="/teachers/{id_teacher}/images", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getTeacherImage(@PathVariable("id_teacher") Long idTeacher){
		if (null == idTeacher) {
			 return new ResponseEntity(new CustomErrorType("id_teacher is required, please set it "), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if (null == teacher) {
			return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: " + idTeacher + " not found"), HttpStatus.NOT_FOUND);
		}
		
		try {
			
			String fileName = teacher.getAvatar();
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
	@RequestMapping(value="/teachers/{id_teacher}/images", method = RequestMethod.DELETE, headers = ("Accept=application/json"))
	public ResponseEntity<?> deleteTeacherImaga(@PathVariable("id_teacher") Long idTeacher) {
		if (null == idTeacher) {
			 return new ResponseEntity(new CustomErrorType("id_teacher is required, please set it "), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if (null == teacher) {
			return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: " + idTeacher + " not found"), HttpStatus.NOT_FOUND);
		}
		
		if (teacher.getAvatar().isEmpty() || teacher.getAvatar() == null) {
			return new ResponseEntity(new CustomErrorType("This Teacher doesn`t have image assigned"), HttpStatus.NOT_FOUND);
		}
		
		String fileName = teacher.getAvatar();
		Path path = Paths.get(fileName);
		File file = path.toFile();
		if (file.exists()) {
			file.delete();
		}
		
		teacher.setAvatar("");
		_teacherService.updateTeacher(teacher);
		
		return new ResponseEntity<Teacher> (HttpStatus.OK);
	}

	
	//UPDATE
	@RequestMapping(value="/teachers/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<Teacher> updateCourse(@PathVariable("id") Long idTeacher, @RequestBody Teacher teacher) {
		if (validateIdTeacher(idTeacher) == false || validateTeacher(teacher) == false) {
			return new ResponseEntity(new CustomErrorType("IdTeacher and teacher is required"), HttpStatus.CONFLICT);
		}
		
		Teacher currentTeacher = _teacherService.findById(idTeacher);
		if (currentTeacher == null) {
			return new ResponseEntity(new CustomErrorType("No exist that teacher"), HttpStatus.NO_CONTENT);
		}
		currentTeacher.setName(teacher.getName());
		currentTeacher.setAvatar(teacher.getAvatar());
		currentTeacher.setCourses(teacher.getCourses());
		currentTeacher.setTeacherSocialMedias(teacher.getTeacherSocialMedias());
		_teacherService.updateTeacher(currentTeacher);
		return new ResponseEntity<Teacher>(currentTeacher, HttpStatus.OK);
	}
	
	//DELETE
	@RequestMapping(value="/teachers/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteTeacher(@PathVariable("id") Long idTeacher) {
		if (validateIdTeacher(idTeacher) == false) {
			return new ResponseEntity(new CustomErrorType("idTeacher is required"), HttpStatus.CONFLICT);
		}
		
		Teacher currentTeacher = _teacherService.findById(idTeacher);
		if (currentTeacher == null) {
			return new ResponseEntity(new CustomErrorType("Unable to delete. teacher with id " + idTeacher + " not found."), HttpStatus.NO_CONTENT);
		}
		_teacherService.deleteTeacher(idTeacher);
		return new ResponseEntity<Teacher>(HttpStatus.OK);
	}
	
	//UPDATE teacher social medias
	@RequestMapping(value="teachers/socialMedias", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> assignTeacherSocialMedia(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder) {
		if (null == teacher.getId()) {
			return new ResponseEntity(new CustomErrorType("idTeacher is required, please set it"), HttpStatus.CONFLICT);
		}
		
		Teacher teacherSaved = _teacherService.findById(teacher.getId());
		if (teacherSaved == null) {
			return new ResponseEntity(new CustomErrorType("The teacher with id: " + teacher.getId() + ", not found."), HttpStatus.NO_CONTENT);
		}
		
		if (teacher.getTeacherSocialMedias().size() == 0) {
			return new ResponseEntity(new CustomErrorType("Teacher social medias is required"), HttpStatus.CONFLICT);
		} else {
			Iterator<TeacherSocialMedia> i = teacher.getTeacherSocialMedias().iterator();
			while (i.hasNext()) {
				TeacherSocialMedia teacherSocialMedia = i.next();
				if (null == teacherSocialMedia.getSocialmedia().getId() || null == teacherSocialMedia.getNickname()) {
					return new ResponseEntity(new CustomErrorType("Teacher with social medias is required"), HttpStatus.CONFLICT);
				} else {
					TeacherSocialMedia tsmAux = _socialMediaService.findByIdAndName(teacherSocialMedia.getSocialmedia().getId(), 
							teacherSocialMedia.getNickname());
					if (null != tsmAux) {
						return new ResponseEntity(new CustomErrorType("The teacher social media with id: " + teacherSocialMedia.getSocialmedia().getId() 
								+ " and nickname: " + teacherSocialMedia.getNickname() + " already exists"), HttpStatus.CONFLICT);
					}
					
					SocialMedia socialMedia = _socialMediaService.findById(teacherSocialMedia.getSocialmedia().getId());
					if (null == socialMedia) {
						return new ResponseEntity(new CustomErrorType("The id social media with id: " + teacherSocialMedia.getSocialmedia().getId() 
								+ " not found." ), HttpStatus.NO_CONTENT);
					}
					
					teacherSocialMedia.setSocialmedia(socialMedia);
					teacherSocialMedia.setTeacher(teacherSaved);
					
					if (null == tsmAux) {
						teacherSaved.getTeacherSocialMedias().add(teacherSocialMedia);
					} else {
						LinkedList<TeacherSocialMedia> teacherSocialMedias = new LinkedList<>();
						teacherSocialMedias.addAll(teacherSaved.getTeacherSocialMedias());
						
						for (int j = 0; j < teacherSocialMedias.size(); j++) {
							TeacherSocialMedia teacherSocialMedia2 = teacherSocialMedias.get(j);
							if (teacherSocialMedia.getTeacher().getId() == teacherSocialMedia2.getTeacher().getId() && 
									teacherSocialMedia.getSocialmedia().getId() == teacherSocialMedia2.getSocialmedia().getId()) {
								teacherSocialMedia2.setNickname(teacherSocialMedia.getNickname());
								teacherSocialMedias.set(j, teacherSocialMedia2);
							} else {
								teacherSocialMedias.set(j, teacherSocialMedia2);
							}
						}
						
						teacherSaved.getTeacherSocialMedias().clear();
						teacherSaved.getTeacherSocialMedias().addAll(teacherSocialMedias);
					}
				}
				
			}
		}
		
		_teacherService.updateTeacher(teacherSaved);
		return new ResponseEntity<Teacher>(teacherSaved, HttpStatus.OK);
//		return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
	}
}
