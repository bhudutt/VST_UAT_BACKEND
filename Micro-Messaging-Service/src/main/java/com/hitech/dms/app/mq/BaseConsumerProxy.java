package com.hitech.dms.app.mq;

import java.lang.reflect.Proxy;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.hitech.dms.app.common.Constant;
import com.hitech.dms.app.entity.MsgLog;
import com.hitech.dms.app.service.MsgLogService;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseConsumerProxy {

	private Object target;

	private MsgLogService msgLogService;

	public BaseConsumerProxy(Object target, MsgLogService msgLogService) {
		this.target = target;
		this.msgLogService = msgLogService;
	}

	public Object getProxy() {
		ClassLoader classLoader = target.getClass().getClassLoader();
		Class[] interfaces = target.getClass().getInterfaces();

		Object proxy = Proxy.newProxyInstance(classLoader, interfaces, (proxy1, method, args) -> {
			Message message = (Message) args[0];
			Channel channel = (Channel) args[1];

			String correlationId = getCorrelationId(message);

			if (isConsumed(correlationId)) { // Consumption is idempotent, preventing messages from being repeatedly
												// consumed
				log.info("Repeated consumption, correlationId: {}", correlationId);
				return null;
			}

			MessageProperties properties = message.getMessageProperties();
			long tag = properties.getDeliveryTag();

			try {
				Object result = method.invoke(target, args); // business logic for real consumption
				msgLogService.updateStatus(correlationId, Constant.MsgLogStatus.CONSUMED_SUCCESS);
				channel.basicAck(tag, false); // consumption confirmation
				return result;
			} catch (Exception e) {
				log.error("getProxy error", e);
				channel.basicNack(tag, false, true);
				return null;
			}
		});

		return proxy;
	}

	/**
	 * Get CorrelationId
	 *
	 * @param message
	 * @return
	 */
	private String getCorrelationId(Message message) {
		String correlationId = null;

		MessageProperties properties = message.getMessageProperties();
		Map<String, Object> headers = properties.getHeaders();
		for (Map.Entry entry : headers.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (key.equals("spring_returned_message_correlation")) {
				correlationId = value;
			}
		}

		return correlationId;
	}

	/**
	 * Whether the message has been consumed
	 *
	 * @param correlationId
	 * @return
	 */
	private boolean isConsumed(String correlationId) {
		MsgLog msgLog = msgLogService.selectByMsgId(correlationId);
		if (null == msgLog || msgLog.getStatus().equals(Constant.MsgLogStatus.CONSUMED_SUCCESS)) {
			return true;
		}

		return false;
	}

}
