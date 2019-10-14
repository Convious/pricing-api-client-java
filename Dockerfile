FROM maven:3.6.2-jdk-11
RUN apt-get update -y && \
    apt-get install -y gnupg
RUN mkdir -p /code
WORKDIR /code
ADD . /code
RUN gpg --import public.asc
RUN gpg --import private.asc
ADD settings.xml /root/.m2/settings.xml
RUN gpg --list-keys --fingerprint --with-colons | \
  sed -E -n -e 's/^fpr:::::::::([0-9A-F]+):$/\1:6:/p' | \
  gpg --import-ownertrust
RUN gpg --list-keys
