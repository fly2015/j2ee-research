/*
 * Sender.java
 *
 * Copyright by CRIF AG
 * Z�rich
 * All rights reserved.
 */
package com.activemq.activemq_dynamic_message.sender;


import com.activemq.activemq_dynamic_message.Client;

/**
 * 
 *
 * @author nhqhien
 * @version $Revision:  $
 */
public class Sender
{
    public static void main(String[] args)
    {
        new Client("Hello");
        System.out.println("Message is sent by " + Sender.class.getSimpleName());
       
    }
}



/*
 * Changes:
 * $Log: $
 */