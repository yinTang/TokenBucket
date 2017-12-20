import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by yintang on 2017/12/20.
 */
public class Test {
    //限流测试
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(15);
        for (int i=0;i<15;i++){
            new Thread(() -> {
                try {
                    barrier.await();
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    // 创建httppost
                    HttpPost httppost = new HttpPost("http://localhost:8080/api/test1");
                    CloseableHttpResponse response = httpclient.execute(httppost);
                    System.out.println(EntityUtils.toString(response.getEntity()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }
}
