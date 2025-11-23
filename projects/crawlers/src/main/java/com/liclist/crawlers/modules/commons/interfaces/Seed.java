package com.liclist.crawlers.modules.commons.interfaces;

import java.util.List;

public interface Seed<T> extends Runnable {
  public List<T> getEntities();
}
