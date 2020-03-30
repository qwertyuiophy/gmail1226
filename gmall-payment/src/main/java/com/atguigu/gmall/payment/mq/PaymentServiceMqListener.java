package com.atguigu.gmall.payment.mq;

import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.PaymentInfo;
import com.atguigu.gmall.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Date;
import java.util.Map;

@Component
public class PaymentServiceMqListener {

    @Autowired
    PaymentService paymentService;

    @JmsListener(destination = "PAYMENT_CHECK_QUEUE",containerFactory = "jmsQueueListener")
    public void consumePaymentCheckResult(MapMessage mapMessage) throws JMSException {

        String out_trade_no = mapMessage.getString("out_trade_no");

        int count = mapMessage.getInt("count");

        //调用支付宝的支付接口
        Map<String,Object> resultMap = paymentService.checkAlipayPayment(out_trade_no);

        if(resultMap !=null && !resultMap.isEmpty()){
            String trade_status = (String)resultMap.get("trade_status");

            //根据查询结果，判断是否进行下一次延迟任务
            if(StringUtils.isNotBlank(trade_status) && trade_status.equals("TRADE_SUCCESS")){



                PaymentInfo paymentInfo = new PaymentInfo();
                paymentInfo.setOrderSn(out_trade_no);
                paymentInfo.setPaymentStatus("已支付");
                paymentInfo.setAlipayTradeNo((String) resultMap.get("trade_no"));// 支付宝的交易凭证号
                paymentInfo.setCallbackContent((String) resultMap.get("call_back_content"));//回调请求字符串
                paymentInfo.setCallbackTime(new Date());
                paymentService.updatePayment(paymentInfo);
                System.out.println("已经支付成功，修改支付信息");
                return;
            }
        }
        if(count > 0){
            System.out.println("没有支付成功，继续发送延迟队列，检查支付状态，检查次数为："+count+"次");
            //检查支付状态（延迟消息队列）
            count--;
            paymentService.sendDelayPaymentResultCheckQueue(out_trade_no, count);
        }else{

            System.out.println("检查次数用尽，结束检查");
        }







    }
}
