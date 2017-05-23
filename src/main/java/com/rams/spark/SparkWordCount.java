package com.rams.spark;


import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SparkWordCount {
	
	public static void wordCountWithJava8(String inputFilePath, String outputPath){
		//spark configuration
		SparkConf conf = new SparkConf().setAppName("SparkWordCount").setMaster("local");
		 
		// intialize spark context
		JavaSparkContext sc = new JavaSparkContext(conf);
		 
		//read input file
		JavaRDD<String> lines = sc.textFile(inputFilePath);
		 
		// convert each line into list of words
		JavaRDD<String> words =lines.flatMap(line -> Arrays.asList(line.split(" ")));
		 
		// assign count 1 to each word visited
		JavaPairRDD<Object, Object> wordCounts = words.mapToPair(w -> new Tuple2(w, 1)).reduceByKey((x, y) -> (int)x + (int)y).sortByKey(false);;
		 
		//save out put to file
		wordCounts.saveAsTextFile(outputPath);
		 
		// close spark context
		sc.close();
        
	}

	/**
	 * [root@sandbox ~]# spark-submit --class com.rams.spark.SparkWordCount --master local --deploy-mode client --executor-memory 1g --name sparkwordcount --conf "spark.app.id=sparkwordcnt" spark-mapred-proj-0.0.1-SNAPSHOT.jar hdfs://sandbox.hortonworks.com:8020//rams/input/sampledata.txt hdfs://sandbox.hortonworks.com:8020//rams/output/output_sparkword_count_0426
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//Below property need to be uncommented for local run
		//System.setProperty("hadoop.home.dir", "C:\\winutils\\");
		if(args.length != 2){
			System.out.println("Pass inputFilePath and outputPath as arguments");
		}
		wordCountWithJava8(args[0], args[1]);
	}

}
