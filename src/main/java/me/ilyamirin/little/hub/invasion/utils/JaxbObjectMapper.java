package me.ilyamirin.little.hub.invasion.utils;

import com.google.common.base.Joiner;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.datatype.XMLGregorianCalendar;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class JaxbObjectMapper extends ObjectMapper {

    private static class CamelCaseObjectMapperStrategy extends PropertyNamingStrategy {

        @Override
        public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
            return convert(defaultName);
        }

        @Override
        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return convert(defaultName);
        }

        @Override
        public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return convert(defaultName);
        }

        private String convert(String str) {
            return str == null || str.isEmpty() ?
                    str :
                    Joiner.on("").join(Character.toTitleCase(str.charAt(0)), str.substring(1));
        }
    }
    private final static PropertyNamingStrategy STRATEGY = new CamelCaseObjectMapperStrategy();

    public JaxbObjectMapper() {
        super();
        this.setPropertyNamingStrategy(STRATEGY);
        this.setAnnotationIntrospector(new JaxbAnnotationIntrospector());
        this.getDeserializationConfig().addMixInAnnotations(XMLGregorianCalendar.class, MixIn.class);
    }

    public static interface MixIn {

        @JsonIgnore
        @XmlTransient
        public void setYear(BigInteger year);
    }
}