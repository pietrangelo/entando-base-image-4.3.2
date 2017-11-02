/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services;

import org.entando.entando.plugins.jpnotify.aps.JpnotifyBaseTestCase;
import org.entando.entando.plugins.jpnotify.aps.system.services.notify.INotifyManager;

public class TestNotifyManager extends JpnotifyBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetNotify() {
		//TODO complete test
		assertNotNull(this._notifyManager);
	}

	public void testGetNotifys() {
		//TODO complete test
		assertNotNull(this._notifyManager);
	}
	
	public void testSearchNotifys() {
		//TODO complete test
		assertNotNull(this._notifyManager);
	}

	public void testAddNotify() {
		//TODO complete test
		assertNotNull(this._notifyManager);
	}

	public void testUpdateNotify() {
		//TODO complete test
		assertNotNull(this._notifyManager);
	}

	public void testDeleteNotify() {
		//TODO complete test
		assertNotNull(this._notifyManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._notifyManager = (INotifyManager) this.getService("jpnotifyNotifyManager");
	}
	
	private INotifyManager _notifyManager;
}

