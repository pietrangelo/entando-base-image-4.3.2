package org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.SocialPost;


public class SocialPostChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((SocialPostChangedObserver) srv).updateFromSocialPostChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return SocialPostChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public SocialPost getSocialPost() {
		return _socialPost;
	}
	public void setSocialPost(SocialPost socialPost) {
		this._socialPost = socialPost;
	}

	private SocialPost _socialPost;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
