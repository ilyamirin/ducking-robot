package com.nscaled.nanopod.interaction.cafs;

import com.nscaled.nanopod.interaction.cafs.FileVersion;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;
import com.google.common.collect.Lists;

public class File {

    /**
     * We only return this as a media type for external consumers(Console)
     */
    public static final String FOLDER_MEDIA_TYPE = "folder";
    private String targetId;
    private String path;
    private boolean isFolder;
    private List<FileVersion> versions = Lists.newLinkedList();
    private String mediaType;
    private Integer versionsCount;

    public List<FileVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<FileVersion> versions) {
        this.versions = versions;
    }

    public File withTargetId(String targetId) {
        setTargetId(targetId);
        return this;
    }

    public File withPath(String path) {
        setPath(path);
        return this;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("isFolder")
    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    /**
     * Gets the Media type, which is almost the same as MIME type
     *
     * @return the mediaType
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * @param mediaType the mediaType to set
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getVersionsCount() {
        return versionsCount;
    }

    public void setVersionsCount(Integer versionsCount) {
        this.versionsCount = versionsCount;
    }
}
