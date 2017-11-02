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
package org.entando.entando.plugins.jpjasper.aps.services.model;

/**
 *
 * @author rinaldo
 * INPUT CONTROLS
 */
public class JasperReportParameterModel implements java.io.Serializable {
    private String uriString;
    private String name;
    private String label;
    private int inputType;
    private String inputValue;
    private String dataType;

    public JasperReportParameterModel() {
    }

    public JasperReportParameterModel(
            int inputType,
            String uriString,
            String name,
            String label,
            String inputValue,
            String dataType) {
        this.inputType = inputType;
        this.uriString = uriString;
        this.name = name;
        this.label = label;
        this.inputValue = inputValue;
        this.dataType = dataType;
    }

    public String getUriString() {
        return this.uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getInputType() {
        return this.inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public String getInputValue() {
        return this.inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
