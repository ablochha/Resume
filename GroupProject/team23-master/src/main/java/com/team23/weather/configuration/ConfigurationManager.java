package com.team23.weather.configuration;

import com.team23.weather.serialization.ISerializer;

import java.io.*;

/**
 * This class implements {@link com.team23.weather.configuration.IConfigurationManager}.
 * It stores the {@link com.team23.weather.configuration.Configuration} object to disk and reads it from there.
 *
 * @author Saquib Mian
 */
public class ConfigurationManager implements IConfigurationManager {

    /**
     * the serializer to use when persisting/reading the configuration
     */
    private final ISerializer _serializer;

    /**
     * the path to the config file
     */
    private final String s_configFilePath = "config.json";

    public ConfigurationManager(ISerializer serializer) {
        _serializer = serializer;
    }

    /**
     * {@inheritDoc}
     * The backing store is the disk
     */
    public Configuration load() {
        Configuration toReturn = Configuration.getDefaults();

        File file = new File(s_configFilePath);
        if( !file.exists() ) {
            return toReturn;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            toReturn = _serializer.deserialize(reader, Configuration.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return toReturn;
    }

    /**
     * {@inheritDoc}
     * The backing store is the disk
     */
    public void save(Configuration config) {
        File file = new File(s_configFilePath);
        Writer writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(_serializer.serialize(config));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
