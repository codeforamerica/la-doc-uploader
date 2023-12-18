package org.ladocuploader.app.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@AllArgsConstructor
@Getter
public class CloudFile {

  Long filesize;
  File file;
}
