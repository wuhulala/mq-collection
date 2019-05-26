package com.wuhulala.rabbitmq.chapter1.exchange.topic;

/**
 * 0_0 o^o
 *
 * @author wuhulala<br>
 * @date 2019/5/25<br>
 * @description o_o<br>
 * @since v1.0<br>
 */
public interface Constants {

    String TOPIC_EXCHANGE_NAME = "topic-exchange";
    String P_ROUTING_KEY_1 = "A.B.C";
    String P_ROUTING_KEY_2 = "B.C";
    String C_ROUTING_KEY_1 = "A.B.*";
    String C_ROUTING_KEY_2 = "*.B.#";
    String C_ROUTING_KEY_3 = "#.B.C";

}
