package cz.pojd.security.handler;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import cz.pojd.security.event.SecurityEvent;
import cz.pojd.security.event.SecurityEventTranslator;

/**
 * Email sender is simply sending email when a security breach is detected. It fetches the file if present on the event and sends that as an
 * attachement to the email
 *
 * @author Lubos Housa
 * @since Nov 9, 2014 12:42:42 AM
 */
public class EmailSender implements SecurityHandler {
    private static final Log LOG = LogFactory.getLog(EmailSender.class);

    @Inject
    private JavaMailSender mailSender;

    @Inject
    private SecurityEventTranslator<String> translator;

    @Value("${email.from}")
    private String from;
    @Value("${email.to}")
    private String to;
    @Value("${email.subject}")
    private String subject;

    public JavaMailSender getMailSender() {
	return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
	this.mailSender = mailSender;
    }

    public String getFrom() {
	return from;
    }

    public void setFrom(String from) {
	this.from = from;
    }

    public String getTo() {
	return to;
    }

    public void setTo(String to) {
	this.to = to;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    @Override
    public void handleSecurityBreach(SecurityEvent event) {
	LOG.info("Sending out email with the detail of the security event");
	if (LOG.isDebugEnabled()) {
	    LOG.debug("About to send email using: " + this + ". Event to send out: " + event);
	}

	try {
	    MimeMessage message = getMailSender().createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	    helper.setFrom(from);
	    helper.setTo(to);
	    helper.setSubject(subject + ": " + event.getType());
	    helper.setText(buildBody(event), true); // useHTML = true

	    // if we have a file path, then attach it
	    if (event.getFilePath() != null) {
		FileSystemResource file = new FileSystemResource(event.getFilePath().toString());
		helper.addAttachment(event.getFilePath().getFileName().toString(), file);
	    }

	    if (LOG.isDebugEnabled()) {
		LOG.debug("About to send message: " + message);
	    }

	    getMailSender().send(message);
	    LOG.info("Email sent.");
	} catch (Exception e) {
	    LOG.warn("Unable to send the email to " + to, e);
	}
    }

    private String buildBody(SecurityEvent securityEvent) {
	return translator.translate(securityEvent);
    }

    @Override
    public String toString() {
	return "EmailSender [mailSender=" + mailSender + ", from=" + from + ", to=" + to + ", subject=" + subject + "]";
    }
}
