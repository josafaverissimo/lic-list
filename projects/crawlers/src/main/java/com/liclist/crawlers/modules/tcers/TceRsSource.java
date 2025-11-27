package com.liclist.crawlers.modules.tcers;

import com.liclist.crawlers.modules.commons.Env;
import com.liclist.crawlers.modules.commons.enums.EnvEnum;
import com.liclist.crawlers.modules.tcers.enums.BiddingFileEnum;
import com.liclist.crawlers.modules.tcers.processors.BiddingItemProcessor;
import com.liclist.crawlers.modules.tcers.processors.BiddingProcessor;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TceRsSource {
    private final Logger logger = LoggerFactory.getLogger(TceRsSource.class);

    private final HttpClient httpClient;

    private final BiddingProcessor biddingProcessor;

    private final BiddingItemProcessor biddingItemProcessor;

    @Inject
    public TceRsSource(
            HttpClient httpClient, BiddingProcessor biddingProcessor, BiddingItemProcessor biddingItemProcessor) {
        this.httpClient = httpClient;
        this.biddingProcessor = biddingProcessor;
        this.biddingItemProcessor = biddingItemProcessor;
    }

    private ZipInputStream fetchFile() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Env.get(EnvEnum.TCERS_FILE_URL)))
                .GET()
                .build();

        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        return new ZipInputStream(new ByteArrayInputStream(response.body()));
    }

    public void storeData() {
        logger.info("Storing tce rs source data");

        try (var zipInputStream = this.fetchFile()) {
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                BufferedReader bufferedReader;
                var inputStream = new InputStreamReader(zipInputStream);

                if (zipEntry.getName().equals(BiddingFileEnum.BIDDING.getFilename())) {
                    bufferedReader = new BufferedReader(inputStream);

                    // this.biddingProcessor.processCsv(bufferedReader);

                    continue;
                }

                if (zipEntry.getName().equals(BiddingFileEnum.BIDDING_ITEM.getFilename())) {
                    bufferedReader = new BufferedReader(inputStream);

                    this.biddingItemProcessor.processCsv(bufferedReader);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
