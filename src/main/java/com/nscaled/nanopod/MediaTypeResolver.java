package com.nscaled.nanopod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import com.nscaled.nanopod.interaction.cafs.File;

/**
 *
 * @author ilyamirin
 */
public class MediaTypeResolver {

    private Properties mediTypes;

    public void loadMediaTypes(String pathToProperties) throws IOException {
        mediTypes = new Properties();
        mediTypes.load(new FileInputStream(pathToProperties));
    }

    public String resolve(String fileName) {
        String ending = fileName.substring(fileName.lastIndexOf(".") + 1);
        return mediTypes.getProperty(ending, "bin");
    }
}
