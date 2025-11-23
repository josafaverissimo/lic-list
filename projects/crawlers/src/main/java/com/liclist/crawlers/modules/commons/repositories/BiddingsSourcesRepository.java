package com.liclist.crawlers.modules.commons.repositories;

import java.util.Optional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liclist.crawlers.modules.commons.entities.BiddingSource;

import jakarta.inject.Inject;

public class BiddingsSourcesRepository {
  private final SessionFactory sessionFactory;
  private final Logger logger = LoggerFactory.getLogger(BiddingsSourcesRepository.class);

  @Inject
  public BiddingsSourcesRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public Optional<BiddingSource> getByName(String name) {
    return sessionFactory.fromTransaction(session -> {
      final String query = "from BiddingSource where name = :name";

      BiddingSource data = session
          .createSelectionQuery(query, BiddingSource.class)
          .setParameter("name", name)
          .getSingleResultOrNull();

      return Optional.ofNullable(data);
    });
  }

  public BiddingSource create(BiddingSource biddingSource) {
    return sessionFactory.fromTransaction(session -> {
      session.persist(biddingSource);

      logger.info(
          String.format(
              "Bidding source #(name=%s) has been created",
              biddingSource.getName()));

      return biddingSource;
    });
  }

  public BiddingSource getOrCreate(BiddingSource biddingSource) {
    return getByName(biddingSource.getName()).orElseGet(() -> this.create(biddingSource));
  }
}
