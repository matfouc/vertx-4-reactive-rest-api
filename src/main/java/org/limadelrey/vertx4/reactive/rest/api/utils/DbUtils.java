package org.limadelrey.vertx4.reactive.rest.api.utils;

import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import java.util.Properties;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;

public class DbUtils {

    private static final String HOST_CONFIG = "datasource.host";
    private static final String PORT_CONFIG = "datasource.port";
    private static final String DATABASE_CONFIG = "datasource.database";
    private static final String USERNAME_CONFIG = "datasource.username";
    private static final String PASSWORD_CONFIG = "datasource.password";

    private DbUtils() {

    }

    /**
     * Build DB client that is used to manage a pool of connections
     *
     * @param vertx Vertx context
     * @return PostgreSQL pool
     */
    public static MySQLPool buildDbClient(Vertx vertx) {
        final Properties properties = ConfigUtils.getInstance().getProperties();

        final MySQLConnectOptions connectOptions = new MySQLConnectOptions ()
                .setPort(Integer.parseInt(properties.getProperty(PORT_CONFIG)))
                .setHost(properties.getProperty(HOST_CONFIG))
                .setDatabase(properties.getProperty(DATABASE_CONFIG))
                .setUser(properties.getProperty(USERNAME_CONFIG))
                .setPassword(properties.getProperty(PASSWORD_CONFIG));

        final PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        return MySQLPool.pool(vertx, connectOptions, poolOptions);
    }

    /**
     * Build Flyway configuration that is used to run migrations
     *
     * @return Flyway configuration
     */
    public static Configuration buildMigrationsConfiguration() {
        final Properties properties = ConfigUtils.getInstance().getProperties();

        final String url = "jdbc:mariadb://" + properties.getProperty(HOST_CONFIG) + ":" + properties.getProperty(PORT_CONFIG) + "/" + properties.getProperty(DATABASE_CONFIG);

        return new FluentConfiguration().dataSource(url, properties.getProperty(USERNAME_CONFIG), properties.getProperty(PASSWORD_CONFIG));
    }

}
