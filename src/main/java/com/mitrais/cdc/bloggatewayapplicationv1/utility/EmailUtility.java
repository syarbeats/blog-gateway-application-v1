/**
 * <h1>EmailUtility</h1>
 * Class to handle sending email that required when user registration
 * and update password as well.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.bloggatewayapplicationv1.utility;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Getter
@Setter
public class EmailUtility {

    private User user;

    /**
     * This method will be used as proxy for
     * sending email.
     *
     * @param data
     * @return will return true if sending email process done successfully
     */
    public boolean sendEmail (Map<String, String> data) {
        log.info("Inside sendEmail1");


        try {
            if(sendEmail(data, data.get("contents"))) {
                return true;
            }else {
                return false;
            }
        } catch (MessagingException | IOException e) {
            log.error(e.getMessage(), e);
        }

        return false;
    }

    /**
     * This method will be used for sending email
     * that required when user registration and update password as well.
     *
     * @param data
     * @param contents
     * @return
     * @throws AddressException
     * @throws MessagingException
     * @throws IOException
     */
    public boolean sendEmail(Map<String, String> data, String contents) throws AddressException, MessagingException, IOException {

        log.info("Inside sendEmail2");
        String token = data.get("token");
        String email = data.get("email");
        String username = data.get("username");
        String subject = data.get("subject");

        try {
            log.info("Token-Inside sendEmail2: "+token);
            log.info("email-Inside sendEmail2: "+email);

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("cdcbootcamp@gmail.com", "CdcJavaBootcamp");
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("admin@onestopclick.com", false));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setContent(subject, "text/html");
            message.setSentDate(new Date());

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(contents, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            Transport.send(message);
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

        return true;
    }
}