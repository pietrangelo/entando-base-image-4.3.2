package org.entando.entando.plugins.jpaltofw.aps.services.parse;

/*

<instances>
    <instance>
        <id>alto1</id>
        <baseUrl>http://localhost:8081/alto/</baseUrl>
        <pid>alto</pid>
        <projectPassword>password</projectPassword>
    </instance>
</instances>
*/


import org.entando.entando.plugins.jpaltofw.aps.services.AltoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.StringReader;
import java.io.StringWriter;

@XmlRootElement(name = "instances")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"config"})
public class AltoConfigDOM {

    @XmlTransient
    Logger logger = LoggerFactory.getLogger(AltoConfigDOM.class);

    @XmlElement(name = "instance")
    private AltoConfig config;

    public AltoConfig extractConfig(String xml) throws JAXBException {
        Unmarshaller um = JAXBContext.newInstance(AltoConfigDOM.class).createUnmarshaller();
        return ((AltoConfigDOM) um.unmarshal(new StringReader(xml))).getConfig();
    }

    public String createConfigXml(final AltoConfig config) throws JAXBException {
        java.io.StringWriter sw = new StringWriter();
        Marshaller marshaller = JAXBContext.newInstance(AltoConfigDOM.class).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        AltoConfigDOM dom = new AltoConfigDOM();
        dom.setConfig(config);
        marshaller.marshal(dom, sw);
        return sw.toString();
    }

    public AltoConfig getConfig() {
        return config;
    }

    public void setConfig(AltoConfig config) {
        this.config = config;
    }
}
