package com.nscaled.nanopod.interaction.cafs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileVersionIdentifier {

    protected String targetId;
    protected String path;
    protected long versionId;
}
