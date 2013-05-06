package com.dsa.pcapneo.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class PcapSummaryLoad {
	private static final Log log = LogFactory.getLog(PcapSummaryLoad.class);

	public static class DateMapper extends
			Mapper<Object, Text, Text, Text> {

		private static final int dateHourEnd = 8;

		// Reads in values and writes to reducer, grouping by day hour
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			Text dateHour = new Text(value.toString().substring(0, dateHourEnd));
			context.write(dateHour, value);
		}
	}

	public static class PcapSummaryReducer extends Reducer<Text, Text, Text, PcapSummary> {
		private static final String COMMA = ",";
		private final SimpleDateFormat format = PcapSummary.getPcapDateFormatter();

		private final PcapSummary pcapSummary = new PcapSummary();
		
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			for (Text val : values) {
				// Example value:
				// From Wireshark summary:
				// 20120212100205.838,169.254.121.61,169.254.255.255,DB-LSP-DISC,151,Dropbox
				// From tshark event:
				// 20120212100205.838,169.254.121.61,169.254.255.255,eth:ip:igmp,151,
				
				try {
					pcapSummary.clearFields();
					StringTokenizer tokenizer = new StringTokenizer(val.toString(),	COMMA);
					int tokencnt = tokenizer.countTokens();
					pcapSummary.setDtoi(format.parse(tokenizer.nextToken()));
					pcapSummary.setIpSrc(tokenizer.nextToken());
					pcapSummary.setIpDest(tokenizer.nextToken());
					pcapSummary.setProtocol(tokenizer.nextToken());
					pcapSummary.setLength(Integer.parseInt(tokenizer.nextToken()));
					if (tokencnt > 5) {
						//Might not get an info / url token
						pcapSummary.setInfo(tokenizer.nextToken());
					}
				} catch (Exception e) {
					log.error("Failed to parse pcap entry: " + val.toString(), e);
				}
				context.write(new Text(UUID.randomUUID().toString()), pcapSummary);
			}
		}
	}
	
//	public static class PcapSummarySequenceFileOutputFormat extends SequenceFileOutputFormat<Text, PcapSummary> {
//		@Override
//		protected String generateFileNameForKeyValue(Text key, PcapSummary value, String name) {
//			return name + key;
//		}
//	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: pcapsummary <load file dir> <dest dir>");
			System.exit(2);
		}
		Job job = new Job(conf, "pcapSummaryLoad");
		job.setJarByClass(PcapSummaryLoad.class);
		job.setMapperClass(DateMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
//		job.setCombinerClass(PcapSummaryReducer.class);
		job.setReducerClass(PcapSummaryReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(PcapSummary.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
