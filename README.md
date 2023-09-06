## Overview

This is a prototype app for LA Document Uploader

## Data

`inputData` is the data entered by clients saved as a json blob in the DB

Example:
``` json
{
  "lastName": "Spears", 
  "birthDate": ["12", "2", "1981"],
  "firstName": "Britney",
  "caseNumber": "0000",
  "phoneNumber": "(123) 456-7890",
  "emailAddress": "itsbritney@example.com",
  "ssn": "some-encrypted-ssn",
  "uploadDocuments": "[\"some-file-path\"]",
  "uploadDocumentsFeedback": "easy",
  "uploadDocumentsFeedbackDetail": "this was super easy"
}
```
---

`url_params` are the params that are included in the requests 

Example:
``` json
{
  "lang": "en",
  "ref_id": "1234"
}
```