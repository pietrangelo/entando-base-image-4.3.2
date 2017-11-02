package org.entando.entando.plugins.jpsocial.aps.system.services.socialpost;

import java.util.List;
import java.util.Date;
import com.agiletec.aps.system.common.FieldSearchFilter;

public interface ISocialPostDAO {

	public List<Integer> searchSocialPosts(FieldSearchFilter[] filters);
	
	public SocialPost loadSocialPost(int id);

	public List<Integer> loadSocialPosts();

	public void removeSocialPost(int id);
	
	public void updateSocialPost(SocialPost socialPost);

	public void insertSocialPost(SocialPost socialPost);
	

}