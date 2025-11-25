package com.liclist.crawlers.modules.tcers.repositories;

import java.util.Optional;

import org.hibernate.SessionFactory;

import com.liclist.crawlers.modules.commons.database.seeds.BiddingSourceSeed;
import com.liclist.crawlers.modules.commons.entities.Bidding;
import com.liclist.crawlers.modules.tcers.dtos.BiddingDto;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class BiddingsRepository {
  private final SessionFactory sessionFactory;
  private final BiddingSourceSeed biddingSourceSeed;

  @Inject
  public BiddingsRepository(
      SessionFactory sessionFactory,
      BiddingSourceSeed biddingSourceSeed) {
    this.sessionFactory = sessionFactory;
    this.biddingSourceSeed = biddingSourceSeed;
  }

  public Optional<Bidding> getBiddingByCode(String code) {
    return this.sessionFactory.fromTransaction(session -> {
      final String query = "from Bidding where code = :code";

      Bidding data = session
          .createSelectionQuery(query, Bidding.class)
          .setParameter("code", code)
          .getSingleResultOrNull();

      return Optional.ofNullable(data);
    });
  }

  public Bidding create(BiddingDto biddingDto) {
    var biddingSource = this.biddingSourceSeed.getMapEntities().get("tcers");

    return this.sessionFactory.fromTransaction(session -> {
      var bidding = new Bidding(biddingDto, biddingSource);

      session.persist(bidding);

      return bidding;
    });
  }

  public Bidding getOrCreate(BiddingDto biddingDto) {
    return getBiddingByCode(biddingDto.code())
        .orElseGet(() -> this.create(biddingDto));
  }
}
