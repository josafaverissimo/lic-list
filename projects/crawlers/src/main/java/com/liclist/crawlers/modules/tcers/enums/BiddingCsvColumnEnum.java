package com.liclist.crawlers.modules.tcers.enums;

import java.util.Arrays;

public enum BiddingCsvColumnEnum {
    AGENCY_CODE(0),
    BIDDING_NUMBER(2),
    BIDDING_YEAR(3),
    BIDDING_PROCESS_NUMBER(8),
    BIDDING_MODALITY(4),
    BIDDING_DESCRIPTION(34),
    AMOUNT(43);

    private final int index;

    BiddingCsvColumnEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static int getMaxIndex() {
        return Arrays.stream(values())
                .mapToInt(BiddingCsvColumnEnum::getIndex)
                .max()
                .orElse(0);
    }
}
