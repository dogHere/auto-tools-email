package com.github.doghere.email

import javax.mail.PasswordAuthentication

class EmailServer(val server: String
                  , val port: String
                  , val account: PasswordAuthentication
                  , val isSSL: Boolean = true) {


  override def toString = s"EmailServer(server=$server, port=$port, account=$account, isSSL=$isSSL)"
}