package gov.nysenate.openleg.util;

import gov.nysenate.openleg.Environment;
import gov.nysenate.util.Config;
import gov.nysenate.util.DB;
import gov.nysenate.util.Mailer;
import gov.nysenate.util.listener.NYSenateConfigurationListener;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class Application
{
    public static Logger logger = Logger.getLogger(Application.class);

    /** Static factory instance */
    protected static Application appInstance = new Application();

    /** Default values */
    public static final String PROD_PROPERTY_FILENAME = "app.properties";
    public static final String TEST_PROPERTY_FILENAME = "test.app.properties";

    /** Dependency instances */
    protected NYSenateConfigurationListener configurationListener;
    protected Config config;
    protected Mailer mailer;
    protected Environment environment;
    protected Storage storage;
    protected XmlHelper xmlhelper;
    protected ObjectMapper objectMapper;
    protected DB db;

    /**
     * Public access call to build()
     * @return boolean - If true then build succeeded
     */
    public static boolean bootstrap()
    {
        return bootstrap(PROD_PROPERTY_FILENAME);
    }

    public static boolean bootstrap(String propertyFileName)
    {
        try
        {
            appInstance.config = new Config(propertyFileName);
            appInstance.xmlhelper = new XmlHelper(appInstance.config, "xml");
            appInstance.db = new DB(appInstance.config, "postgresdb");
            appInstance.mailer = new Mailer(appInstance.config, "mailer");
            appInstance.environment = new Environment(appInstance.config, "env", "master");
            //appInstance.lucene = new Lucene(appInstance.config, "lucene");
            appInstance.storage = new Storage(appInstance.environment.getStorageDirectory());
            appInstance.objectMapper = new ObjectMapper();
            return true;
        }
        catch (ConfigurationException ce)
        {
            logger.fatal("Failed to load configuration file " + propertyFileName);
            logger.fatal(ce.getMessage(), ce);
        }
        catch (Exception ex)
        {
            logger.fatal("An exception occurred while building dependencies");
            logger.fatal(ex.getMessage(), ex);
        }
        return false;
    }

    public static boolean shutdown() throws IOException
    {
       // if (appInstance.lucene != null) {
            //appInstance.lucene.close();
       // }
        return true;
    }

    public static Config getConfig() {
        return appInstance.config;
    }

    public static DB getDB() {
        return appInstance.db;
    }

    public static Environment getEnvironment() {
        return appInstance.environment;
    }

    public static Storage getStorage() {
        return appInstance.storage;
    }

    public static XmlHelper getXmlHelper()
    {
        return appInstance.xmlhelper;
    }

    public static ObjectMapper getObjectMapper()
    {
        return appInstance.objectMapper;
    }
}
