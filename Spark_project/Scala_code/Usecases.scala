import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Column
import org.apache.spark.sql.types._
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType};

object awards
{
def main(args: Array[String] {

val sqlContext = new org.apache.sparl.sql.SQLContext(sc);

 import sqlContext.implicits._

val schema = StructType(List(
  StructField ("director_name", StringType, true),
  StructField("ceremony", StringType, true),
  StructField("year", IntegerType, true),
  StructField("category", StringType, true),
  StructField("outcome", StringType, true),
  StructField("original_lang", StringType, true)
))

val DF = (spark.readStream.format("csv").option("maxFilesPerTrigger", 2).option("header", "false").option("path","/user/project/*").schema(schema).load())

DF.createOrReplaceTempView("awards")
val query = """select * from awards where outcome='Won' OR outcome='Nominated' having year=2011"""
val q1 =spark.sql(query)

//val query = """select distinct ceremony, category from awards where ceremony='Berlin International Film Festival'"""
//val q2 =spark.sql(query)

//val query = """select director_name, count(outcome) from awards where outcome='Won' and original_lang='fr' group by director_name"""
//val q3 =spark.sql(query)

//val query = """Select temp.director_name,temp.no_of_awards from (Select director_name,count(*) as no_of_awards from awards where outcome='Won' group by direc tor_name)temp where no_of_awards>10"""
//val q4 =spark.sql(query)

q1.writeStream.outputMode("update").format("console").start().awaitTermination(10)

}
 }
