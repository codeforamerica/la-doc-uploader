document.addEventListener("DOMContentLoaded", function() {
  var sameAsAddressBox = document.getElementById("sameAsHomeAddress-true-label");

  if (sameAsAddressBox) {
    sameAsAddressBox.addEventListener("click", function() {
      var selectedClass = document.getElementsByClassName('is-selected')
      var mailFields = document.getElementById("hidden-mailing-fields")
      if(selectedClass.length > 0){
        mailFields.style.display="none";
      } else {
        mailFields.style.display="block";
      }
    });
  }

  var noPermanentAddress = document.getElementById("noHomeAddress-true-label");

  if (noPermanentAddress) {
    noPermanentAddress.addEventListener("click", function() {
      var selectedClass = document.getElementsByClassName('is-selected')
      var mailFields = document.getElementById("hidden-home-address-fields")
      if(selectedClass.length > 0){
        mailFields.style.display="none";
      } else {
        mailFields.style.display="block";
      }
    });
  }
});

function setCsrf(cookieName) {
  console.log("attempting to set cookie");
  var element = document.getElementById("token");
  if (element) {
    let cookie = cookieName + '=' + element.getAttribute("value") + ';';
    document.cookie = cookie;
    console.log("setting CSRF to: " + cookie);
  }
}
