package org.ladocuploader.app.upload;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
class NoOpReadOnlyCloudFileRepository implements ReadOnlyCloudFileRepository {

  @Override
  public CloudFile download(String filePath) {
    return null;
  }
}
