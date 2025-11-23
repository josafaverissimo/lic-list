package com.liclist.crawlers.modules.commons.database.seeds;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liclist.crawlers.modules.commons.interfaces.Seed;

import jakarta.inject.Inject;

public class Seeds implements Runnable {
  private final List<Seed<? extends Object>> seeds = new ArrayList<>();

  private final Logger logger = LoggerFactory.getLogger(Seeds.class);

  @Inject
  public Seeds(BiddingSourceSeed biddingSourceSeed) {
    this.seeds.add(biddingSourceSeed);
  }

  public void run() {
    logger.info("Running seeds");

    for(Seed<? extends Object> seed : this.seeds) {
      seed.run();
    }
  }
}
