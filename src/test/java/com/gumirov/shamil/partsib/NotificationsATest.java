package com.gumirov.shamil.partsib;

import com.gumirov.shamil.partsib.configuration.endpoints.PricehookIdTaggingRule;
import org.junit.Test;

import javax.activation.DataHandler;
import java.util.*;

/**
 * NullPointerException in log is expected for this test.
 *
 * @author: Shamil@Gumirov.com
 * Copyright (c) 2018 by Shamil Gumirov.
 */
public class NotificationsATest extends AbstractMailAutomationTest {

  @Test
  public void test() throws Exception {
    Map<String, DataHandler> attachments = Collections.singletonMap("a.csv",
        new DataHandler("hi there", "text/plain"));

    //send 2 messages, expect 1 notification
    launch("acceptedmail", "taglogger",
        Arrays.asList("982.0.lamps", "982.0.lamps"),
        null, 2, "direct:emailreceived",
        new EmailMessage("Price", "office@dinamikasveta.ru", attachments),
        new EmailMessage("Price", "office@dinamikasveta.ru", attachments)
    );
  }

  @Override
  public int getExpectedNotificationCount() {
    return 1;
  }

  @Override
  public List<PricehookIdTaggingRule> getTagRules() {
    return loadTagsFile("tagrules.json");
  }
}
