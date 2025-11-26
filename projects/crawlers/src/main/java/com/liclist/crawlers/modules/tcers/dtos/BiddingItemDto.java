package com.liclist.crawlers.modules.tcers.dtos;

public record BiddingItemDto(String agencyCode, String biddingNumber,
  String biddingYear, String biddingModality, String itemNumber,
  String itemDescription, double itemQuantity, String itemMetricUnit,
  double itemAmount) {
  public String code() {
    return String
      .format("%s.%s.%s-%s_%d", agencyCode, biddingNumber, biddingYear,
        biddingModality, itemNumber);
  }

  public int amountScale() {
    return 4;
  }

  public int amount() {
    return (int) (itemAmount * Math.pow(10, amountScale()));
  }
}
