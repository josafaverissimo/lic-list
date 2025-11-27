package com.liclist.crawlers.modules.tcers.enums;

public enum BiddingFileEnum {
    BIDDING("licitacao.csv"),
    BIDDING_ITEM("item.csv");

    private final String filename;

    BiddingFileEnum(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
