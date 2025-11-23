package com.liclist.crawlers.di;

import com.liclist.crawlers.modules.commons.Crawlers;
import com.liclist.crawlers.modules.commons.database.seeds.Seeds;

import dagger.Component;
import jakarta.inject.Singleton;

@Singleton
@Component(modules = { DatabaseModule.class })
public interface AppComponent {
  Seeds seeds();
  Crawlers crawlers();
}
