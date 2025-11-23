package com.liclist.crawlers.modules.commons.database;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import com.liclist.crawlers.App;
import com.liclist.crawlers.modules.commons.Env;
import com.liclist.crawlers.modules.commons.enums.EnvEnum;
import jakarta.persistence.PersistenceConfiguration;

public class Database {
  private static final SessionFactory sessionFactory = buildSessionFactory();

  private static SessionFactory buildSessionFactory() {

    return new HibernatePersistenceConfiguration("liclist", App.class)
      .property(PersistenceConfiguration.JDBC_URL, Env.get(EnvEnum.DB_URL))
      .property(PersistenceConfiguration.JDBC_USER, Env.get(EnvEnum.DB_USER))
      .property(PersistenceConfiguration.JDBC_PASSWORD, Env.get(EnvEnum.DB_PASSWORD))
      .createEntityManagerFactory();
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}
