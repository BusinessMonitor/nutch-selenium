package org.apache.nutch.protocol.selenium;

// JDK imports

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.metadata.SpellCheckedMetadata;
import org.apache.nutch.net.protocols.Response;
import org.apache.nutch.protocol.ProtocolException;
import org.apache.nutch.protocol.http.api.HttpException;
import org.apache.nutch.storage.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.EOFException;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.URL;

// import org.apache.nutch.crawl.CrawlDatum;

/* Most of this code was borrowed from protocol-htmlunit; which in turn borrowed it from protocol-httpclient */

public class HttpResponse implements Response {

    private Http http;
    private URL url;
    private String orig;
    private String base;
    private byte[] content;
    private int code;
    private Metadata headers = new SpellCheckedMetadata();

    /**
     * The nutch configuration
     */
    private Configuration conf = null;

    public HttpResponse(Http http, URL url, WebPage page, Configuration conf) throws ProtocolException, IOException {

        this.conf = conf;
        this.http = http;
        this.url = url;
        this.orig = url.toString();
        this.base = url.toString();

        FirefoxDriver driver = new FirefoxDriver();
        try {
            driver.get(url.toString());
            // Wait for the page to load, timeout after 3 seconds
            new WebDriverWait(driver, 3);

            String innerHtml = driver.findElement(By.tagName("body")).getAttribute("innerHTML");
            code = 200;
            content = innerHtml.getBytes("UTF-8");
        } finally {
            driver.close();
        }
    }

    public URL getUrl() {
        return url;
    }

    public int getCode() {
        return code;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    /* ------------------------- *
     * <implementation:Response> *
     * ------------------------- */

    public Metadata getHeaders() {
        return headers;
    }

    public byte[] getContent() {
        return content;
    }
}
