/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpmultisite.aps.system.services.content;

import org.entando.entando.plugins.jpmultisite.aps.system.services.content.model.SharedContent;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.List;
import static junit.framework.Assert.assertNotNull;
import org.entando.entando.plugins.jpmultisite.apsadmin.ApsAdminPluginBaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestSharedContentManager extends ApsAdminPluginBaseTestCase {
	
	private static final Logger _logger = LoggerFactory.getLogger(TestSharedContentManager.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() {
		this._sharedContentManager = (ISharedContentManager) this.getService("jpmultisiteSharedContentManager");
	}
	
	public void testBeanManager() {
		assertNotNull(this._sharedContentManager);
	}

	public void testAddSharedContent() throws ApsSystemException {
		SharedContent sharedContent = addSharedContent();
		List<Integer> sharedContents = this._sharedContentManager.getSharedContents();
		sharedContents = this._sharedContentManager.getSharedContents();
		assertEquals(1, sharedContents.size());
		sharedContent = (SharedContent) this._sharedContentManager.getSharedContent(sharedContents.get(0));
		_logger.debug("shared content: "+sharedContent.toString());
	}

	public void testUpdateSharedContent() throws ApsSystemException {
		SharedContent sharedContent = addSharedContent();
		sharedContent = (SharedContent) this._sharedContentManager.getSharedContent(sharedContent.getId());
		assertEquals("testSrc", sharedContent.getMultisiteCodeSrc());
		sharedContent.setMultisiteCodeSrc("asd");
		this._sharedContentManager.updateSharedContent(sharedContent);
		sharedContent = this._sharedContentManager.getSharedContent(sharedContent.getId());
		assertEquals("asd", sharedContent.getMultisiteCodeSrc());
		assertNotNull(this._sharedContentManager);
	}
	
	private SharedContent addSharedContent() throws ApsSystemException {
		SharedContent sharedContent = new SharedContent("test", "testSrc", "testTo");
		List<Integer> sharedContents = this._sharedContentManager.getSharedContents();
		assertEquals(0, sharedContents.size());
		this._sharedContentManager.addSharedContent(sharedContent);
		sharedContents = this._sharedContentManager.getSharedContents();
		assertEquals(1, sharedContents.size());
		return sharedContent;
	}

	@Override
	protected void tearDown() throws Exception {
		List<Integer> sharedContents = this._sharedContentManager.getSharedContents();
		for (Integer integer : sharedContents) {
			this._sharedContentManager.deleteSharedContent(integer);
		}
	}
	
	private ISharedContentManager _sharedContentManager;
}

