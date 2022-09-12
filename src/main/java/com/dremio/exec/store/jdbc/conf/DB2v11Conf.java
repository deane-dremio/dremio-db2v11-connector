package com.dremio.exec.store.jdbc.conf;

import static com.google.common.base.Preconditions.checkNotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.dremio.exec.catalog.conf.DisplayMetadata;
import com.dremio.exec.catalog.conf.NotMetadataImpacting;
import com.dremio.exec.catalog.conf.Secret;
import com.dremio.exec.catalog.conf.SourceType;
import com.dremio.options.OptionManager;
import com.dremio.security.CredentialsService;
import com.dremio.exec.store.jdbc.CloseableDataSource;
import com.dremio.exec.store.jdbc.DataSources;
import com.dremio.exec.store.jdbc.JdbcPluginConfig;
import com.dremio.exec.store.jdbc.dialect.arp.ArpDialect;
import com.dremio.exec.store.jdbc.dialect.DB2Dialect;
import com.google.common.annotations.VisibleForTesting;
import java.util.Properties;
import io.protostuff.Tag;

/**
 * Configuration for IBM DB2 sources.
 */
@SourceType(value = "IBMDB2V11ARP", label = "IBM DB2 v11", uiConfig = "db2-layout.json", externalQuerySupported = true)
public class DB2v11Conf extends AbstractArpConf<DB2v11Conf> {
  private static final String ARP_FILENAME = "arp/implementation/db2v11-arp.yaml";
  private static final ArpDialect ARP_DIALECT =
      AbstractArpConf.loadArpFile(ARP_FILENAME, (DB2Dialect::new));
  private static final String DRIVER = "com.ibm.db2.jcc.DB2Driver";
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DB2v11Conf.class);
  
  @NotBlank
  @Tag(1)
  @DisplayMetadata(label = "Database Server")
  public String host;

  @NotBlank
  @Tag(2)
  @DisplayMetadata(label = "Database Port")
  public String port;

  @NotBlank
  @Tag(3)
  @DisplayMetadata(label = "Database")
  public String database;

  @NotBlank
  @Tag(4)
  @DisplayMetadata(label = "Username")
  public String username;

  @NotBlank
  @Tag(5)
  @Secret
  @DisplayMetadata(label = "Password")
  public String password;
  
  @Tag(6)
  @DisplayMetadata(label = "Record fetch size")
  @NotMetadataImpacting
  public int fetchSize = 2000;

  @Tag(7)
  @DisplayMetadata(label = "Query timeout (s)")
  @NotMetadataImpacting
  public int queryTimeout = 10;


  @Tag(8)
  @DisplayMetadata(label = "Maximum idle connections")
  @NotMetadataImpacting
  public int maxIdleConns = 8;

  @Tag(9)
  @DisplayMetadata(label = "Connection idle time (s)")
  @NotMetadataImpacting
  public int idleTimeSec = 60;


  @VisibleForTesting
  public String toJdbcConnectionString() {
    final String database = checkNotNull(this.database, "Missing database.");
	final String host = checkNotNull(this.host, "Missing host.");
    final String port = checkNotNull(this.port, "Missing port.");
    final String username = checkNotNull(this.username, "Missing username.");
    final String password = checkNotNull(this.password, "Missing password.");
    return String.format("jdbc:db2://%s:%s/%s", host, port, database);
  }

  @Override
  @VisibleForTesting
  public JdbcPluginConfig buildPluginConfig(JdbcPluginConfig.Builder configBuilder, CredentialsService credentialsService, OptionManager optionManager) {
         return configBuilder.withDialect(getDialect())
		.withFetchSize(fetchSize)
        .withDatasourceFactory(this::newDataSource)
        .clearHiddenSchemas()
        //.addHiddenSchema("SYSTEM")
        .build();
  }

  private CloseableDataSource newDataSource() {
	final Properties properties = new Properties();
    return DataSources.newGenericConnectionPoolDataSource(DRIVER,
      toJdbcConnectionString(), username, password, properties, DataSources.CommitMode.DRIVER_SPECIFIED_COMMIT_MODE, maxIdleConns, idleTimeSec);
  }

  @Override
  public ArpDialect getDialect() {
    return ARP_DIALECT;
  }

  @VisibleForTesting
  public static ArpDialect getDialectSingleton() {
    return ARP_DIALECT;
  }
}
