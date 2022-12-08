package com.awards;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class AwardsApplication {
    public static void main(String[] args) throws Exception {

        SparkSession spark = SparkSession.builder().appName("spark streaming").config("spark.master", "local").config("spark.sql.warehouse.dir", "file:///apps/").getOrCreate();
        spark.sparkContext().setLogLevel("ERROR");
StructType schema = new StructType().add("director_name", DataTypes.StringType).add("ceremony", DataTypes.StringType).add("year", DataTypes.IntegerType).add("category", DataTypes.StringType).add("outcome", DataTypes.StringType).add("original_lang", DataTypes.StringType);
Dataset<Row> rawData = spark.readStream().option("header","false").format("csv").schema(schema).csv("/user/project/*");

        rawData.createOrReplaceTempView("awards");

        Dataset<Row> result = spark.sql("select director_name, count(outcome) as french_awards from awards where outcome='Won' and original_lang='fr' group by director_name");

        StreamingQuery query = result.writeStream().outputMode(OutputMode.Update()).format("console").start();

        query.awaitTermination();
    }
}
