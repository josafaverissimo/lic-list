package com.liclist.crawlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.liclist.crawlers.di.AppComponent;
import com.liclist.crawlers.di.DaggerAppComponent;
import dagger.internal.DaggerGenerated;

public class App {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(App.class);

    AppComponent component = DaggerAppComponent.create();

    logger.info("Crawlers say: Hello world!");
  }
}
