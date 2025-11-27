package com.liclist.crawlers;

import com.liclist.crawlers.di.AppComponent;
import com.liclist.crawlers.di.DaggerAppComponent;
import com.liclist.crawlers.modules.commons.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(App.class);

        logger.info("Crawlers app initiated");

        Env.check();

        AppComponent component = DaggerAppComponent.create();

        component.seeds().run();
        component.crawlers().run();
    }
}
