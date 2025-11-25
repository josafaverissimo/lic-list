package com.liclist.crawlers.modules.commons.database.seeds;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liclist.crawlers.modules.commons.entities.BiddingSource;
import com.liclist.crawlers.modules.commons.interfaces.Seed;
import com.liclist.crawlers.modules.commons.repositories.BiddingsSourcesRepository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class BiddingSourceSeed implements Seed<BiddingSource> {
  private final BiddingsSourcesRepository biddingsSourceRepository;

  private final Logger logger = LoggerFactory.getLogger(BiddingSourceSeed.class);

  @Inject
  public BiddingSourceSeed(BiddingsSourcesRepository biddingsSourceRepository) {
    this.biddingsSourceRepository = biddingsSourceRepository;
  }

  public void run() {
    logger.info("Seeding biddings sources");

    for (BiddingSource biddingSource : this.getEntities()) {
      this.biddingsSourceRepository.getOrCreate(biddingSource);
    }
  }

  public List<BiddingSource> getEntities() {
    return List.of(new BiddingSource("xx1fdefqwpfx4t1cyoflvc8n", "tcers"));
  }

  public Map<String, BiddingSource> getMapEntities() {
    return getEntities()
        .stream()
        .collect(Collectors.toMap(BiddingSource::getName, Function.identity()));
  }
}
