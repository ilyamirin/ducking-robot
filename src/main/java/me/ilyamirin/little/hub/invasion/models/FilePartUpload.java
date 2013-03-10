package me.ilyamirin.little.hub.invasion.models;

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
