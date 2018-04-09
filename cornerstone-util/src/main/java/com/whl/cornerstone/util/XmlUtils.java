package com.whl.cornerstone.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by whling on 2018/4/10.
 */
public class XmlUtils {

    public static <T> String toXmlString(T obj)
            throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(new Class[]{obj.getClass()});
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(obj, stringWriter);
        return stringWriter.toString();
    }

    public static <T> T toObject(String str, Class<T> clazz)
            throws JAXBException {
        StringReader stringReader = new StringReader(str);
        JAXBContext jaxbContext = JAXBContext.newInstance(new Class[]{clazz});
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (T) unmarshaller.unmarshal(stringReader);
    }
}
