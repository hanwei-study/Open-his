package com.hanwei.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hanwei
 * @ClassName IdGerneratorSnowflake
 * @date 2020/9/24
 */
public class IdGeneratorSnowflake {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    private static long workId = 0;
    private static long datacenterId = 1;
    private static Snowflake snowflake;

    static {
        try {
            log.info("当前机器的工作ID为:" + workId);
            snowflake= IdUtil.createSnowflake(workId, datacenterId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("当前机器的workId获取失败", e);

        }
    }

    /**
     * 生成ID
     */
    public static synchronized Long snowflakeId() {
        return snowflake.nextId();
    }

    /**
     * 根据前缀前成ID
     */
    public static String generatorIdWithProfix(String profix) {
        return profix + snowflakeId();
    }

    public static void main(String[] args) {
        System.out.println(snowflakeId());
    }
}
