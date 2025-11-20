package com.liclist.api;

import io.javalin.Javalin;

public class App {
  public static void main(String[] args) {
    Javalin.create(config -> {
      config.useVirtualThreads = true;
    }).get("/", ctx -> ctx.result("Hello world")).start(8080);
  }
}
