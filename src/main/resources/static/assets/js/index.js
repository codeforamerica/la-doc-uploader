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
});