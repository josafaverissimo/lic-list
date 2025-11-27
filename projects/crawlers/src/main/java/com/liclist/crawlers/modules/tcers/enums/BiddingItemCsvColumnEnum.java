package com.liclist.crawlers.modules.tcers.enums;

import java.util.Arrays;

public enum BiddingItemCsvColumnEnum {
    AGENCY_CODE(0),
    BIDDING_NUMBER(1),
    BIDDING_YEAR(2),
    BIDDING_MODALITY(3),
    ITEM_NUMBER(5),
    ITEM_DESCRIPTION(7),
    ITEM_QUANTITY(8),
    ITEM_METRIC_UNIT(9),
    ITEM_AMOUNT(10);

    private final int index;

    BiddingItemCsvColumnEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static int getMaxIndex() {
        return Arrays.stream(values())
                .mapToInt(BiddingItemCsvColumnEnum::getIndex)
                .max()
                .orElse(0);
    }
}
