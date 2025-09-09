package com.hitech.dms.app.service.mail;

import java.math.BigInteger;

import com.hitech.dms.app.common.ServerResponse;
import com.hitech.dms.app.entity.Mail;
import com.hitech.dms.app.entity.MailWithTemplate;

public interface MailService {

    ServerResponse testIdempotence();

    ServerResponse accessLimit();

    ServerResponse send(Mail mail, BigInteger mailItemId);
    
    ServerResponse sendWithTemplate(MailWithTemplate mail, BigInteger mailItemId);
    
    public ServerResponse sendPasswordWithTemplate(MailWithTemplate mail, BigInteger mailItemId);
    
    public ServerResponse sendChangeWithTemplate(MailWithTemplate mail, BigInteger mailItemId);
    
    public ServerResponse sendCreateEnqWithTemplate(MailWithTemplate mail, BigInteger mailItemId);
    
    public ServerResponse sendEnqFollowupWithTemplate(MailWithTemplate mail, BigInteger mailItemId);
    
    public ServerResponse sendValidatedEnqWithTemplate(MailWithTemplate mail, BigInteger mailItemId);
    
}