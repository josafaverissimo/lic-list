package com.liclist.crawlers.modules.commons;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liclist.crawlers.modules.commons.interfaces.Crawler;
import com.liclist.crawlers.modules.tcers.TceRsCrawler;

import jakarta.inject.Inject;

public class Crawlers implements Runnable {
  private final List<Crawler> crawlers = new ArrayList<>();

  private final Logger logger = LoggerFactory.getLogger(Crawlers.class);

  @Inject
  public Crawlers(TceRsCrawler tceRsCrawler) {
    this.crawlers.add(tceRsCrawler);
  }

  public void run() {
    logger.info("Running crawlers");

    for(Crawler crawler : this.crawlers) {
      crawler.run();
    }
  }
}
