/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 */

package org.wso2.mb.integration.tests.mqtt.functional;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.engine.context.TestUserMode;
import org.wso2.mb.integration.common.clients.MQTTClientEngine;
import org.wso2.mb.integration.common.clients.MQTTConstants;
import org.wso2.mb.integration.common.clients.QualityOfService;
import org.wso2.mb.integration.common.clients.ClientMode;
import org.wso2.mb.integration.common.utils.backend.MBIntegrationBaseTest;

import javax.xml.xpath.XPathExpressionException;
import java.util.List;

/**
 * Verifies basic mqtt message transactions are functional.
 * <p/>
 * Send a single mqtt messages on qos 1 and receive.
 * Send 100 messages on qos 1 and receive them.
 */
public class BasicSendReceiveTestCase extends MBIntegrationBaseTest {

    /**
     * Initialize super class.
     *
     * @throws Exception
     */
    @BeforeClass
    public void prepare() throws Exception {
        super.init(TestUserMode.SUPER_TENANT_USER);
    }

    /**
     * Send a single mqtt message on qos {@link QualityOfService#LEAST_ONCE} and receive.
     *
     * @throws MqttException
     */
    @Test(groups = {"wso2.mb", "mqtt"}, description = "Single mqtt message send receive test case")
    public void performBasicSendReceiveTestCase() throws MqttException, XPathExpressionException {
        String topic = "topic";
        int noOfSubscribers = 1;
        int noOfPublishers = 1;
        int noOfMessages = 1;
        boolean saveMessages = true;
        MQTTClientEngine mqttClientEngine = new MQTTClientEngine();
        //create the subscribers
        mqttClientEngine.createSubscriberConnection(topic, QualityOfService.LEAST_ONCE, noOfSubscribers, saveMessages,
                ClientMode.BLOCKING, automationContext);

        mqttClientEngine.createPublisherConnection(topic, QualityOfService.LEAST_ONCE,
                MQTTConstants.TEMPLATE_PAYLOAD, noOfPublishers,
                noOfMessages, ClientMode.BLOCKING, automationContext);

        mqttClientEngine.waitUntilAllMessageReceivedAndShutdownClients();

        List<MqttMessage> receivedMessages = mqttClientEngine.getReceivedMessages();

        Assert.assertEquals(receivedMessages.size(), noOfMessages, "The received message count is incorrect.");

        Assert.assertEquals(receivedMessages.get(0).getPayload(), MQTTConstants.TEMPLATE_PAYLOAD,
                "The received message is incorrect");

    }

    /**
     * Send 100 mqtt message on qos {@link QualityOfService#LEAST_ONCE} and receive them.
     *
     * @throws MqttException
     */
    @Test(groups = {"wso2.mb", "mqtt"}, description = "Single mqtt message send receive test case")
    public void performBasicSendReceiveMultipleMessagesTestCase()
            throws MqttException, XPathExpressionException {
        String topic = "topic";
        int noOfSubscribers = 1;
        int noOfPublishers = 1;
        int noOfMessages = 100;
        boolean saveMessages = false;
        MQTTClientEngine mqttClientEngine = new MQTTClientEngine();
        //create the subscribers
        mqttClientEngine.createSubscriberConnection(topic, QualityOfService.LEAST_ONCE, noOfSubscribers, saveMessages,
                ClientMode.BLOCKING, automationContext);

        mqttClientEngine.createPublisherConnection(topic, QualityOfService.LEAST_ONCE,
                MQTTConstants.TEMPLATE_PAYLOAD, noOfPublishers,
                noOfMessages, ClientMode.BLOCKING, automationContext);

        mqttClientEngine.waitUntilAllMessageReceivedAndShutdownClients();

        Assert.assertEquals(mqttClientEngine.getReceivedMessageCount(), noOfMessages,
                "The received message count is incorrect.");

    }


}
