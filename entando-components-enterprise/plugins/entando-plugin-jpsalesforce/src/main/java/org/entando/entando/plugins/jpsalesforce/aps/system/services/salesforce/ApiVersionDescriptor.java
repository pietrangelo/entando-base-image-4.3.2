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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce;

import org.json.JSONObject;

/**
 * Description of a REST API version made available by salesforce for integration
 * with third part software
 * @author entando
 */
public class ApiVersionDescriptor {

  public ApiVersionDescriptor(String label, String url, String version) throws Throwable {
    _label = label;
    _url = url;
    _version = version;
  }

  /**
   * Map the JSON description into java class
   * {
   *  "label":"Winter '11",
   *  "url":"/services/data/v20.0",
   *  "version":"20.0"
   * } ...
   * @param obj
   * @throws Throwable
   */
  public ApiVersionDescriptor(JSONObject obj) throws Throwable {
    _label =  obj.getString(LABEL);
    _url = obj.getString(URL);
    _version = obj.getString(VERSION);
  }

  public String getLabel() {
    return _label;
  }

  protected void setLabel(String label) {
    this._label = label;
  }

  public String getUrl() {
    return _url;
  }

  protected void setUrl(String url) {
    this._url = url;
  }

  public String getVersion() {
    return _version;
  }

  protected void setVersion(String version) {
    this._version = version;
  }

  private String _label;
  private String _url;
  private String _version;


  public final static String LABEL = "label";
  public final static String URL = "url";
  public final static String VERSION = "version";
}
