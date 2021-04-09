package bot.java.lambda.apis;

import bot.java.lambda.config.Config;
import me.duncte123.botcommons.web.WebUtils;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopGG {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopGG.class);

    public static void postServerCount(long serverCount) {
        String postUrl = "https://top.gg/api//bots/752052866809593906/stats";

        RequestBody body = new FormBody.Builder()
                .add("server_count", serverCount + "")
                .build();

        Request request = WebUtils.defaultRequest()
                .post(body)
                .addHeader("Authorization", Config.get("TopGG_Token"))
                .url(postUrl)
                .build();

        WebUtils.ins.prepareRaw(request, r -> r).async(
                response -> {
                    if (response.isSuccessful()) {
                        LOGGER.info("Posted Server Count to top.gg");
                    } else if (response.code() == 429) {
                        LOGGER.info("Rate Limit on top.gg : {}", response.message());
                    } else {
                        LOGGER.info("Couldn't Post Server Count on top.gg : {}", response.message());
                    }
                }
        );
    }
}
