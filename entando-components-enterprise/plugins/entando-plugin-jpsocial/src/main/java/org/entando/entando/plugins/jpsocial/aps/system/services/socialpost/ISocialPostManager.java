package org.entando.entando.plugins.jpsocial.aps.system.services.socialpost;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Date;
import com.agiletec.aps.system.common.FieldSearchFilter;

public interface ISocialPostManager {

	public SocialPost getSocialPost(int id) throws ApsSystemException;

	public List<Integer> getSocialPosts() throws ApsSystemException;

	public List<Integer> searchSocialPosts(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addSocialPost(SocialPost socialPost) throws ApsSystemException;

	public void updateSocialPost(SocialPost socialPost) throws ApsSystemException;

	public void deleteSocialPost(int id) throws ApsSystemException;

}