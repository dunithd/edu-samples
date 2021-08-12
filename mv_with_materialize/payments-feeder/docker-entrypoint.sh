#!/usr/bin/env bash

# Copyright Materialize, Inc. All rights reserved.
#
# Use of this software is governed by the Business Source License
# included in the LICENSE file at the root of this repository.
#
# As of the Change Date specified in that file, in accordance with
# the Business Source License, use of this software will be governed
# by the Apache License, Version 2.0.

set -euo pipefail

wait-for-it --timeout=60 zookeeper:2181
wait-for-it --timeout=60 kafka:9092

topics=(
  payments
)

echo "${topics[@]}" | xargs -n1 -P8 kafka-topics --zookeeper zookeeper:2181 --create --if-not-exists --partitions 1 --replication-factor 1 --topic

echo "Starting payments event publishing..."

cat /tmp/minified-payments.txt | kafka-console-producer --broker-list kafka:9092 --topic payments

echo "Payments event publishing completed"