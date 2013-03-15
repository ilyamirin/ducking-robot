package me.ilyamirin.little.hub.invasion.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@XStreamAlias("AuthSessionResponse")
public class AuthSessionResponse {

    private boolean status;
    @XStreamAlias("SessionId")
    private String sessionId;
}
