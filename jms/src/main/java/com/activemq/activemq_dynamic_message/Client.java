/*
 * Client.java
 *
 * Copyright by Hien Ng
 * Da Nang
 * All rights reserved.
 */
package com.activemq.activemq_dynamic_message;

import java.util.Random;

import jakarta.jms.Connection;
import jakarta.jms.DeliveryMode;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


/**
 * @author nhqhien
 * @version $Revision: $
 */
public class Client implements MessageListener
{
    private static int ackMode;
    private static String clientQueueName;

    private boolean transacted = false;
    private MessageProducer producer;

    static
    {
        clientQueueName = "client.messages";
        ackMode = Session.AUTO_ACKNOWLEDGE;
    }

    public Client(String message)
    {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constant.BROKER_URL);
        Connection connection;
        try
        {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(transacted, ackMode);
            Destination adminQueue = session.createQueue(clientQueueName);

            // Setup a message producer to send message to the queue the server is consuming from
            this.producer = session.createProducer(adminQueue);
            this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a temporary queue that this client will listen for responses on then create a consumer
            // that consumes message from this temporary queue...for a real application a client should reuse
            // the same temp queue for each message to the server...one temp queue per client
            Destination tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);

            // This class will handle the messages to the temp queue as well
            responseConsumer.setMessageListener(this);

            // Now create the actual message you want to send
            TextMessage txtMessage = session.createTextMessage();
            txtMessage.setText(message);

            // Set the reply to field to the temp queue you created above, this is the queue the server
            // will respond to
            txtMessage.setJMSReplyTo(tempDest);

            // Set a correlation ID so when you get a response you know which sent message the response is for
            // If there is never more than one outstanding message to the server then the
            // same correlation ID can be used for all the messages...if there is more than one outstanding
            // message to the server you would presumably want to associate the correlation ID with this
            // message somehow...a Map works good
            String correlationId = this.createRandomString();
            txtMessage.setJMSCorrelationID(correlationId);
            this.producer.send(txtMessage);
        }
        catch (JMSException e)
        {
            // Handle the exception appropriately
        }
    }


    private String createRandomString()
    {
        Random random = new Random(System.currentTimeMillis());
        long randomLong = random.nextLong();
        return Long.toHexString(randomLong);
    }


    public void onMessage(Message message)
    {
        String messageText = null;
        try
        {
            if (message instanceof TextMessage)
            {
                TextMessage textMessage = (TextMessage)message;
                messageText = textMessage.getText();
                System.out.println("messageText = " + messageText);
            }
        }
        catch (JMSException e)
        {
            // Handle the exception appropriately
        }
    }
}

/*
 * Changes:
 * $Log: $
 */