package com.github.doghere.email

import java.io.File
import javax.mail.BodyPart

class EmailMessage(val from: String
                   , val to: List[String]
                   , val cc: List[String]
                   , val title: String
                   , val body: BodyPart
                   , val attachments: List[File] = List[File]()
                   , val dataSource: List[EmailReSource]=List[EmailReSource]()) {

  override def toString = s"EmailMessage(from=$from, to=$to, cc=$cc, title=$title, body=$body, attachments=$attachments)"
}

