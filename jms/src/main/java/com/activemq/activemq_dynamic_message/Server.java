/*
 * Server.java
 *
 * Copyright by Hien Ng
 * Da Nang
 * All rights reserved.
 */
package com.activemq.activemq_dynamic_message;

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
 * 
 *
 * @author nhqhien
 * @version $Revision:  $
 */
public class Server implements MessageListener
{
    private static int ackMode;
    private static String messageQueueName;

    private Session session;
    private boolean transacted = false;
    private MessageProducer replyProducer;
    private MessageProtocol messageProtocol;

    static
    {
        messageQueueName = "client.messages";
        ackMode = Session.AUTO_ACKNOWLEDGE;
    }

    public Server()
    {
        // Delegating the handling of messages to another class, instantiate it before setting up JMS so it
        // is ready to handle messages
        this.messageProtocol = new MessageProtocol();
        this.setupMessageQueueConsumer();
    }


    private void setupMessageQueueConsumer()
    {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constant.BROKER_URL);
        Connection connection;
        try
        {
            connection = connectionFactory.createConnection();
            connection.start();
            this.session = connection.createSession(this.transacted, ackMode);
            Destination adminQueue = this.session.createQueue(messageQueueName);

            // Setup a message producer to respond to messages from clients, we will get the destination
            // to send to from the JMSReplyTo header field from a Message
            this.replyProducer = this.session.createProducer(null);
            this.replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Set up a consumer to consume messages off of the admin queue
            MessageConsumer consumer = this.session.createConsumer(adminQueue);
            consumer.setMessageListener(this);
        }
        catch (JMSException e)
        {
            System.out.println(e);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }


    public void onMessage(Message message)
    {
        try
        {
            TextMessage response = this.session.createTextMessage();
            if (message instanceof TextMessage)
            {
                TextMessage txtMsg = (TextMessage)message;
                String messageText = txtMsg.getText();
                response.setText(this.messageProtocol.handleProtocolMessage(messageText));
            }

            // Set the correlation ID from the received message to be the correlation id of the response message
            // this lets the client identify which message this is a response to if it has more than
            // one outstanding message to the server
            response.setJMSCorrelationID(message.getJMSCorrelationID());

            // Send the response to the Destination specified by the JMSReplyTo field of the received message,
            // this is presumably a temporary queue created by the client
            this.replyProducer.send(message.getJMSReplyTo(), response);
        }
        catch (JMSException e)
        {
            // Handle the exception appropriately
        }
    }


    public static void main(String[] args)
    {
        new Server();
        System.out.println("Server is started.");
    }

}



/*
 * Changes:
 * $Log: $
 */