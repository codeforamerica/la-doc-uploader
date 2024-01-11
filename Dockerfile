FROM eclipse-temurin:17-jdk-alpine

RUN mkdir /opt/form-flow-starter-app
COPY . /opt/form-flow-starter-app
WORKDIR /opt/form-flow-starter-app

ARG APTIBLE_ENV=/opt/form-flow-starter-app/.aptible.env
RUN set -a  && \
    if [ -e ${APTIBLE_ENV} ] ; then . ${APTIBLE_ENV} ; fi && \
    ./gradlew assemble && \
    cp ./build/libs/app.jar app.jar

# Latest releases available at https://github.com/aptible/supercronic/releases
ENV SUPERCRONIC_VERSION=v0.2.25
ENV SUPERCRONIC_URL=https://github.com/aptible/supercronic/releases/download/${SUPERCRONIC_VERSION}/supercronic-linux-amd64 \
    SUPERCRONIC=supercronic-linux-amd64 \
    SUPERCRONIC_SHA1SUM=642f4f5a2b67f3400b5ea71ff24f18c0a7d77d49

RUN apk --no-cache add curl
RUN curl -fsSLO "$SUPERCRONIC_URL" \
 && echo "${SUPERCRONIC_SHA1SUM}  ${SUPERCRONIC}" | sha1sum -c - \
 && chmod +x "$SUPERCRONIC" \
 && mv "$SUPERCRONIC" "/usr/local/bin/${SUPERCRONIC}" \
 && ln -s "/usr/local/bin/${SUPERCRONIC}" /usr/local/bin/supercronic

EXPOSE 8080
