from pdpyras import APISession
from requests.sessions import session
from kafka import KafkaConsumer
import json
import sys

print("Starting the alerts listener")

# Kafka configurations
kafka_broker = 'kafka:9092'
alerts_topic = "high-temp-alerts-u5-1631586083-13100517045407420899"

# PageDuty configurations
api_token = 'u+CtEy_6s91Pp93RM7sQ'
service_id = "PU35FY7"
from_email = "dunithd@gmail.com"

# Initialize the PageDuty session
# session = APISession(api_token)
# session.default_from = "dunithd@gmail.com"

consumer = KafkaConsumer(alerts_topic,
    bootstrap_servers=[kafka_broker],
    auto_offset_reset='earliest',
    enable_auto_commit=True,
    group_id='my-group',
    value_deserializer=lambda x: json.loads(x.decode('utf-8')))

# consumer.subscribe(pattern=alerts_topic_pattern)

for message in consumer:
  try:
      event = message.value
      if event["after"] is None:
        continue
      else:
        row = event["after"]["row"]

      # print("sensor-uuid: %s average: %s"%(row["sensor_uuid"],str(row["avg"])))
      sensor_uuid = row["sensor_uuid"]
      avg_temperature = row["avg"]

      # Trigger a PD incident
      incident_title = "High temperature observed in the data center"
      incident_description = "The temperature sensor %s observed an average temperature of %s during the past minute." % (sensor_uuid,str(avg_temperature))

      payload = {
        "type": "incident",
        "service": {"id": "", "type": "service_reference"},
        "body": {"type": "incident_body", "details": ""}
      }

      # Manipulate the payload 
      payload["title"] = incident_title
      payload["service"]["id"] = service_id
      payload["body"]["details"] = incident_description

      # pd_incident = session.rpost("incidents", json=payload)
      # print(pd_incident)
      print("Incident triggered")
      print(json.dumps(payload))
  except:
    print("An error occured while triggering the incident.", sys.exc_info()[0])




