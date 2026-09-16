package com.wildriftmetalite;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Loads the bundled offline snapshot without network access. */
public final class OfflineDataRepository {
    public static final String ROOT = "data/";

    public static final class Metadata {
        public final String patch, updatedAt, coverage;
        public Metadata(String patch, String updatedAt, String coverage) { this.patch=patch; this.updatedAt=updatedAt; this.coverage=coverage; }
    }
    public static final class ChampionRecord {
        public final String id,name,roles,damageType,tags,description;
        public ChampionRecord(String id,String name,String roles,String damageType,String tags,String description){this.id=id;this.name=name;this.roles=roles;this.damageType=damageType;this.tags=tags;this.description=description;}
    }
    public static final class ItemRecord {
        public final String id,name,category,tags,description;
        public ItemRecord(String id,String name,String category,String tags,String description){this.id=id;this.name=name;this.category=category;this.tags=tags;this.description=description;}
    }
    public static final class RuneRecommendation {
        public final String championId,role,runes,source;
        public RuneRecommendation(String championId,String role,String runes,String source){this.championId=championId;this.role=role;this.runes=runes;this.source=source;}
    }

    private OfflineDataRepository() {}

    public static Metadata manifest(Context context){
        try { JSONObject o=readObject(context,ROOT+"data_manifest.json"); return new Metadata(o.optString("patch","unknown"),o.optString("updatedAt","unknown"),o.optString("coverage","unknown")); }
        catch(Exception e){return new Metadata("unknown","unknown","unavailable");}
    }
    public static List<ChampionRecord> champions(Context context)throws Exception{
        JSONArray rows=readObject(context,ROOT+"champions.json").getJSONArray("champions"); List<ChampionRecord> out=new ArrayList<>();
        for(int i=0;i<rows.length();i++){JSONObject o=rows.getJSONObject(i);out.add(new ChampionRecord(o.optString("id"),o.optString("name"),join(o.optJSONArray("roles")),o.optString("damageType"),join(o.optJSONArray("tags")),o.optString("description")));} return out;
    }
    public static List<ItemRecord> items(Context context)throws Exception{
        JSONArray rows=readObject(context,ROOT+"items.json").getJSONArray("items"); List<ItemRecord> out=new ArrayList<>();
        for(int i=0;i<rows.length();i++){JSONObject o=rows.getJSONObject(i);out.add(new ItemRecord(o.optString("id"),o.optString("name"),o.optString("category"),join(o.optJSONArray("tags")),o.optString("description")));} return out;
    }
    public static List<RuneRecommendation> runeRecommendations(Context context)throws Exception{
        JSONArray rows=readObject(context,ROOT+"rune_recommendations.json").getJSONArray("recommendations"); List<RuneRecommendation> out=new ArrayList<>();
        for(int i=0;i<rows.length();i++){JSONObject o=rows.getJSONObject(i);out.add(new RuneRecommendation(o.optString("championId"),o.optString("role"),join(o.optJSONArray("runes")),o.optString("source")));} return out;
    }
    private static String join(JSONArray a){if(a==null)return "";StringBuilder s=new StringBuilder();for(int i=0;i<a.length();i++){if(i>0)s.append(',');s.append(a.optString(i));}return s.toString();}
    private static JSONObject readObject(Context context,String path)throws Exception{InputStream in=context.getAssets().open(path);try(BufferedReader r=new BufferedReader(new InputStreamReader(in,StandardCharsets.UTF_8))){StringBuilder s=new StringBuilder();String line;while((line=r.readLine())!=null)s.append(line);return new JSONObject(s.toString());}}
}
