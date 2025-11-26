package com.liclist.crawlers.modules.tcers.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liclist.crawlers.modules.tcers.dtos.BiddingItemDto;
import com.liclist.crawlers.modules.tcers.enums.BiddingItemCsvColumnEnum;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class BiddingItemProcessor {
  private final Logger logger = LoggerFactory.getLogger(BiddingItemProcessor.class);
  private static final String COMMA_SEPARATOR = ",";
  private static final int MIN_ROW_LENGTH = BiddingItemCsvColumnEnum.getMaxIndex();

  @Inject
  public BiddingItemProcessor() {
  }

  private <T> Optional<T> safeParse(String value, Function<String, T> converter) {
    try {
      return Optional.of(converter.apply(value));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private BiddingItemDto parseToDto(
      List<String> row) throws IllegalArgumentException, NumberFormatException {
    if (row.size() < MIN_ROW_LENGTH)
      throw new IllegalArgumentException(
          "Row size is less than " + MIN_ROW_LENGTH);

    final String rawItemQuantity = row.get(
        BiddingItemCsvColumnEnum.ITEM_QUANTITY.getIndex());

    final String rawItemAmount = row.get(
        BiddingItemCsvColumnEnum.ITEM_AMOUNT.getIndex());

    Optional<Double> itemQuantity = safeParse(
        rawItemQuantity, Double::valueOf);

    Optional<Double> itemAmount = safeParse(
        rawItemAmount, Double::valueOf);

    if (itemQuantity.isEmpty())
      throw new NumberFormatException(
          "item quantity is not a int: " + rawItemQuantity);

    if (itemQuantity.isEmpty())
      throw new NumberFormatException(
          "item amount is not a double: " + rawItemQuantity);

    return new BiddingItemDto(
        row.get(BiddingItemCsvColumnEnum.AGENCY_CODE.getIndex()),
        row.get(BiddingItemCsvColumnEnum.BIDDING_NUMBER.getIndex()),
        row.get(BiddingItemCsvColumnEnum.BIDDING_YEAR.getIndex()),
        row.get(BiddingItemCsvColumnEnum.BIDDING_MODALITY.getIndex()),
        row.get(BiddingItemCsvColumnEnum.ITEM_NUMBER.getIndex()),
        row.get(BiddingItemCsvColumnEnum.ITEM_DESCRIPTION.getIndex()),
        itemQuantity.get(),
        row.get(BiddingItemCsvColumnEnum.ITEM_METRIC_UNIT.getIndex()),
        itemAmount.get());
  }

  public void processCsv(BufferedReader bufferedReader) throws IOException {
    logger.info("Processing bidding item csv");

    String line;

    bufferedReader.readLine();

    while ((line = bufferedReader.readLine()) != null) {
      String[] row = line.split(COMMA_SEPARATOR);

      final BiddingItemDto dto;

      try {
        dto = parseToDto(List.of(row));

      } catch(Exception e) {
        // logger.error(e.getMessage());

        continue;
      }

      System.out.println(dto);

      try {

      } catch (Exception e) {

      }
    }

  }
}
