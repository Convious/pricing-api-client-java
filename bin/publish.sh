#!/usr/bin/env bash
echo $SONATYPE_PRIVATE_KEY > private.asc
docker-compose run --rm -e SONATYPE_USER=$SONATYPE_USER -e SONATYPE_PASSWORD=$SONATYPE_PASSWORD build mvn deploy
