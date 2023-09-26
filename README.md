# Overview

This is a prototype app for LA Document Uploader. This is a standard Spring Boot application that uses the `form-flows` Java package as a library.

To see an example of an application built on form flow, check out the [form-flow-starter-app repo.](https://github.com/codeforamerica/form-flow-starter-app)

# Setup Instructions

## System dependencies
- java
- java development kit
- gradle
- postgress
- intelliJ and EnvFile
- Database Setup
- [form-flow library](https://github.com/codeforamerica/form-flow)

_Note: these instructions are specific to macOS, but the same dependencies do need to be installed
on Windows as well._

### Java Development Kit

If you do not already have Java 17 installed, we recommend doing this:

```
brew tap homebrew/cask-versions
brew install --cask temurin17
```

### Set up jenv to manage your jdk versions

First run `brew install jenv`.

Add the following to your `~/.bashrc` or `~/.zshrc`:

```
export PATH="$HOME/.jenv/bin:$PATH"
eval "$(jenv init -)"
```

For m1 macs, if the above snippet doesn't work, try:

```
export PATH="$HOME/.jenv/bin:$PATH"
export JENV_ROOT="/opt/homebrew/Cellar/jenv/"
eval "$(/opt/homebrew/bin/brew shellenv)"
eval "$(jenv init -)"
```

Reload your terminal, then finally run this from the repo's root directory:

```
jenv add /Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home
```

### Gradle

`brew install gradle`

### Start the local databases

- Install PostgreSQL 14 via an [official download](https://www.postgresql.org/download/)
    - Or on macOS, through homebrew: `brew install postgresql@14`

## Database Setup
Create a new database and user for the la-doc-uploader app and test app:
```
$ createdb la-doc-uploader
$ createuser -s la-doc-uploader.

$ createdb la-doc-uploader-test
$ createuser -s la-doc-uploader-test
```

## Setup IntelliJ and EnvFile
Note that you'll need to provide some environment variables specified in [sample.env](sample.env) to
your IDE/shell to run the application. We use IntelliJ and have provided setup instructions for
convenience.

- `cp sample.env .env` (.env is marked as ignored by git)
- Download the [EnvFile plugin](https://plugins.jetbrains.com/plugin/7861-envfile) and follow the
  setup instructions [here](https://github.com/Ashald/EnvFile#usage) to setup Run Configurations with
  EnvFile.

### Setup Live Templates

- Use instructions from
  the [form-flow library here.](https://github.com/codeforamerica/form-flow#intellij-setup)

## Using a local version of the Form-Flow Library (For Form-Flow Library Developers)

To use a local version of the  [form-flow](https://github.com/codeforamerica/form-flow) library you
can do the following:

1. Clone the `form-flow` repo in the same directory as the la-doc-uploader app.
2. Build the `form-flow` library jar: `./gradlew clean test build`
3. In this starter app, set the `SPRING_PROFILES_ACTIVE`  to `dev` in
   the .env file
4. Start the `la-doc-uploader-app`: `./gradlew clean test build`  

Changing the `SPRING_PROFILES_ACTIVE` to `dev` will cause the starter
app's [build.gradle](build.gradle) to pull in the local library, via this line:

 ```
 implementation fileTree(dir: "$rootDir/../form-flow/lib/build/libs", include: '*.jar')
 ```


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
