package com.liclist.crawlers.modules.tcers.enums;

public enum BiddingCsvColumnEnum {
  AGENCY_CODE(0),
  BIDDING_ID(2),
  BIDDING_YEAR(3),
  BIDDING_DESCRIPTION(34),
  AMOUNT(43);

  private final int index;

  BiddingCsvColumnEnum(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}
