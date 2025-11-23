package com.liclist.crawlers.di;

import java.net.http.HttpClient;

import org.hibernate.SessionFactory;

import com.liclist.crawlers.modules.commons.database.Database;

import dagger.Module;
import dagger.Provides;
import jakarta.inject.Singleton;

@Module
public class ProviderModule {
  @Provides
  @Singleton
  public SessionFactory provideSessionFactory() {
    return Database.getSessionFactory();
  }

  @Provides
  @Singleton
  public HttpClient proviseHttpClient() {
    return HttpClient.newHttpClient();
  }
}
