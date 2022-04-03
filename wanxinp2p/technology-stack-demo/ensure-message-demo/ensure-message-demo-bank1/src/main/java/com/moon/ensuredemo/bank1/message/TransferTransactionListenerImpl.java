package com.moon.ensuredemo.bank1.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moon.ensuredemo.bank1.dao.AccountInfoDao;
import com.moon.ensuredemo.bank1.model.AccountChangeEvent;
import com.moon.ensuredemo.bank1.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQTransactionListener(txProducerGroup = "producer_ensure_transfer")
public class TransferTransactionListenerImpl implements RocketMQLocalTransactionListener {

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private AccountInfoDao accountInfoDao;

    /**
     * 执行本地事务
     *
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        // 1.接收并解析消息
        final JSONObject jsonObject = JSON.parseObject(new String((byte[]) msg.getPayload()));
        AccountChangeEvent accountChangeEvent = JSONObject
                .parseObject(jsonObject.getString("accountChange"), AccountChangeEvent.class);

        try {
            // 2.执行本地事务
            accountInfoService.doUpdateAccountBalance(accountChangeEvent);
            // 3.返回执行结果
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }


    /**
     * 事务回查
     *
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("TransferTransactionListenerImpl 执行事务回查");
        // 1.接收并解析消息
        final JSONObject jsonObject = JSON.parseObject(new String((byte[]) msg.getPayload()));
        AccountChangeEvent accountChangeEvent = JSONObject
                .parseObject(jsonObject.getString("accountChange"), AccountChangeEvent.class);

        // 2.查询de_duplication表
        int isExistTx = accountInfoDao.isExistTx(accountChangeEvent.getTxNo());

        // 3.根据查询结果返回值。（交易记录表存在记录，则说明本地事务成功）
        return isExistTx > 0 ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }
}
