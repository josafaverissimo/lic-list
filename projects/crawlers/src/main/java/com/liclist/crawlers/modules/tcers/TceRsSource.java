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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.primitives.Doubles;
import com.liclist.crawlers.modules.commons.Env;
import com.liclist.crawlers.modules.commons.entities.Bidding;
import com.liclist.crawlers.modules.commons.entities.BiddingSource;
import com.liclist.crawlers.modules.commons.enums.EnvEnum;
import com.liclist.crawlers.modules.tcers.dtos.BiddingDto;
import com.liclist.crawlers.modules.tcers.enums.BiddingCsvColumnEnum;

import jakarta.inject.Inject;

public class TceRsSource {
  private final Logger logger = LoggerFactory.getLogger(TceRsSource.class);

  private final String COMMA_DELIMITER = ",";

  private HttpClient httpClient;

  private SessionFactory sessionFactory;

  @Inject
  public TceRsSource(HttpClient httpClient, SessionFactory sessionFactory) {
    this.httpClient = httpClient;
    this.sessionFactory = sessionFactory;
  }

  private ZipInputStream fetchFile() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(Env.get(EnvEnum.TCERS_FILE_URL)))
        .GET()
        .build();

    HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

    return new ZipInputStream(new ByteArrayInputStream(response.body()));
  }

  private BiddingDto parseRowToBiddingDto(List<String> row) throws IllegalArgumentException {
    final String agencyCode = Iterables.get(row, BiddingCsvColumnEnum.AGENCY_CODE.getIndex(), null);

    final String biddingId = Iterables.get(row, BiddingCsvColumnEnum.BIDDING_ID.getIndex(), null);

    final String biddingYear = Iterables.get(row, BiddingCsvColumnEnum.BIDDING_YEAR.getIndex(), null);

    final String biddingDescription = Iterables
        .get(row, BiddingCsvColumnEnum.BIDDING_DESCRIPTION.getIndex(), null);

    final String biddingAmount = Iterables
        .get(row, BiddingCsvColumnEnum.AMOUNT.getIndex(), null);

    if (agencyCode == null || biddingId == null || biddingYear == null || biddingDescription == null) {
      throw new IllegalArgumentException(
          String.format(
              "There's some null value: {agencyCode=%s, biddingId=%s, biddingYear=%s, biddingDescription=%s}",
              agencyCode, biddingId, biddingYear, biddingDescription));
    }

    final String code = String.format("%s.%s.%s", agencyCode, biddingId, biddingYear);
    final short amountScale = 4;

    Integer amount = null;

    if (biddingAmount.length() != 0) {
      Double parsedBiddingAmount = Doubles.tryParse(biddingAmount.strip());

      if (parsedBiddingAmount != null)
        amount = (int) (parsedBiddingAmount * Math.pow(10, amountScale));
    }

    return new BiddingDto(code, biddingDescription.strip(), amount, amountScale);
  }

  private void storeBiddingData(BufferedReader bufferedReader) throws IOException {
    String line;

    bufferedReader.readLine();

    while ((line = bufferedReader.readLine()) != null) {
      String[] row = line.split(COMMA_DELIMITER);

      if (row.length < 43)
        continue;

      BiddingDto biddingDto;

      try {
        biddingDto = parseRowToBiddingDto(List.of(row));

        sessionFactory.inTransaction(session -> {
          var biddingSource = session.getReference(BiddingSource.class, "xx1fdefqwpfx4t1cyoflvc8n");

          session.persist(new Bidding(biddingDto, biddingSource));
        });

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

        if (zipEntry.getName().equals("licitacao.csv")) {
          bufferedReader = new BufferedReader(new InputStreamReader(zipInputStream));

          this.storeBiddingData(bufferedReader);
        }
      }

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
