package com.boamorte.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boamorte.profesoresplatzi.dao.SocialMediaDao;
import com.boamorte.profesoresplatzi.model.SocialMedia;
import com.boamorte.profesoresplatzi.model.TeacherSocialMedia;

@Service("socialMediaService")
@Transactional
public class SocialMediaServiceImpl implements SocialMediaService {
	
	@Autowired
	private SocialMediaDao _socialMediaDao;

	@Override
	public void saveSocialMedia(SocialMedia socialMedia) {
		_socialMediaDao.saveSocialMedia(socialMedia);
	}

	@Override
	public void deleteSocialMedia(Long idSocialMedia) {
		_socialMediaDao.deleteSocialMedia(idSocialMedia);
	}

	@Override
	public void deleteSocialMedia(SocialMedia socialMedia) {
		_socialMediaDao.deleteSocialMedia(socialMedia);
	}

	@Override
	public void updateSocialMedia(SocialMedia socialMedia) {
		_socialMediaDao.updateSocialMedia(socialMedia);
	}

	@Override
	public List<SocialMedia> findAllSocialMedia() {
		return _socialMediaDao.findAllSocialMedia();
	}

	@Override
	public SocialMedia findById(Long idSocialMedia) {
		return _socialMediaDao.findById(idSocialMedia);
	}

	@Override
	public SocialMedia findByName(String name) {
		return _socialMediaDao.findByName(name);
	}

	@Override
	public TeacherSocialMedia findByIdAndName(Long idSocialMedia, String nickname) {
		return _socialMediaDao.findByIdAndName(idSocialMedia, nickname);
	}

}
