package me.ilyamirin.little.hub.invasion.interaction.cafs;

import me.ilyamirin.little.hub.invasion.interaction.cafs.FileVersion;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class CreateFileVersionRequest {

    private String sessionId;
    private FileVersion version;
}
