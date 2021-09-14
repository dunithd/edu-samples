CREATE SOURCE sensor_data_raw
FROM PUBNUB
SUBSCRIBE KEY 'sub-c-5f1b7c8e-fbee-11e3-aa40-02ee2ddab7fe'
CHANNEL 'pubnub-sensor-network';

CREATE VIEW sensor_data AS
SELECT
    ((text::jsonb)->>'sensor_uuid') AS sensor_uuid,
    ((text::jsonb)->>'ambient_temperature')::float AS ambient_temperature,
    ((text::jsonb)->>'radiation_level')::int AS radiation_level,
    ((text::jsonb)->>'humidity')::float AS humidity,
    ((text::jsonb)->>'photosensor')::float AS photosensor,
    to_timestamp(((text::jsonb)->'timestamp')::bigint) AS ts
FROM sensor_data_raw;

CREATE MATERIALIZED VIEW anomalies AS
SELECT sensor_uuid,
       AVG(ambient_temperature) AS avg
FROM sensor_data
WHERE EXTRACT(EPOCH FROM (ts + INTERVAL '5 seconds'))::bigint * 1000 > mz_logical_timestamp()
GROUP BY sensor_uuid
HAVING AVG(ambient_temperature) > 40;

CREATE SINK alerts
FROM anomalies
INTO KAFKA BROKER 'kafka:9092' TOPIC 'high-temp-alerts'
    CONSISTENCY TOPIC 'high-temp-alerts-consistency'
    CONSISTENCY FORMAT AVRO USING CONFLUENT SCHEMA REGISTRY 'http://schema-registry:8081'
WITH (reuse_topic=true)
FORMAT JSON;
