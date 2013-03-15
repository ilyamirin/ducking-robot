package me.ilyamirin.little.hub.invasion.interaction.cafs;

import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ilamirin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadPartsRequest {

    private Collection<FilePartUpload> fileParts;
}
