package com.nscaled.nanopod.interaction.console;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sessionType",
    "correlationId"
})
@XmlRootElement(name = "AuthSessionRequest")
public class AuthSessionRequest extends BaseAuthenticatedRequestType {

    @XmlElement(name = "SessionType", required = true)
    protected AuthSessionTypeEnum sessionType;
    @XmlElement(name = "CorrelationId", required = true)
    protected String correlationId;

    /**
     * Gets the value of the sessionType property.
     *
     * @return possible object is {@link AuthSessionTypeEnum }
     *
     */
    public AuthSessionTypeEnum getSessionType() {
        return sessionType;
    }

    /**
     * Sets the value of the sessionType property.
     *
     * @param value allowed object is {@link AuthSessionTypeEnum }
     *
     */
    public void setSessionType(AuthSessionTypeEnum value) {
        this.sessionType = value;
    }

    /**
     * Gets the value of the correlationId property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * Sets the value of the correlationId property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCorrelationId(String value) {
        this.correlationId = value;
    }
}
