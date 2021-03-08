package dev.cosgy.JMusicBot.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class MessageTranslator {
    static Logger log = LoggerFactory.getLogger("MessageTranslator");
    private static String langFile = "lang.csgdev";
    private static HashMap<String, String> langData = new HashMap<>();

    public static void LoadLangFail(){
        log.info("言語ファイルの読み込み開始");
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
        InputStream res = MessageTranslator.class.getClassLoader().getResourceAsStream(langFile);
        File file;
        byte[] bytes = null;
        try {
            bytes = res.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            langData = objectMapper.readValue(bytes, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("言語ファイルの読み込み完了");
    }

    public static String ReplaceText(String message){
        String msg;

        msg = langData.get(message);

        return msg == null ? message : msg;
    }
}
