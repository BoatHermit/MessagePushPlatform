package com.boat.mpp.stream;

import com.boat.mpp.common.domain.AnchorInfo;
import com.boat.mpp.stream.constants.MppFlinkConstant;
import com.boat.mpp.stream.function.MppFlatMapFunction;
import com.boat.mpp.stream.sink.MppSink;
import com.boat.mpp.stream.utils.MessageQueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * flink启动类
 *
 * @author boat
 */
@Slf4j
public class MppBootStrap {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 1.获取KafkaConsumer
        KafkaSource<String> kafkaConsumer = MessageQueueUtils
                .getKafkaConsumer(MppFlinkConstant.TOPIC_NAME, MppFlinkConstant.GROUP_ID, MppFlinkConstant.BROKER);
        DataStreamSource<String> kafkaSource = env
                .fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), MppFlinkConstant.SOURCE_NAME);


        // 2.数据转换处理
        SingleOutputStreamOperator<AnchorInfo> dataStream = kafkaSource
                .flatMap(new MppFlatMapFunction()).name(MppFlinkConstant.FUNCTION_NAME);

        // 3. 将实时数据多维度写入Redis(已实现)，离线数据写入hive(未实现)
        dataStream.addSink(new MppSink()).name(MppFlinkConstant.SINK_NAME);
        env.execute(MppFlinkConstant.JOB_NAME);

    }
}
