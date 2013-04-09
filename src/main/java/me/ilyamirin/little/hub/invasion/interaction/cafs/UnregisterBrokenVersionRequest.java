package me.ilyamirin.little.hub.invasion.interaction.cafs;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 *
 * @author ilamirin
 */
@Data
@XmlRootElement
public class UnregisterBrokenVersionRequest extends RequestEntity {

    private String targetId;
    private String path;
    private long versionId;
}
