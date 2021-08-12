CREATE SOURCE orders
FROM KAFKA BROKER 'kafka:9092' TOPIC 'mysql.order_db.orders'
FORMAT AVRO USING CONFLUENT SCHEMA REGISTRY 'http://schema-registry:8081' ENVELOPE DEBEZIUM;

CREATE SOURCE shipments
FROM KAFKA BROKER 'kafka:9092' TOPIC 'postgres.public.shipments'
FORMAT AVRO USING CONFLUENT SCHEMA REGISTRY 'http://schema-registry:8081' ENVELOPE DEBEZIUM;

CREATE SOURCE payments
FROM KAFKA BROKER 'kafka:9092' TOPIC 'payments'
FORMAT BYTES;

CREATE VIEW payments_view AS
  SELECT
    (payment_data ->'payment_id')::INT as payment_id,
    (payment_data ->'order_id')::INT as order_id,
    (payment_data ->'payment_date')::STRING as payment_date,
    (payment_data ->'payment_method')::STRING as payment_method,
    (payment_data ->'amount')::FLOAT as amount,
    (payment_data ->'status')::STRING as status
  FROM (
    SELECT convert_from(data, 'utf8')::jsonb AS payment_data
    FROM payments
  );

  CREATE MATERIALIZED VIEW order_history AS
    SELECT
      orders.order_id as order_id,
      orders.customer_id as customer_id,
      orders.order_date as order_date,
      orders.total as total,
      payments_view.status as payment_status,
      shipments.status as shipment_status
    FROM
      orders,
      payments_view,
      shipments
    WHERE
      orders.order_id = payments_view.order_id
      AND payments_view.order_id = shipments.order_id;