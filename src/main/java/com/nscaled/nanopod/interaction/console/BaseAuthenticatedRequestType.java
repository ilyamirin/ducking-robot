package com.nscaled.nanopod.interaction.console;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseAuthenticatedRequestType", propOrder = {
    "uuid",
    "token"
})
@XmlSeeAlso({AuthSessionRequest.class})
public abstract class BaseAuthenticatedRequestType {

    @XmlElement(name = "Uuid", required = true)
    protected String uuid;
    @XmlElement(name = "Token", required = true)
    protected String token;

    /**
     * Gets the value of the uuid property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setUuid(String value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the token property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setToken(String value) {
        this.token = value;
    }
}
