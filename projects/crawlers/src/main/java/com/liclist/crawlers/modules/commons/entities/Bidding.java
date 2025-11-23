package com.liclist.crawlers.modules.commons.entities;

import java.time.ZonedDateTime;

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

  private int amount;

  @Column(name = "amount_scale")
  private short amountScale;

  @Column(name = "created_at", insertable = false, updatable = false)
  private ZonedDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "bidding_source_id", nullable = false)
  private BiddingSource biddingSource;

  public Bidding() {
  }

  public Bidding(
      String code,
      String description,
      int amount,
      short amountScale,
      BiddingSource biddingSource) {
    this.id = CUID.randomCUID2().toString();
    this.description = description;
    this.amount = amount;
    this.amountScale = amountScale;
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
