package com.liclist.crawlers.modules.tcers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.liclist.crawlers.modules.commons.interfaces.Crawler;
import jakarta.inject.Inject;

public class TceRsCrawler implements Crawler {
  private Logger logger = LoggerFactory.getLogger(TceRsCrawler.class);

  @Inject
  public TceRsCrawler() {}

  public void run() {
    logger.info("Running Tce/RS Crawler");
  }
}
