package me.ilyamirin.little.hub.invasion.models;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class CreateFileVersionRequest {

    private String sessionId;
    private FileVersion version;
}
