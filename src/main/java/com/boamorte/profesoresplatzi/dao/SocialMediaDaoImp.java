package com.boamorte.profesoresplatzi.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.boamorte.profesoresplatzi.model.SocialMedia;
import com.boamorte.profesoresplatzi.model.TeacherSocialMedia;

@Repository
@Transactional
public class SocialMediaDaoImp extends AbstractSession implements SocialMediaDao {
	
	@Override
	public void saveSocialMedia(SocialMedia socialMedia) {
		try {
			getSession().persist(socialMedia);
		} catch(Exception e) {
			System.out.println("Error al crear al SocialMedia: " + e.getMessage());
		}
	}

	@Override
	public void deleteSocialMedia(Long idSocialMedia) {
		try {
			SocialMedia socialMedia = findById(idSocialMedia);
			if (socialMedia != null) {
				getSession().delete(socialMedia);
			}			
		} catch(Exception e) {
			System.out.println("Error al borrar el SocialMedia: " + e.getMessage());
		}
	}
	
	@Override
	public void deleteSocialMedia(SocialMedia socialMedia) {
		try {
			SocialMedia socialMediaResponse = findById(socialMedia.getId());
			if (socialMediaResponse != null) {
				getSession().delete(socialMedia);
			}			
		} catch(Exception e) {
			System.out.println("Error al borrar el SocialMedia: " + e.getMessage());
		}
	}

	@Override
	public void updateSocialMedia(SocialMedia socialMedia) {
		try {
			getSession().update(socialMedia);
		} catch(Exception e) {
			System.out.println("Error al actualizar el SocialMedia: " + e.getMessage());
		}
	}

	@Override
	public List<SocialMedia> findAllSocialMedia() {
		return getSession().createQuery("from SocialMedia").list();
	}

	@Override
	public SocialMedia findById(Long idSocialMedia) {
		return (SocialMedia) getSession().get(SocialMedia.class, idSocialMedia);
	}

	@Override
	public SocialMedia findByName(String name) {
		return (SocialMedia) getSession().createQuery(
				"from SocialMedia where name = :name")
				.setParameter("name", name).uniqueResult();
	}

	@Override
	public TeacherSocialMedia findByIdAndName(Long idSocialMedia, String nickname) {
		// TODO Auto-generated method stub
		List<Object[]> objects = getSession().createQuery(
				"from TeacherSocialMedia tsm join tsm.socialmedia sm "
				+ "where sm.id = :idSocialMedia and tsm.nickname = :nickname")
				.setParameter("idSocialMedia", idSocialMedia)
				.setParameter("nickname", nickname).list();
		
		if (objects.size() != 0) {
			for (Object[] objects2 : objects) {
				for (Object object : objects2) {
					if (object instanceof TeacherSocialMedia) {
						return (TeacherSocialMedia) object;						
					}
				}
			}
		}
		
		return null;
		
	}
	
	@Override
	public TeacherSocialMedia findSocialMediaByIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia) {
		// TODO Auto-generated method stub
		
		List<Object[]> objs = getSession().createQuery(
				"from TeacherSocialMedia tsm join tsm.socialmedia sm "
				+ "join tsm.teacher t where sm.id = :id_social_media "
				+ "and t.id = :id_teacher")
				.setParameter("id_social_media", idSocialMedia)
				.setParameter("id_teacher", idTeacher).list();
		
		if (objs.size()>0) {
			for (Object[] objects : objs) {
				for (Object object : objects) {
					if (object instanceof TeacherSocialMedia) {
						return (TeacherSocialMedia) object;
					}
				}
			}
		}
		return null;
		
	}
}
