package com.haiyisec.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.zen.frame.global.ErrorConfig;
import org.zen.frame.vendor.lic.event.LicenseExpiredEvent;

/**
 * Created by Administrator on 2020/12/2.
 */
@Component
@Slf4j
public class AppEventHandler {

    @EventListener
    public void event(LicenseExpiredEvent event){
        log.error(ErrorConfig.安全_授权已过期_Code+":"+ErrorConfig.安全_授权已过期_Msg);
        System.exit(0);
    }

}
