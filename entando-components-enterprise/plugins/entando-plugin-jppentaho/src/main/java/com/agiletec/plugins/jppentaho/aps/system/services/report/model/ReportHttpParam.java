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
package com.agiletec.plugins.jppentaho.aps.system.services.report.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Pojo for HTTP parameters for identify a biserver report
 * */
public class ReportHttpParam implements Serializable {
    
    public ReportHttpParam() {}
    
    public ReportHttpParam(String name, String path, String solution) {
        this.setName(name);
        this.setPath(path);
        this.setSolution(solution);
    }

    public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }

    public String getPath() {
        return _path;
    }
    public void setPath(String path) {
        this._path = path;
    }

    public String getSolution() {
        return _solution;
    }
    public void setSolution(String solution) {
        this._solution = solution;
    }

    public void setDescription(String description) {
        this._description = description;
    }
    public String getDescription() {
        return _description;
    }

    public void setTitle(String title) {
        this._title = title;
    }
    public String getTitle() {
        return _title;
    }

    public void setAction(String action) {
        this._action = action;
    }
    public String getAction() {
        return _action;
    }

    public void setType(int type) {
        this._type = type;
    }
    public int getType() {
        return _type;
    }

    public String toString() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", _name);
        map.put("path", _path);
        map.put("solution", _solution);
        map.put("description", _description);
        map.put("title", _title);
        map.put("action", _action);
        map.put("type", String.valueOf(_type));
        return map.toString();
    }
    
    private String _name;
    private String _path;
    private String _solution;
    private String _description;
    private String _title;
    private String _action;
    private int _type;
    public final static int TYPE_PRPT = 0;
    public final static int TYPE_XACTION = 1;
    public final static int TYPE_CDF = 2;
    
}