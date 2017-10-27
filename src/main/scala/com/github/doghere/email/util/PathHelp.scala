package com.github.doghere.email.util

import java.io.File
import java.nio.file.{Path, Paths}

object PathHelp {
  def toAbsolutePath(base:String,path:String): Path ={
    if(new File(path).getAbsolutePath == path){
      Paths.get(path)
    }else{
      Paths.get(base,path)
    }
  }
}
