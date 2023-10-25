package com.wth.api.mapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wth.api.model.entity.InterfaceInfo;
import com.wth.api.service.InterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
/**
 *
 */
@SpringBootTest
public class InsertDataTest {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     *  插入假数据
     */
    @Test
    public void doInsert() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final int INSERT_NUM = 1000;
        List<InterfaceInfo> list = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            InterfaceInfo interfaceInfo = new InterfaceInfo();
            interfaceInfo.setDescription("描述" + i);
            interfaceInfo.setUrl("www.abc "+ i + ".com");
            interfaceInfo.setRequestHead("RequestHead" + i);
            interfaceInfo.setResponseHead("setResponseHead" + i);
            interfaceInfo.setStatus(0);
            interfaceInfo.setCreateId(0L + i);
            interfaceInfo.setCreateTime(new Date());
            interfaceInfo.setUpdateTime(new Date());
            interfaceInfo.setIsDeleted(0);
            list.add(interfaceInfo);
        }
        interfaceInfoService.saveBatch(list,1000);
        stopWatch.stop();
        System.out.println("花费时间为： " + stopWatch.getTotalTimeMillis());
    }

}
