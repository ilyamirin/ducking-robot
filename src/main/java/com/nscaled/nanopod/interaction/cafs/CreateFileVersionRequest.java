package com.nscaled.nanopod.interaction.cafs;

import com.nscaled.nanopod.interaction.cafs.FileVersion;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class CreateFileVersionRequest {

    private String sessionId;
    private FileVersion version;
}
