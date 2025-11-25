package com.liclist.crawlers.modules.tcers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.primitives.Doubles;
import com.liclist.crawlers.modules.commons.Env;
import com.liclist.crawlers.modules.commons.enums.EnvEnum;
import com.liclist.crawlers.modules.tcers.dtos.BiddingDto;
import com.liclist.crawlers.modules.tcers.enums.BiddingCsvColumnEnum;
import com.liclist.crawlers.modules.tcers.enums.BiddingFileEnum;
import com.liclist.crawlers.modules.tcers.repositories.BiddingsRepository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TceRsSource {
  private final Logger logger = LoggerFactory.getLogger(TceRsSource.class);

  private final String COMMA_DELIMITER = ",";

  private final HttpClient httpClient;

  private final BiddingsRepository biddingsRepository;

  @Inject
  public TceRsSource(HttpClient httpClient,
    BiddingsRepository biddingsRepository) {
    this.httpClient = httpClient;
    this.biddingsRepository = biddingsRepository;
  }

  private ZipInputStream fetchFile() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(Env.get(EnvEnum.TCERS_FILE_URL)))
      .GET()
      .build();

    HttpResponse<byte[]> response =
      httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

    return new ZipInputStream(new ByteArrayInputStream(response.body()));
  }

  private BiddingDto parseRowToBiddingDto(List<String> row)
    throws IllegalArgumentException {

    final int minLength = BiddingCsvColumnEnum.getMaxIndex();

    if (row.size() < minLength)
      throw new IllegalArgumentException(
        "Got row with size less than " + minLength);

    final String agencyCode =
      Iterables.get(row, BiddingCsvColumnEnum.AGENCY_CODE.getIndex(), null);

    final String biddingModality = Iterables
      .get(row, BiddingCsvColumnEnum.BIDDING_MODALITY.getIndex(), null);

    final String biddingNumber =
       Iterables.get(row, BiddingCsvColumnEnum.BIDDING_NUMBER.getIndex(), null);

    final String biddingYear =
      Iterables.get(row, BiddingCsvColumnEnum.BIDDING_YEAR.getIndex(), null);

    final String biddingDescription = Iterables
      .get(row, BiddingCsvColumnEnum.BIDDING_DESCRIPTION.getIndex(), null);

    final String biddingAmount =
      Iterables.get(row, BiddingCsvColumnEnum.AMOUNT.getIndex(), null);

    if (agencyCode == null || biddingNumber == null || biddingYear == null
      || biddingDescription == null) {
      throw new IllegalArgumentException(String
        .format(
          "There's some null value: {agencyCode=%s, biddingId=%s, "
            + "biddingYear=%s, biddingDescription=%s}",
          agencyCode, biddingNumber, biddingYear, biddingDescription));
    }

    final String code = String
      .format("%s.%s.%s-%s", agencyCode, biddingNumber, biddingYear,
        biddingModality);

    final short amountScale = 4;

    Integer amount = null;

    if (biddingAmount.length() != 0) {
      Double parsedBiddingAmount = Doubles.tryParse(biddingAmount.strip());

      if (parsedBiddingAmount != null)
        amount = (int) (parsedBiddingAmount * Math.pow(10, amountScale));
    }

    return new BiddingDto(code, biddingDescription.strip(), amount,
      amountScale);
  }

  private void storeBiddingData(BufferedReader bufferedReader)
    throws IOException {
    String line;

    bufferedReader.readLine();

    while ((line = bufferedReader.readLine()) != null) {
      String[] row = line.split(COMMA_DELIMITER);

      try {
        BiddingDto biddingDto = parseRowToBiddingDto(List.of(row));

        try (var executor =
          Executors.newFixedThreadPool(50, Thread.ofVirtual().factory())) {

          executor.submit(() -> {
            try {
              this.biddingsRepository.getOrCreate(biddingDto);
            } catch (Exception e) {
              e.printStackTrace();
            }
          });

        } catch (Exception e) {
          e.printStackTrace();
        }

      } catch (IllegalArgumentException e) {
        logger.warn(String.format("Failed to parse row: %s", e.getMessage()));
      }
    }
  }

  public void storeData() {
    logger.info("Storing tce rs source data");

    try (var zipInputStream = this.fetchFile()) {
      ZipEntry zipEntry;

      while ((zipEntry = zipInputStream.getNextEntry()) != null) {
        BufferedReader bufferedReader;

        if (zipEntry.getName().equals(BiddingFileEnum.BIDDING.getFilename())) {
          bufferedReader =
            new BufferedReader(new InputStreamReader(zipInputStream));

          this.storeBiddingData(bufferedReader);
        }
      }

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
