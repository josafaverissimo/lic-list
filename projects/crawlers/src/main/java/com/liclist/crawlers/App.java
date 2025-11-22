package com.liclist.crawlers;

import com.liclist.crawlers.di.AppComponent;
import com.liclist.crawlers.di.DaggerAppComponent;

public class App {
  public static void main(String[] args) {
    AppComponent component = DaggerAppComponent.create();

    component.tceRsCrawler().run();
  }
}
