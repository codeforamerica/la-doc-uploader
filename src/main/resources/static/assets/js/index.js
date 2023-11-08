document.addEventListener("DOMContentLoaded", function() {
  var sameAsAddressBox = document.getElementById("sameAsHomeAddress-true-label");

  if (sameAsAddressBox) {
    sameAsAddressBox.addEventListener("click", function() {
      document.getElementById("mailingAddressStreetAddress1").value="";
      document.getElementById("mailingAddressStreetAddress2").value="";
      document.getElementById("mailingAddressCity").value="";
      document.getElementById("mailingAddressState").value="";
      document.getElementById("mailingAddressZipCode").value="";
    });
  }
});