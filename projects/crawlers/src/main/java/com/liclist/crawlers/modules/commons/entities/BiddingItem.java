package com.liclist.crawlers.modules.commons.entities;

import com.liclist.crawlers.modules.tcers.dtos.BiddingItemDto;
import io.github.thibaultmeyer.cuid.CUID;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "liclist_biddings_items")
public class BiddingItem {
    @Id
    private String id;

    @Basic(optional = false)
    private String code;

    private String description;

    private double quantity;

    @Column(name = "metric_unit")
    private String metricUnit;

    @Column(name = "unit_amount")
    private int unitAmount;

    @Column(name = "unit_amount_scale")
    private int unitAmountScale;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bidding_id", nullable = false)
    private Bidding bidding;

    public BiddingItem() {}

    public BiddingItem(BiddingItemDto biddingItemDto, Bidding bidding) {
        this.id = CUID.randomCUID2().toString();
        this.code = biddingItemDto.code();
        this.description = biddingItemDto.itemDescription();
        this.quantity = biddingItemDto.itemQuantity();
        this.unitAmount = biddingItemDto.amount();
        this.unitAmountScale = biddingItemDto.amountScale();
        this.bidding = bidding;
        this.metricUnit = biddingItemDto.itemMetricUnit();
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getUnitAmount() {
        return unitAmount;
    }

    public int getUnitAmountScale() {
        return unitAmountScale;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Bidding getBidding() {
        return bidding;
    }
}
