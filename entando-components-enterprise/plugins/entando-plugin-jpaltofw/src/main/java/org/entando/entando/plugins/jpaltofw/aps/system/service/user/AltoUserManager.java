/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpaltofw.aps.system.service.user;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.system.services.user.UserManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.plugins.jpaltofw.aps.system.service.client.GokibiClient;
import org.entando.entando.plugins.jpaltofw.aps.system.service.client.GokibiInstance;
import org.entando.entando.plugins.jpaltofw.aps.system.service.client.IGokibiInstancesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Manager for AltoUser.
 * @author E.Santoboni
 */
public class AltoUserManager extends UserManager {
	
	private static final Logger _logger = LoggerFactory.getLogger(AltoUserManager.class);

	@Override
	protected void release() {
		super.release();
		this.localCache.clear();
	}
	
    @Override
    public UserDetails getUser(String username, String password) throws ApsSystemException {
        try {
			GokibiClient client = new GokibiClient();
			List<GokibiInstance> instances = this.getAltoInstancesManager().getInstances();
			for (int i = 0; i < instances.size(); i++) {
				GokibiInstance instance = instances.get(i);
				UserDetails user = client.getUser(username, password, instance);
				if (null != user) {
					return user;
				}
			}
        } catch (Throwable t) {
        	_logger.error("error on user login - '{}'", username , t);
        }
		return super.getUser(username, password);
    }
    
    @Override
    public UserDetails getUser(String username) throws ApsSystemException {
		if (localCache.contains(username)) {
			AltoUser user = new AltoUser();
			user.setUsername(username);
			return user;
		}
        return super.getUser(username);
    }
    
    @Override
    public List<UserDetails> getUsers() throws ApsSystemException {
        return this.searchUsers(null);
    }
    
    @Override
    public List<UserDetails> searchUsers(String text) throws ApsSystemException {
        List<UserDetails> users = super.searchUsers(text);
        try {
			GokibiClient client = new GokibiClient();
			List<GokibiInstance> instances = this.getAltoInstancesManager().getInstances();
			for (int i = 0; i < instances.size(); i++) {
				GokibiInstance instance = instances.get(i);
				List<String> usernames = client.getUsernames(instance.getBaseUrl(), text);
				for (int j = 0; j < usernames.size(); j++) {
					String username = usernames.get(j);
					this.localCache.add(username);
					AltoUser user = new AltoUser();
					user.setInstanceId(instance.getId());
					user.setUsername(username);
					users.add(user);
				}
			}
        } catch (Throwable t) {
        	_logger.error("error searching users by text '{}'", text , t);
        }
        if (null != users) {
            BeanComparator comparator = new BeanComparator("username");
            Collections.sort(users, comparator);
        }
        return users;
    }
    
    @Override
    public List<String> getUsernames() throws ApsSystemException {
        return this.searchUsernames(null);
    }
    
    @Override
    public List<String> searchUsernames(String text) throws ApsSystemException {
        List<String> usernames = super.searchUsernames(text);
        try {
            GokibiClient client = new GokibiClient();
			List<GokibiInstance> instances = this.getAltoInstancesManager().getInstances();
			for (int i = 0; i < instances.size(); i++) {
				GokibiInstance instance = instances.get(i);
				List<String> altoUsernames = client.getUsernames(instance.getBaseUrl(), text);
				this.localCache.addAll(altoUsernames);
				usernames.addAll(altoUsernames);
			}
        } catch (Throwable t) {
        	_logger.error("error searching usernames by text '{}'", text , t);
        }
        if (null != usernames) {
            Collections.sort(usernames);
        }
        return usernames;
    }
    
	public IGokibiInstancesManager getAltoInstancesManager() {
		return altoInstancesManager;
	}
	public void setAltoInstancesManager(IGokibiInstancesManager altoInstancesManager) {
		this.altoInstancesManager = altoInstancesManager;
	}
	
	private IGokibiInstancesManager altoInstancesManager;
	
	private Set<String> localCache = new HashSet<String>();
	
}