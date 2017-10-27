package com.github.doghere.email

import java.io.File
import javax.mail.PasswordAuthentication
import javax.mail.internet.MimeBodyPart

import com.github.doghere.email.util.PathHelp

import scala.xml.{Elem, NodeSeq}


class Config {


  def load(mail:NodeSeq,emailServer: EmailServer,workDir:String): Email ={
    val from = (mail \ "from" text).trim
    val to = (mail \ "to" \ "p").toList.map(_.text.trim)
    val cc = (mail \ "cc" \ "p").toList.map(_.text.trim)
    val title = mail \ "title" text
    val body = mail \ "body" toString()
    val attachments = (mail \ "attachments" \ "a" toList).map(a => new File(PathHelp.toAbsolutePath(workDir,a.text.trim).toString))
    val dataSource  = (mail \ "resuorces" \ "resource").map(r=>{
      new EmailReSource(PathHelp.toAbsolutePath(workDir,(r \ "file").text.trim).toString,(r \ "id").text.trim)
    }).toList

    val email = new Email(
      new EmailMessage(
        from = from
        , to = to
        , cc = cc
        , title = title
        , body = {
          val contentPart = new MimeBodyPart
          contentPart.setContent(body, "text/html;charset=utf-8")
          contentPart
        }
        , attachments = attachments
        , dataSource = dataSource
      )
      , emailServer
    )
    email
  }

  def load(configFile:File): List[Email] ={
    val config: Elem = scala.xml.XML
      .load(configFile.getPath)

    val emails: NodeSeq = config \ "emails"
    load(emails: NodeSeq,configFile.getParent:String)
  }

  /**
    *
    * @param emails
    * @param workPath work dir
    */
  def load(emails: NodeSeq,workPath:String): List[Email] ={

    if(emails.text.trim!="") {
      //mail
      val emailServer = emails \ "emailServer"
      val server = (emailServer \ "server"  ).text.trim
      val port = (emailServer \ "port"  ).text.trim
      val username = (emailServer \ "username"  ).text.trim
      val password = (emailServer \ "password"  ).text.trim

      val mailServer = new EmailServer(
        server = server
        , port = port
        , new PasswordAuthentication(username, password))

      (emails \ "email").toList.map(mail => {
        load(mail:NodeSeq,mailServer: EmailServer,workPath)
      })
    }else{
      List[Email]()
    }
  }

}
