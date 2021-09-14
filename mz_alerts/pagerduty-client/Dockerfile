FROM python:3

RUN apt-get update && apt-get -qy install curl

RUN pip install kafka-python pdpyras

COPY . /pagerduty_client

COPY docker-entrypoint.sh /usr/local/bin

ENTRYPOINT ["docker-entrypoint.sh"]
