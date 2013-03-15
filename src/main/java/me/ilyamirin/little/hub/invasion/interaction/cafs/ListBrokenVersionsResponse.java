package me.ilyamirin.little.hub.invasion.interaction.cafs;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 *
 * @author ilamirin
 */
@Data
@XmlRootElement
public class ListBrokenVersionsResponse extends ResponseEntity {

    private List<FileVersionIdentifier> brokenVersions;
}
