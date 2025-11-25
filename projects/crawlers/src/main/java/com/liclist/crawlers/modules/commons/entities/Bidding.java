package com.liclist.crawlers.modules.commons.entities;

import java.time.ZonedDateTime;

import com.liclist.crawlers.modules.tcers.dtos.BiddingDto;

import io.github.thibaultmeyer.cuid.CUID;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "liclist_biddings")
public class Bidding {
  @Id
  private String id;

  @Basic(optional = false)
  private String code;

  private String description;

  private Integer amount;

  @Column(name = "amount_scale")
  private Short amountScale;

  @Column(name = "created_at", insertable = false, updatable = false)
  private ZonedDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "bidding_source_id", nullable = false)
  private BiddingSource biddingSource;

  public Bidding() {
  }

  public Bidding(BiddingDto biddingDto, BiddingSource biddingSource) {
    this.id = CUID.randomCUID2().toString();
    this.code = biddingDto.code();
    this.description = biddingDto.description();
    this.amount = biddingDto.amount();
    this.amountScale = biddingDto.amountScale();
    this.biddingSource = biddingSource;
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

  public Integer getAmount() {
    return amount;
  }

  public Short getAmountScale() {
    return amountScale;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }
}
