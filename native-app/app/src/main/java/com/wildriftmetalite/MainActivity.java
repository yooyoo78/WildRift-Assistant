package com.wildriftmetalite;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    private final List<String> roles = Arrays.asList("MID", "TOP", "JGL", "ADC", "SUP");
    private final List<Champion> champions = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private Spinner roleSpinner;
    private Spinner championSpinner;
    private Spinner enemySpinner;
    private Spinner enemyItemSpinner;
    private TextView recommendationView;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        seedData();
        buildNativeUi();
    }

    private void seedData() {
        champions.add(new Champion("Kai'Sa", "ADC", DamageType.MAGIC_PHYSICAL, 1.0, 1.0));
        champions.add(new Champion("Yasuo", "MID", DamageType.PHYSICAL, 0.9, 0.7));
        champions.add(new Champion("Ahri", "MID", DamageType.MAGIC, 0.8, 0.9));
        champions.add(new Champion("Malphite", "TOP", DamageType.MAGIC_PHYSICAL, 0.7, 1.2));
        champions.add(new Champion("Rammus", "JGL", DamageType.PHYSICAL, 0.5, 1.3));
        champions.add(new Champion("Morgana", "SUP", DamageType.MAGIC, 0.8, 1.0));

        items.add(new Item("Mortal Reminder", true, false, true, false, false, false, false, 0));
        items.add(new Item("Lord Dominik's Regards", false, true, false, false, false, false, false, 0));
        items.add(new Item("Maw of Malmortius", false, false, false, false, false, true, false, 0));
        items.add(new Item("Guardian Angel", false, false, false, false, false, false, true, 0));
        items.add(new Item("Black Cleaver", false, true, false, false, false, false, false, 0));
        items.add(new Item("Serpent's Fang", false, false, false, true, false, false, false, 0));
        items.add(new Item("Mercury's Treads", false, false, false, false, false, false, false, 0));
    }

    private void buildNativeUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(18), dp(18), dp(18));
        root.setBackgroundColor(Color.rgb(11, 13, 16));

        TextView title = text("WILD RIFT ASSISTANT", 24, Color.WHITE);
        root.addView(title, params());
        TextView subtitle = text("Native dashboard • Counter-aware Item Recommendation", 13, Color.LTGRAY);
        root.addView(subtitle, params());

        roleSpinner = spinner(roles.toArray(new String[0]));
        championSpinner = spinner(names(champions));
        enemySpinner = spinner(names(champions));
        enemyItemSpinner = spinner(itemNames(items));
        root.addView(label("YOUR ROLE")); root.addView(roleSpinner, params());
        root.addView(label("YOUR CHAMPION")); root.addView(championSpinner, params());
        root.addView(label("ENEMY CHAMPION / THREAT")); root.addView(enemySpinner, params());
        root.addView(label("ENEMY ITEM")); root.addView(enemyItemSpinner, params());

        TextView run = text("ANALYZE COUNTER BUILD", 15, Color.WHITE);
        run.setGravity(Gravity.CENTER);
        run.setPadding(0, dp(16), 0, dp(16));
        run.setBackgroundColor(Color.rgb(30, 90, 105));
        run.setOnClickListener(v -> updateRecommendation());
        root.addView(run, params());

        recommendationView = text("เลือกข้อมูลแล้วกด ANALYZE COUNTER BUILD", 15, Color.WHITE);
        recommendationView.setPadding(dp(4), dp(18), dp(4), dp(8));
        root.addView(recommendationView, params());

        ScrollView scroll = new ScrollView(this);
        scroll.addView(root);
        setContentView(scroll);
    }

    private void updateRecommendation() {
        Champion me = champions.get(championSpinner.getSelectedItemPosition());
        Champion enemy = champions.get(enemySpinner.getSelectedItemPosition());
        Item enemyItem = items.get(enemyItemSpinner.getSelectedItemPosition());
        List<ItemScore> scores = RecommendationEngine.recommend(me, enemy, enemyItem, items);
        StringBuilder out = new StringBuilder("RECOMMENDED ITEMS\n\n");
        for (int i = 0; i < Math.min(3, scores.size()); i++) {
            ItemScore s = scores.get(i);
            out.append(i + 1).append(". ").append(s.item.name)
               .append("  [score ").append(s.score).append("]\n")
               .append("   ").append(s.reason).append("\n\n");
        }
        out.append("Threats: ").append(enemy.damageType).append(", healing=").append(enemy.healingThreat)
           .append(", armor/MR pressure=").append(enemy.defensePressure).append("\n")
           .append("Enemy item analyzed: ").append(enemyItem.name);
        recommendationView.setText(out.toString());
    }

    private Spinner spinner(String[] values) {
        Spinner s = new Spinner(this);
        s.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, values));
        return s;
    }
    private TextView label(String s) { return text(s, 11, Color.rgb(120, 210, 220)); }
    private TextView text(String s, int size, int color) { TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color); return t; }
    private LinearLayout.LayoutParams params() { return new LinearLayout.LayoutParams(-1, -2); }
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }
    private String[] names(List<Champion> list) { String[] a = new String[list.size()]; for(int i=0;i<list.size();i++) a[i]=list.get(i).name; return a; }
    private String[] itemNames(List<Item> list) { String[] a = new String[list.size()]; for(int i=0;i<list.size();i++) a[i]=list.get(i).name; return a; }

    enum DamageType { PHYSICAL, MAGIC, MAGIC_PHYSICAL }
    static class Champion {
        final String name, role; final DamageType damageType; final double healingThreat, defensePressure;
        Champion(String name,String role,DamageType damageType,double healingThreat,double defensePressure){this.name=name;this.role=role;this.damageType=damageType;this.healingThreat=healingThreat;this.defensePressure=defensePressure;}
    }
    static class Item {
        final String name; final boolean antiHeal, armorPen, antiHealing, antiShield, attackSpeed, magicResist, revive; final double baseScore;
        Item(String name,boolean antiHeal,boolean armorPen,boolean antiHealing,boolean antiShield,boolean attackSpeed,boolean magicResist,boolean revive,double baseScore){this.name=name;this.antiHeal=antiHeal;this.armorPen=armorPen;this.antiHealing=antiHealing;this.antiShield=antiShield;this.attackSpeed=attackSpeed;this.magicResist=magicResist;this.revive=revive;this.baseScore=baseScore;}
    }
    static class ItemScore { final Item item; final double score; final String reason; ItemScore(Item item,double score,String reason){this.item=item;this.score=score;this.reason=reason;} }

    static class RecommendationEngine {
        static List<ItemScore> recommend(Champion me, Champion enemy, Item enemyItem, List<Item> catalog) {
            List<ItemScore> result = new ArrayList<>();
            for (Item item : catalog) {
                double score = item.baseScore;
                List<String> reasons = new ArrayList<>();
                if (enemy.healingThreat >= 0.8 && (item.antiHeal || item.antiHealing)) { score += 5; reasons.add("ลดผลการฮีล"); }
                if (enemy.defensePressure >= 1.0 && item.armorPen) { score += 4; reasons.add("รับมือเกราะสูง"); }
                if (enemy.damageType == DamageType.MAGIC && item.magicResist) { score += 5; reasons.add("รับมือ Magic damage"); }
                if (enemy.damageType == DamageType.PHYSICAL && item.armorPen) { score += 2; reasons.add("เจาะแนวรับกายภาพ"); }
                if (enemyItem.antiShield && item.antiShield) { score += 5; reasons.add("ตอบโต้ Shield"); }
                if (enemyItem.name.contains("Armor") && item.armorPen) { score += 3; reasons.add("ตอบโต้ไอเทมเกราะ"); }
                if (enemyItem.name.contains("Maw") && item.magicResist) { score += 2; reasons.add("ลดแรงกดดันเวท"); }
                if (me.name.equals("Kai'Sa") && item.armorPen) { score += 1; reasons.add("เข้ากับแผน Kai'Sa"); }
                if (reasons.isEmpty()) reasons.add("ตัวเลือกสถานการณ์");
                result.add(new ItemScore(item, score, String.join(" • ", reasons)));
            }
            result.sort((a,b) -> Double.compare(b.score,a.score));
            return result;
        }
    }
}
