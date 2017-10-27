package com.github.doghere.email

import java.util.Properties
import javax.activation.{DataHandler, FileDataSource}
import javax.mail._
import javax.mail.internet._


class Email(val emailMessage: EmailMessage, val emailServer: EmailServer) {
  def send(): Unit = {
    val props = new Properties
    props.put("mail.smtp.host", emailServer.server)
    props.put("mail.smtp.socketFactory.port", emailServer.port)
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
    props.put("mail.smtp.auth", emailServer.isSSL.toString)
    props.put("mail.smtp.port", emailServer.port)
    val session = Session.getDefaultInstance(props, new Authenticator() {
      override protected def getPasswordAuthentication: PasswordAuthentication
      = emailServer.account
    })

    val message = new MimeMessage(session)

    message.setFrom(new InternetAddress(emailMessage.from))

    emailMessage.to.foreach(to => {
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to))
    })

    emailMessage.cc.foreach(cc => {
      message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc))
    })


    message.setSubject(emailMessage.title)

    //body
    val multipart = new MimeMultipart
    multipart.addBodyPart(emailMessage.body)

    //resources
    emailMessage.dataSource.foreach(dataSource=>{
      val imgBodyPart = new MimeBodyPart()
      val fds = new FileDataSource(dataSource.path)
      imgBodyPart.setDataHandler(new DataHandler(fds))
      imgBodyPart.setContentID(dataSource.id)
      multipart.addBodyPart(imgBodyPart)
    })

    //attachments
    emailMessage.attachments.foreach(attachment => {
      val attachmentPart = new MimeBodyPart
      val source = new FileDataSource(attachment)
      attachmentPart.setDataHandler(new DataHandler(source))
      //chinese
      attachmentPart.setFileName(MimeUtility.encodeWord(attachment.getName))
      multipart.addBodyPart(attachmentPart)
    })
    message.setContent(multipart)

    Transport.send(message)
  }

  override def toString = s"SendEmail(emailMessage=$emailMessage, emailServer=$emailServer)"


}