package com.liclist.crawlers.di;

import org.hibernate.SessionFactory;

import com.liclist.crawlers.modules.commons.database.Database;

import dagger.Module;
import dagger.Provides;
import jakarta.inject.Singleton;

@Module
public class DatabaseModule {
  @Provides
  @Singleton
  public SessionFactory provideSessionFactory() {
    return Database.getSessionFactory();
  }
}
