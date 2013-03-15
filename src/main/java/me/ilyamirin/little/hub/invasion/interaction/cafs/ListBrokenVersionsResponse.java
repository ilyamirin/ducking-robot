package me.ilyamirin.little.hub.invasion.models;

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
