package com.union.fmdouban.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.union.fmdouban.api.bean.FMChannel;
import com.union.fmdouban.api.bean.FMChannelType;
import com.union.fmdouban.api.bean.FMRichChannel;
import com.union.fmdouban.api.bean.FMSong;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by zhouxiaming on 16/3/21.
 */
public class FMParserFactory {
    private static Gson mGson = new Gson();
    private static JsonParser mParser = new JsonParser();

    /**
     * 解析频道列表
     * @param channelList
     * @return
     */
    public static List<FMChannel> parserToChannelList(String channelList) {
        JsonObject jsonElement = (JsonObject)mParser.parse(channelList);
        JsonArray channelArray = jsonElement.getAsJsonArray("channels");
        List<FMChannel> channels = mGson.fromJson(channelArray, new TypeToken<List<FMChannel>>(){}.getType());
        return channels;
    }

    /**
     * 解析PlayList
     * @param playList
     * @return
     */
    public static Queue<FMSong> parserToSongList(String playList) {
        JsonObject jsonElement = (JsonObject)mParser.parse(playList);
        int ret = jsonElement.get("r").getAsInt();
        if (ret != 0) {
            return null;
        }

        JsonArray jsonArray = jsonElement.getAsJsonArray("song");
        Queue<FMSong> songList = mGson.fromJson(jsonArray, new TypeToken<Queue<FMSong>>() {}.getType());

        return songList;
    }

    /**
     * 从html中解析 频道类别
     * @param htmlContent
     */
    public static List<FMChannelType> parserChannelTypeFromHtml(String htmlContent) {
        List<FMChannelType> typeList = new ArrayList<FMChannelType>();
        try {
            Parser parser = new Parser(htmlContent);
            parser.setEncoding("UTF-8");
            //解析频道类别
            NodeFilter filter = new NodeClassFilter(Bullet.class);
            NodeList nodeList = parser.parse(filter);
            Node[] nodes = nodeList.toNodeArray();
            for (Node node : nodes) {
                if (node.toString().contains("data-genre_id") && node.getParent().toString().contains("fm-side-taglist")) {
                    Node parent = node.getParent();
                    if (parent instanceof TagNode) {
                        TagNode tagNode = (TagNode) parent;
                        if ("fm-side-taglist clearfix".equals(tagNode.getAttribute("class"))) {
                            String id = ((TagNode) node).getAttribute("data-genre_id");
                            String name = ((Bullet) node).getChild(0).getText().replaceAll("&amp;", ".");
                            typeList.add(new FMChannelType(id, name));
                        }
                    }
                }
            }
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return typeList;
    }

    /**
     * 从html中解析热门频道列表
     * @param htmlContent
     * @return
     */
    public static List<FMRichChannel> parserChannelFromHtml(String htmlContent) {
        List<FMRichChannel> channelList = new ArrayList<FMRichChannel>();
        //解析热门频道
        try {
            Parser parser = new Parser(htmlContent);
            parser.setEncoding("UTF-8");
            NodeFilter scriptFilter = new NodeClassFilter(ScriptTag.class);
            NodeList scriptList = parser.parse(scriptFilter);
            Node[] scriptNodes = scriptList.toNodeArray();
            for (Node node : scriptNodes) {
                if (node.toString().contains("window.hot_channels_json")) {
                    String[] jsonArray = node.toString().split("window.");
                    for (String s : jsonArray) {
                        if (s.contains("channels_json")) {
                            String[] hot = s.split("=");
                            String key = hot[0];
                            String value = hot[1].replace(";", "").trim();
                            List<FMRichChannel> singleList = mGson.fromJson(value, new TypeToken<List<FMRichChannel>>(){}.getType());
                            if (singleList != null && singleList.size() > 0) {
                                channelList.addAll(singleList);
                            }
                        }
                    }
                }
            }
        } catch (ParserException e) {
            e.printStackTrace();
        }

        return channelList;
    }
}
