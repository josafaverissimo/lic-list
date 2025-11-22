package com.liclist.crawlers;

import com.liclist.crawlers.di.AppComponent;
import com.liclist.crawlers.di.DaggerAppComponent;
import com.liclist.crawlers.modules.commons.Env;

public class App {
  public static void main(String[] args) {
    Env.check();

    AppComponent component = DaggerAppComponent.create();

    component.tceRsCrawler().run();
  }
}
