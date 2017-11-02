package org.entando.entando.plugins.jpsocial.aps.system.services.socialpost;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.api.JAXBSocialPost;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.event.SocialPostChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;

public class SocialPostManager extends AbstractService implements ISocialPostManager {

	private static final Logger _logger =  LoggerFactory.getLogger(SocialPostManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
 
	@Override
	public SocialPost getSocialPost(int id) throws ApsSystemException {
		SocialPost socialPost = null;
		try {
			socialPost = this.getSocialPostDAO().loadSocialPost(id);
		} catch (Throwable t) {
			_logger.error("Error loading socialPost with id {}", id, t);
			throw new ApsSystemException("Error loading socialPost with id: " + id, t);
		}
		return socialPost;
	}

	@Override
	public List<Integer> getSocialPosts() throws ApsSystemException {
		List<Integer> socialPosts = new ArrayList<Integer>();
		try {
			socialPosts = this.getSocialPostDAO().loadSocialPosts();
		} catch (Throwable t) {
			_logger.error("Error loading SocialPost", t);
			throw new ApsSystemException("Error loading SocialPost ", t);
		}
		return socialPosts;
	}

	@Override
	public List<Integer> searchSocialPosts(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> socialPosts = new ArrayList<Integer>();
		try {
			socialPosts = this.getSocialPostDAO().searchSocialPosts(filters);
		} catch (Throwable t) {
			_logger.error("Error searching SocialPosts ", t);
			throw new ApsSystemException("Error searching SocialPosts ", t);
		}
		return socialPosts;
	}

	@Override
	public void addSocialPost(SocialPost socialPost) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			socialPost.setId(key);
			this.getSocialPostDAO().insertSocialPost(socialPost);
			this.notifySocialPostChangedEvent(socialPost, SocialPostChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding SocialPost", t);
			throw new ApsSystemException("Error adding SocialPost", t);
		}
	}
 
	@Override
	public void updateSocialPost(SocialPost socialPost) throws ApsSystemException {
		try {
			this.getSocialPostDAO().updateSocialPost(socialPost);
			this.notifySocialPostChangedEvent(socialPost, SocialPostChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating SocialPost", t);
			throw new ApsSystemException("Error updating SocialPost " + socialPost, t);
		}
	}

	@Override
	public void deleteSocialPost(int id) throws ApsSystemException {
		try {
			SocialPost socialPost = this.getSocialPost(id);
			this.getSocialPostDAO().removeSocialPost(id);
			this.notifySocialPostChangedEvent(socialPost, SocialPostChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting SocialPost wiwh id: {}", id, t);
			throw new ApsSystemException("Error deleting SocialPost wiwh id:" + id, t);
		}
	}


/*
    public List<SocialPost> getSocialPostsForApi(Properties properties) throws Throwable {
        String holder = properties.getProperty("holder");
        return this.searchSocialPosts(holder);
    }
*/
	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/socialPost?id=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
    public JAXBSocialPost getSocialPostForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("id");
        int id = 0;
		JAXBSocialPost jaxbSocialPost = null;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        SocialPost socialPost = this.getSocialPost(id);
        if (null == socialPost) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "SocialPost with id '" + idString + "' does not exist", Response.Status.CONFLICT);
        }
        jaxbSocialPost = new JAXBSocialPost(socialPost);
        return jaxbSocialPost;
    }

    /**
     * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/socialPost 
     * @param jaxbSocialPost
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void addSocialPostForApi(JAXBSocialPost jaxbSocialPost) throws ApiException, ApsSystemException {
        if (null != this.getSocialPost(jaxbSocialPost.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "SocialPost with id " + jaxbSocialPost.getId() + " already exists", Response.Status.CONFLICT);
        }
        SocialPost socialPost = jaxbSocialPost.getSocialPost();
        this.addSocialPost(socialPost);
    }

    /**
     * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/socialPost 
     * @param jaxbSocialPost
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void updateSocialPostForApi(JAXBSocialPost jaxbSocialPost) throws ApiException, ApsSystemException {
        if (null == this.getSocialPost(jaxbSocialPost.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "SocialPost with id " + jaxbSocialPost.getId() + " does not exist", Response.Status.CONFLICT);
        }
        SocialPost socialPost = jaxbSocialPost.getSocialPost();
        this.updateSocialPost(socialPost);
    }

    /**
     * DELETE http://localhost:8080/<portal>/api/rs/en/socialPost?id=1
	 * @param properties
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void deleteSocialPostForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("id");
        int id = 0;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        this.deleteSocialPost(id);
    }

	private void notifySocialPostChangedEvent(SocialPost socialPost, int operationCode) {
		SocialPostChangedEvent event = new SocialPostChangedEvent();
		event.setSocialPost(socialPost);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}
	
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setSocialPostDAO(ISocialPostDAO socialPostDAO) {
		 this._socialPostDAO = socialPostDAO;
	}
	protected ISocialPostDAO getSocialPostDAO() {
		return _socialPostDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private ISocialPostDAO _socialPostDAO;
}
