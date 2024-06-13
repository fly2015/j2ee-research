/*
 * MessageProtocol.java
 *
 * Copyright by Hien Ng
 * Da Nang
 * All rights reserved.
 */
package com.activemq.activemq;

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