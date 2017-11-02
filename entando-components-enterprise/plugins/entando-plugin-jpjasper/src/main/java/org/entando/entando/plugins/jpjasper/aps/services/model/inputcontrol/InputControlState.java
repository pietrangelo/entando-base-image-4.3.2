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
package org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol;

import java.util.ArrayList;
import java.util.List;

public class InputControlState {

	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}

	public String getUri() {
		return _uri;
	}
	public void setUri(String uri) {
		this._uri = uri;
	}
	
	/**
	 * Value e options sono alternativi. Dipende dal tipo di inputControl. Nel caso di liste è valorizzato options, diversamente value
	 * @return
	 */
	public String getValue() {
		return _value;
	}
	public void setValue(String value) {
		this._value = value;
	}
	
	/**
	 * Value e options sono alternativi. Dipende dal tipo di inputControl. Nel caso di liste è valorizzato options, diversamente value
	 * 
	 * @return
	 */
	public List<InputControlStateOption> getOptions() {
		return _options;
	}
	public void setOptions(List<InputControlStateOption> options) {
		this._options = options;
	}

	/**
	 * Se options è valorizzato restituisce l'indice della lista dove selected == true
	 * @return
	 */
	public List<Integer> getSelectedOptionIndex() {
		List<Integer> list = null;
		if (null != this.getOptions() && !this.getOptions().isEmpty()) {
			list = new ArrayList<Integer>();
			for (int i = 0; i< this.getOptions().size(); i++) {
				InputControlStateOption opt = this.getOptions().get(i);
				if (opt.isSelected()) list.add(i);
			}
		}
		return list;
	}


	public String getError() {
		return _error;
	}
	public void setError(String error) {
		this._error = error;
	}


	private String _id;
	private String _uri;
	private String _value;
	private String _error;
	
	private List<InputControlStateOption> _options;

}
