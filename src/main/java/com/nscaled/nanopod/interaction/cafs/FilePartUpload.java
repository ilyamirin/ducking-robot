package com.nscaled.nanopod.interaction.cafs;

import lombok.Data;

/**
 *
 * @author ilamirin
 */
@Data
public class FilePartUpload {

    private String fileVersionKey;
    private long index;
    private byte[] content;
}
