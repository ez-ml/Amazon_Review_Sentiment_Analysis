import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._

object FeatureEngineering1 {
  def main(args: Array[String]): Unit = {
    print("Starting Feature Engineering 1")
    print("####################################################################################################################################")
    val spark = SparkSession
      .builder()
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .config("spark.hadoop.fs.s3a.multiobjectdelete.enable","false")
      .config("spark.hadoop.fs.s3a.fast.upload","true")
      .getOrCreate()

    import spark.implicits._


    



    val malware_list_rdd = spark.sparkContext.wholeTextFiles("s3a://ml-workflow-data/security/Malware_Dataset/Malware_Classification_DataSet/*.asm", 500)
    val malware_df = spark.createDataFrame(malware_list_rdd).toDF("File", "Text").
      withColumn("file_name",split(col("File"),"/").
      getItem(6)).withColumn("file_name", regexp_replace($"file_name", ".asm" , "") )


    val fileName_df= spark.read.format("csv").option("inferSchema", "true").
      option("header", "true").
      load("s3a://ml-workflow-data/security/Malware_Dataset/Malware_Classification_DataSet/Training_Label_Classification_SmallSet.csv")

    val mergedDf = malware_df.join(fileName_df, Seq("file_name"), "inner")


    mergedDf.write.mode("overwrite").format("parquet").option("compression", "snappy").mode("overwrite").save("s3a://ml-workflow-data/security/Malware_Dataset/Output/malware_tmp_parquet_11")

    mergedDf.show(20)

    spark.sparkContext.stop()
    print("####################################################################################################################################")

  }

}