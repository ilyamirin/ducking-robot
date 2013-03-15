package me.ilyamirin.little.hub.invasion.interaction.cafs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class FileVersionIdentifier {

    protected String targetId;
    protected String path;
    protected long versionId;
}