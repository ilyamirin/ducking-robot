package me.ilyamirin.little.hub.invasion.models;

import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class FileVersion {

    private static final int TO_STRING_HASHES_LIMIT = 50;
    private File file;
    private Integer chunksCount;
    private Long sizeInBytes;
    protected List<String> hashes = Lists.newLinkedList();
    private Long lastModificationDate;
    private String key;
    /**
     * It is version unique(per file) number
     */
    private Long version;

    public static FileVersion of(File file) {
        FileVersion fs = new FileVersion();
        fs.setFile(file);
        return fs;
    }

    public synchronized void setHashes(List<String> hashes) {
        this.hashes = hashes;

        if (hashes != null) {
            this.chunksCount = hashes.size();
        }

    }

    public List<String> getHashes() {
        return hashes;
    }

    /**
     * Gets the version ID (timestamp when this version has been created on
     * CAFS).
     */
    @JsonIgnore // we have a getVersion() method following the JavaBeans convention
    public Long getVersionId() {
        return version;
    }

    public void setVersionId(Long version) {
        this.version = version;
    }

    /**
     * Gets the last modification date.
     */
    public Long getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * Sets the last modification date.
     */
    public void setLastModificationDate(Long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public FileVersion withVersion(Long version) {
        setVersionId(version);
        return this;
    }

    public FileVersion withHashes(String... hashes) {
        setHashes(Arrays.asList(hashes));
        return this;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public Integer getChunksCount() {
        return chunksCount;
    }

    public void setChunksCount(Integer chunksCount) {
        this.chunksCount = chunksCount;
    }

    public void setSizeInBytes(Long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public Long getSizeInBytes() {
        return sizeInBytes;
    }

    public Long getVersion() {
        return version;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "FileVersion [file=" + file
                + ", version=" + version
                + ", sizeInBytes=" + sizeInBytes
                + ", lastModificationDate=" + lastModificationDate
                + ", chunksCount=" + chunksCount
                + ", hashes=" + hashesLimited()
                + ", key=" + key
                + "]";
    }

    private String hashesLimited() {
        if (hashes == null || hashes.size() <= TO_STRING_HASHES_LIMIT) {
            return String.valueOf(hashes);
        }

        int count = hashes.size();
        StringBuilder b = new StringBuilder();
        b.append("[");
        for (int i = 0; i < TO_STRING_HASHES_LIMIT; i++) {
            b.append(hashes.get(i)).append(",");
        }
        b.append("... (");
        b.append(count);
        b.append(" chunks total) ");
        b.append("]");

        return b.toString();
    }
}
