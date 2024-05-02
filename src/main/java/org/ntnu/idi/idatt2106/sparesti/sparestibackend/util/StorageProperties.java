package org.ntnu.idi.idatt2106.sparesti.sparestibackend.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class containing the location of the folder for storing uploaded files.
 *
 * @author L.M.L Nilsen
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "/upload-dir";
}
