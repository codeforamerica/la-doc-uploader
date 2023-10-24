// make the call to the server to delete the file.
function sendDeleteXhrRequest(id, flow) {
  var xhrRequest = new XMLHttpRequest();
  xhrRequest.open('POST',
      '/file-delete?' + id + '&returnPath=' + window.location.pathname +
      '&flow=' + flow, true);
  xhrRequest.withCredentials = false;
  xhrRequest.setRequestHeader("Accept", "*/*");
  xhrRequest.setRequestHeader("X-Requested-With", "XMLHttpRequest");
  var csrfToken = [[${_csrf.token}]];
  var formData = new FormData();
  formData.append("_csrf", csrfToken);
  formData.append("id", id);
  xhrRequest.send(formData);
}

//
// This needs to make all the changes in the page based on the file being deleted
function removeFileDocTypePage(file, id) {
  // remove the file from the page
  window[dropzonePrefix + [[${inputName}]]].removeFile(file);
  if (id) {
    let toDeleteIdx = window['userFileIds' + [[${inputName}]]].indexOf(id);
    if (toDeleteIdx !== -1) {
      window['userFileIds' + [[${inputName}]]].splice(toDeleteIdx, 1)
    }
  }
  updateFileInputValue();
  showNumberOfAddedFiles();
}

// for cancel link
removeLink.onclick = function () {
  var confirmMsg = `[[${#messages.msg('general.files.confirm-delete', '${fileName}')}]]`;
  let confirmation = confirm(confirmMsg.replaceAll('"',''))

  if (confirmation) {
    sendDeleteXhrRequest(id, [[${flow}]]);
    mixpanel.track('file_upload_delete', {
      'page_name': window.location.pathname,
      'file_size': file.size,
      'file_format': file.type
    });
    removeFileDocTypePage(file, id);
  }
}