/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsocial.apsadmin.social.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * This object contains all the attributes list of the object to post, tied to
 * the given user.
 *
 * @author entando
 */
public class Post {

	private static final Logger _logger =  LoggerFactory.getLogger(Post.class);

  public Post(String username, Content content) {
    this._username = username;
    this._content = content;
    this._queue = new HashSet<PostAttribute>();
    this._fast = false;
  }

  public Post(String username) {
    this._username = username;
    this._queue = new HashSet<PostAttribute>();
    this._fast = false;
  }

  /**
   * Return the postAttributes sorted by attribute and language. Attributes
   * retain the same order of the content.
   *
   * @param langs The language of the desired post attributes
   * @return
   */
  public Set<PostAttribute> getSortedQueue(List<Lang> langs) {
    Set<PostAttribute> result = new LinkedHashSet<PostAttribute>();
    try {
      if (null != _queue && !_queue.isEmpty() && null != _content) {
        Iterator<AttributeInterface> contAttrItr = _content.getAttributeList().iterator();

        while (contAttrItr.hasNext()) {
          Iterator<PostAttribute> itr = getQueue().iterator();
          AttributeInterface cnt = contAttrItr.next();

          while (itr.hasNext()) {
            PostAttribute postAttr = itr.next();
//            System.out.println("  Examining " + postAttr.getAttributeName());
            if (!postAttr.isIgnore()
                    && postAttr.getName().equals(JpSocialSystemConstants.ATTRIBUTE_SOCIALDESCRIPTION)) {
//              System.out.println("  DESCRIPTION ONLY " + postAttr.getAttributeName());
              result.add(postAttr);
              // no more attributes expected
              return result;
            } // build index
            else if (!postAttr.isIgnore()
                    && postAttr.getName().equals(cnt.getName())) {
              // iterate langs if possible
              if (null != langs && !langs.isEmpty()) {
                Iterator<Lang> lItr = langs.iterator();

                while (lItr.hasNext()) {
                  String langCode = lItr.next().getCode();
                  // add the current post attribute if the case
                  if (postAttr.getLang().equals(langCode)) {
                    result.add(postAttr);
                  }
                }
              } else {
                // adds attributes ignoring languages
                result.add(postAttr);
              }
            }
          }
        }
      } else {
        _logger.debug("empty queue to deliver!");
      }
    } catch (Throwable t) {
    	_logger.error("error in getSortedQueue", t);
    }
    return result;
  }

  /**
   * Return the given attribute from the queue. Search is case insensitive
   *
   * @param id
   * @return
   */
  public PostAttribute getAttribyteById(String id) {
    PostAttribute result = null;

    if (null != id && !"".equals(id.trim())
            && null != this._queue && !this._queue.isEmpty()) {
      Iterator<PostAttribute> itr = this.getQueue().iterator();

      while (itr.hasNext()) {
        PostAttribute attribute = itr.next();
        if (attribute.getId().equalsIgnoreCase(id)) {
          return attribute;
        }
      }
    }
    return result;
  }

  /**
   * Reenable all the attributes previously marked "ignored" in the delivery
   * queue and set their status to unsent
   */
  public void restoreAttributes() throws Throwable{

    if (null != this._queue && !this._queue.isEmpty()) {
      Iterator<PostAttribute> itr = this._queue.iterator();

      while (itr.hasNext()) {
        PostAttribute attribute = itr.next();

        attribute.setIgnore(false);
        attribute.setSubmitted(false);
      }
    }
  }

  /**
   * After a delivery check what attributes where not dispatched
   *
   * @return a map indexed by delivery queue with list of unsent attributes
   */
  public Map<String, List<String>> detectUnsentAttributes() {
    Map<String, List<String>> result = new HashMap<String, List<String>>();

    if (null != this._queue && !this._queue.isEmpty()) {
      Iterator<PostAttribute> itr = this._queue.iterator();

      while (itr.hasNext()) {
        PostAttribute attribute = itr.next();

        if (!attribute.isIgnore()) {
          String queue = attribute.getQueue();
          
          if (!attribute.isSubmitted()) {
            List<String> list = result.get(queue);

            // create attribute list of not delivered attributes
            if (null == list) {
              list = new ArrayList<String>();
              result.put(queue, list);
            }
            // add faulting attribute
            list.add(attribute.getName());
          }
        }
      }
    }
    return result;
  }

  public Set<PostAttribute> getQueue() {
    return _queue;
  }

  public void setQueue(Set<PostAttribute> queue) {
    this._queue = queue;
  }

  public String getUsername() {
    return _username;
  }

  public void setUsername(String username) {
    this._username = username;
  }

  public Content getContent() {
    return _content;
  }

  public void setContent(Content _content) {
    this._content = _content;
  }

  /**
   * check whether to ask for user confirmation before posting or not.
   *
   * @return tre the user must be asked for confirmation before submit
   */
  public boolean isFast() {
    return _fast;
  }

  public String getFast() {
    return String.valueOf(_fast);
  }

  public void setFast(boolean fast) {
    this._fast = fast;
  }

  public String getOrigin() {
    return _origin;
  }

  public void setOrigin(String _origin) {
    this._origin = _origin;
  }
  
  private Set<PostAttribute> _queue = new LinkedHashSet<PostAttribute>();
  private Content _content;
  private String _username;
  private boolean _fast;
  private String _origin;

}
