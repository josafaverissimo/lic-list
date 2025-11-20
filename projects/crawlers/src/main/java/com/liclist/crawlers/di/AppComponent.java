package com.liclist.crawlers.di;

import com.liclist.crawlers.modules.tcers.TceRsCrawler;
import dagger.Component;
import jakarta.inject.Singleton;

@Singleton
@Component
public interface AppComponent {
  TceRsCrawler tceRsCrawler();
}
