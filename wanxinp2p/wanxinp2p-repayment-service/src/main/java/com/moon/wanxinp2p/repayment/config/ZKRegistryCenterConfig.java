package com.moon.wanxinp2p.repayment.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * zookeeper 配置类
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-30 15:20
 * @description
 */
@Configuration
public class ZKRegistryCenterConfig {

    // 读取配置 zookeeper 服务器地址
    @Value("${p2p.zookeeper.server}")
    private String ZOOKEEPER_SERVER;

    // 定时任务的名称空间
    @Value("${p2p.job.namespace}")
    private String JOB_NAMESPACE;

    /**
     * zk的配置及创建注册中心
     *
     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter createRegistryCenter() {
        // zk配置
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(ZOOKEEPER_SERVER, JOB_NAMESPACE);
        // 创建注册中心
        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }

}
