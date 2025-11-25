package com.liclist.crawlers.di;

import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hibernate.SessionFactory;

import com.liclist.crawlers.di.qualifiers.CpuExecutor;
import com.liclist.crawlers.di.qualifiers.IoExecutor;
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
  public HttpClient provideHttpClient() {
    return HttpClient.newHttpClient();
  }

  @Provides
  @Singleton
  @IoExecutor
  public ExecutorService provideVirtualThreadExecutor() {
    return Executors.newFixedThreadPool(10, Thread.ofVirtual().factory());
  }

  @Provides
  @Singleton
  @CpuExecutor
  public ExecutorService provideFixedPoolExecutor() {
    int cores = Runtime.getRuntime().availableProcessors();

    return Executors.newFixedThreadPool(cores);
  }
}
