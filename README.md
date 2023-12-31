# Spring Batch with Spark Demo

## Goal

The main objective of this project is to integrate Apache Spark inside Spring Batch, with the possibility to read from
different and multiple datasources, join the results of the queries and store the output data into a file.

## What done

First of all, this Spring Batch job has been defined and handled as a mono-job and mono-step batch flow.

Then, an initial integration of Apache Spark has been done. The main configuration of Spark is present inside the 
class _ApplicationSparkConfig_.
To use it, a custom _ItemReader_ needs to be created in order to read the data from the specific datasource and join/merge
the results.

The datasources used are:
- H2
- MySQL
- BigQuery

For the first two datasources, the configuration is inside the application.yaml file and the class _BatchDatabaseConfiguration_
is made to instantiate them in the Spring context.

For BigQuery, it is necessary to have an account on Google Cloud (obviously) and a credentials JSON file related to the
service account used to connect to BigQuery. (the GCP documentation is your best friend :) )

The custom reader is able to read the data from these three different sources, then join MySQL with BigQuery and final join
with the H2 one using Spark APIs.

<ins>Please note that this is a really basic version just for testing and understanding the functionalities of Apache Spark inside
Spring Batch.</ins>

## How to
If you want to test this repo, you must have a MySQL server running with a basic table (see init.sql for an example) and
the GCP environment set up correctly.
Before running, remember to set the following env variables:
- GOOGLE_APPLICATION_CREDENTIALS: containing the path of credentials JSON file
- GOOGLE_CLOUD_PROJECT: the project id of GCP
- HADOOP_HOME: the path of Hadoop main folder since Spark uses it to work
