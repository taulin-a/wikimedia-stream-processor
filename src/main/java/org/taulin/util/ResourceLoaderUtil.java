package org.taulin.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

@UtilityClass
public class ResourceLoaderUtil {
    public static File loadResource(String resourceName) {
        try {
            final URL resourceUrl = ClassLoader.getSystemResource(resourceName);
            return Paths.get(resourceUrl.toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid resource name: " + resourceName);
        }
    }
}
