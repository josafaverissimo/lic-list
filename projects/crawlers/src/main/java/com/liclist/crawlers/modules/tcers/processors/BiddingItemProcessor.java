package com.liclist.crawlers.modules.tcers.processors;

import com.liclist.crawlers.modules.commons.entities.Bidding;
import com.liclist.crawlers.modules.tcers.dtos.BiddingItemDto;
import com.liclist.crawlers.modules.tcers.enums.BiddingItemCsvColumnEnum;
import com.liclist.crawlers.modules.tcers.repositories.BiddingsItemsRepository;
import com.liclist.crawlers.modules.tcers.repositories.BiddingsRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class BiddingItemProcessor {
    private static final String COMMA_SEPARATOR = ",";
    private static final int MIN_ROW_LENGTH = BiddingItemCsvColumnEnum.getMaxIndex();

    private final Logger logger = LoggerFactory.getLogger(BiddingItemProcessor.class);
    private final BiddingsItemsRepository biddingsItemsRepository;
    private final BiddingsRepository biddingsRepository;

    @Inject
    public BiddingItemProcessor(
            BiddingsItemsRepository biddingsItemsRepository, BiddingsRepository biddingsRepository) {
        this.biddingsItemsRepository = biddingsItemsRepository;
        this.biddingsRepository = biddingsRepository;
    }

    private <T> Optional<T> safeParse(String value, Function<String, T> converter) {
        try {
            return Optional.of(converter.apply(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private BiddingItemDto parseToDto(List<String> row) throws IllegalArgumentException, NumberFormatException {
        final String rawItemQuantity = row.get(BiddingItemCsvColumnEnum.ITEM_QUANTITY.getIndex());

        final String rawItemAmount = row.get(BiddingItemCsvColumnEnum.ITEM_AMOUNT.getIndex());

        Optional<Double> itemQuantity = safeParse(rawItemQuantity, Double::valueOf);

        Optional<Double> itemAmount = safeParse(rawItemAmount, Double::valueOf);

        if (itemQuantity.isEmpty()) throw new NumberFormatException("item quantity is not a int: " + rawItemQuantity);

        if (itemQuantity.isEmpty()) throw new NumberFormatException("item amount is not a double: " + rawItemQuantity);

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

            if (row.length < MIN_ROW_LENGTH)
                throw new IllegalArgumentException("Row size is less than " + MIN_ROW_LENGTH);

            try {
                BiddingItemDto dto = parseToDto(List.of(row));

                String code = String.format(
                        "%s.%s.%s-%s", dto.agencyCode(), dto.biddingNumber(), dto.biddingYear(), dto.biddingModality());

                Optional<Bidding> bidding = biddingsRepository.getBiddingByCode(code);

                if (bidding.isEmpty()) {
                    continue;
                }

                try {
                    if (dto.itemMetricUnit() == null) continue;

                    try (var executor =
                            Executors.newFixedThreadPool(50, Thread.ofVirtual().factory())) {

                        executor.submit(() -> {
                            try {
                                biddingsItemsRepository.create(dto, bidding.get());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                continue;
            }
        }
    }
}
