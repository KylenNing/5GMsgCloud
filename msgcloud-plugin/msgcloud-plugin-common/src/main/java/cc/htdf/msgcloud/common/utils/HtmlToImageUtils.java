package cc.htdf.msgcloud.common.utils;


import cn.hutool.core.io.FileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ningyq
 * @Date: 2020/9/2
 * @Description: TODO
 */
@Slf4j
public class HtmlToImageUtils {

    public static InputStream htmlToImageByUrl(String url,String chromedriverPath) throws AWTException, InterruptedException, IOException {

        chromedriverPath = "/usr/local/bin/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromedriverPath);
        Map<String, String> mobileEmulation = new HashMap<String, String>();
        mobileEmulation.put("deviceName", "iPhone X");
        Map<String, Object> chromeOptions = new HashMap<String, Object>();
        chromeOptions.put("mobileEmulation", mobileEmulation);
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        // ChromeOptions
        ChromeOptions chromeOption = new ChromeOptions();
        // 设置后台静默模式启动浏览器
        //chromeOption.addArguments("--headless");
        chromeOption.addArguments("--no-sandbox");
        chromeOption.addArguments("--disable-dev-shm-usage");
        chromeOption.addArguments("--hide-scrollbars");
        chromeOption.addArguments("--auto-open-devtools-for-tabs");
        //chromeOption.addArguments("--widows-size=375,812");
        capabilities.merge(chromeOption);
        TreeMap map = (TreeMap) capabilities.getCapability(ChromeOptions.CAPABILITY);
        //map.put("mobileEmulation", mobileEmulation);
        WebDriver driver = new ChromeDriver(capabilities);
        driver.manage().window().maximize();
        driver.manage().window().setSize(new Dimension(375,1050));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        //url = "https://www.airbnb.cn/?_set_bev_on_new_domain=1599211124_5LPZuUxGEzRUXRRA";
        url = "http://182.92.220.116/template/template_1/index.html?code=210200&id=28";
        driver.get(url);
        Thread.sleep(3000);
        // 调用截图方法
        File srcfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String picName = Math.random()+".png";
        FileUtil.copyFile(srcfile,new File("/Users/kylen/Desktop/testData/png/"+picName));
        log.info("图片生成成功！");
        //driver.quit();
        InputStream is = FileUtil.getInputStream(srcfile);
        return is;
    }

}