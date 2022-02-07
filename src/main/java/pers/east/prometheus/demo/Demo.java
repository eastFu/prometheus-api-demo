package pers.east.prometheus.demo;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;

import java.util.Random;

public class Demo {

    public void readData(){

    }

    public void writeData(){

    }


    public static void main(String[] args) {
        try {

            Counter counter = Counter.build()
                    .name("blog_visit")
                    .labelNames("blog_id")
                    .help("counter_blog_visit")
                    .register();
            //注：通常只能注册1次，1个实例中重复注册会报错

            Gauge gauge = Gauge.build()
                    .name("blog_fans")
                    .help("gauge_blog_fans")
                    .register();

            PushGateway gateway = new PushGateway("49.233.209.196:9091");
            Random rnd = new Random();

            //粉丝数先预设50
            gauge.inc(50);
            while (true) {
                //随机生成1个blogId
                int blogId = rnd.nextInt(100000);
                //该blogId的访问量+1
                counter.labels(blogId + "").inc();
                //模拟粉丝数的变化
                if (blogId % 2 == 0) {
                    gauge.inc();
                } else {
                    gauge.dec();
                }
                //利用网关采集数据
                gateway.push(counter, "job-counter-test");
                gateway.push(gauge, "job-gauge-test");

                //辅助输出日志
                System.out.println("blogId:" + blogId);
                Thread.sleep(5000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
