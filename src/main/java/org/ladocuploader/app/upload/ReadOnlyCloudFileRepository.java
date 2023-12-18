package org.ladocuploader.app.upload;

public interface ReadOnlyCloudFileRepository {

  CloudFile download(String filePath);
}
