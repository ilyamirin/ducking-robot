package me.ilyamirin.little.hub.invasion.interaction.cafs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@ToString
@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class CafsResponse<E extends ResponseEntity> {

    public static final String ERROR_FIELD_NAME = "error";
    public static final String ERROR_CODE_FIELD_NAME = "error_code";
    @JsonProperty(CafsResponse.ERROR_CODE_FIELD_NAME)
    @XmlElement(name = CafsResponse.ERROR_CODE_FIELD_NAME)
    private String errorCode;
    @JsonProperty(CafsResponse.ERROR_FIELD_NAME)
    @XmlElement(name = CafsResponse.ERROR_FIELD_NAME)
    private String errorMessage;
    private Object request;
    private E response;

    public CafsResponse() {
    }

    /**
     * Tells whether or not this object describes an error condition.
     *
     * @return true if, and only if, this object denotes an error
     */
    public boolean hasErrors() {
        return errorCode != null && !errorCode.isEmpty();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonIgnore
    public String getErrorPhrase() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.errorMessage).append(" (code: ").append(this.errorCode).append(")");
        return builder.toString();
    }

    // this is to save some time on client upon deserialization
    @XmlTransient
    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    /**
     * @param entity
     */
    public void setEntityToResponse(E entity) {
        this.response = entity;
    }

    public void setNoResponseExpected() {
        this.response = null;
    }

    /**
     * get entity from response
     *
     * @return
     */
    @JsonIgnore
    @XmlTransient
    public E getEntityFromResponse() {
        return this.getResponse();
    }

    public E getResponse() {
        return this.response;
    }
}
