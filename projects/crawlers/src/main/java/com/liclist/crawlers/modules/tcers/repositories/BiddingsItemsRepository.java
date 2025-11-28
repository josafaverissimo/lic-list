package com.liclist.crawlers.modules.tcers.repositories;

import com.liclist.crawlers.modules.commons.entities.Bidding;
import com.liclist.crawlers.modules.commons.entities.BiddingItem;
import com.liclist.crawlers.modules.tcers.dtos.BiddingItemDto;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class BiddingsItemsRepository {
    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(BiddingsItemsRepository.class);

    @Inject
    public BiddingsItemsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public BiddingItem create(BiddingItemDto biddingItemDto, Bidding bidding) {
        return this.sessionFactory.fromTransaction(session -> {
            var biddingItem = new BiddingItem(biddingItemDto, bidding);

            session.persist(biddingItem);

            logger.info(String.format("Bidding item #(id=%s)", biddingItem.getId()));

            return biddingItem;
        });
    }
}
