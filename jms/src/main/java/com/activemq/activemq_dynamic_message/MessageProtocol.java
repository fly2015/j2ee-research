/*
 * MessageProtocol.java
 *
 * Copyright by Hien Ng
 * Da Nang
 * All rights reserved.
 */
package com.activemq.activemq_dynamic_message;

/**
 * 
 *
 * @author nhqhien
 * @version $Revision:  $
 */
public class MessageProtocol
{
    public String handleProtocolMessage(String messageText)
    {
        try
        {
            Thread.sleep(200000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String responseText;
        if ("MyProtocolMessage".equalsIgnoreCase(messageText))
        {
            responseText = "I recognize your protocol message";
        }
        else
        {
            responseText = "Unknown protocol message: " + messageText;
        }

        return responseText;
    }
}



/*
 * Changes:
 * $Log: $
 */