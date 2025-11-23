package com.liclist.crawlers.modules.tcers.dtos;

public record BiddingDto(
    String code,
    String description,
    Integer amount,
    short amountScale) {
}
