package com.wildriftmetalite;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends Activity {
    private static final int BG = Color.rgb(9, 12, 16);
    private static final int PANEL = Color.rgb(18, 23, 30);
    private static final int PANEL_2 = Color.rgb(23, 29, 37);
    private static final int ACCENT = Color.rgb(72, 207, 220);
    private static final int TEXT = Color.rgb(245, 248, 250);
    private static final int MUTED = Color.rgb(155, 166, 178);
    private static final String DATA_PATCH = "7.2e-reference";
    private static final String DATA_SOURCE = "Wild Rift Core / Wild Rift Build / Wild Rift Meta / Wild Rift Guides";

    private final List<String> roles = Arrays.asList("MID", "TOP", "JGL", "ADC", "SUP");
    private final List<Champion> champions = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private SharedPreferences prefs;
    private LinearLayout root;
    private boolean thai = true;
    private int currentTab = 0;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("wr_assistant", MODE_PRIVATE);
        thai = prefs.getBoolean("thai", true);
        seedData();
        showTab(0);
    }

    private void seedData() {
        if (!champions.isEmpty()) return;
        champions.add(new Champion("Ahri", "MID", "MAGIC", "BURST,ROAM,AP", "นักเวทที่เน้น Burst และการ Roam"));
        champions.add(new Champion("Yasuo", "MID", "PHYSICAL", "DPS,CRIT,DUEL", "ไฟต์เตอร์ที่เด่นเมื่อมีพื้นที่เล่นและไอเทมคริต"));
        champions.add(new Champion("Akali", "MID", "MAGIC", "ASSASSIN,BURST,FLANK", "Assassin ที่ต้องระวังการเข้าถึงแนวหลัง"));
        champions.add(new Champion("Orianna", "MID", "MAGIC", "POKE,CONTROL,TEAMFIGHT", "Control mage ที่เก่งใน Teamfight"));
        champions.add(new Champion("Lux", "SUP", "MAGIC", "POKE,CC,BURST", "Poke และ CC ระยะไกล"));
        champions.add(new Champion("Morgana", "SUP", "MAGIC", "CC,SHIELD,AP", "มี CC และ Black Shield สำหรับตัดเวท"));
        champions.add(new Champion("Malphite", "TOP", "MAGIC", "TANK,ENGAGE,ARMOR", "Tank/Engage ที่กดดันไฟต์ด้วย Ultimate"));
        champions.add(new Champion("Darius", "TOP", "PHYSICAL", "DPS,HEAL,DUEL", "ไฟต์เตอร์เลนข้างที่ชนะการแลกยาว"));
        champions.add(new Champion("Rammus", "JGL", "PHYSICAL", "TANK,CC,ARMOR", "Tank ที่ตอบโต้ทีมกายภาพได้ดี"));
        champions.add(new Champion("Lee Sin", "JGL", "PHYSICAL", "EARLY,DUEL,ENGAGE", "Jungle ที่มีแรงกดดันช่วงต้นเกมสูง"));
        champions.add(new Champion("Kha'Zix", "JGL", "PHYSICAL", "ASSASSIN,BURST,ISOLATE", "Assassin ที่อันตรายเมื่อแยกเป้าหมาย"));
        champions.add(new Champion("Kai'Sa", "ADC", "MAGIC_PHYSICAL", "DPS,BURST,SCALING", "ADC ที่ผสมกายภาพและเวทและสเกลตามไอเทม"));
        champions.add(new Champion("Jinx", "ADC", "PHYSICAL", "DPS,TEAMFIGHT,SCALING", "ADC DPS สูงและเด่นในไฟต์ยาว"));
        champions.add(new Champion("Caitlyn", "ADC", "PHYSICAL", "POKE,RANGE,LANE", "ADC ระยะไกลที่กดเลนได้ดี"));
        champions.add(new Champion("Thresh", "SUP", "PHYSICAL", "CC,ENGAGE,UTILITY", "Support ที่มี CC และช่วยเพื่อนได้หลากหลาย"));
        champions.add(new Champion("Leona", "SUP", "PHYSICAL", "CC,ENGAGE,TANK", "Engage support ที่เน้นล็อกเป้าหมาย"));
        champions.add(new Champion("Garen", "TOP", "PHYSICAL", "TANK,DPS,HEAL", "ไฟต์เตอร์เล่นง่ายและแข็งแรงในเลน"));
        champions.add(new Champion("Vayne", "ADC", "PHYSICAL", "DPS,TRUE_DAMAGE,SCALING", "ADC ที่รับมือแนวหน้าด้วย True Damage"));
        champions.add(new Champion("Seraphine", "SUP", "MAGIC", "POKE,CC,TEAMFIGHT", "Utility mage ที่เด่นใน Teamfight"));
        champions.add(new Champion("Dr. Mundo", "TOP", "PHYSICAL", "TANK,HEAL,HP", "แนวหน้าพลังชีวิตสูงและฟื้นฟูได้มาก"));

        items.add(new Item("Mortal Reminder", "ANTI_HEAL,ARMOR_PEN", "ลดการฟื้นฟู + เจาะเกราะ"));
        items.add(new Item("Lord Dominik's Regards", "ARMOR_PEN", "เจาะเกราะสำหรับเป้าหมายแนวหน้า"));
        items.add(new Item("Maw of Malmortius", "MR,SHIELD", "ป้องกัน Burst เวท"));
        items.add(new Item("Guardian Angel", "REVIVE,ARMOR", "เพิ่มความปลอดภัยและโอกาสกลับเข้าไฟต์"));
        items.add(new Item("Black Cleaver", "ARMOR_PEN,HP", "ลดเกราะและเพิ่มความถึก"));
        items.add(new Item("Serpent's Fang", "ANTI_SHIELD", "ตัดประสิทธิภาพ Shield"));
        items.add(new Item("Mercury's Treads", "MR,CC_RESIST", "ต้านเวทและลดผล CC"));
        items.add(new Item("Plated Steelcaps", "ARMOR,BASIC_ATTACK", "ลดแรงกดดันจากการโจมตีกายภาพ"));
        items.add(new Item("Spirit Visage", "MR,HEAL", "เพิ่มความถึกเวทและการฟื้นฟู"));
        items.add(new Item("Executioner's Calling", "ANTI_HEAL", "ทางเลือกต้นเกมเพื่อลดการฮีล"));
        items.add(new Item("Serylda's Grudge", "ARMOR_PEN,SLOW", "เจาะเกราะและเพิ่มการควบคุมด้วย Slow"));
    }

    private void showTab(int tab) {
        currentTab = tab;
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);
        setContentView(root);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(12), dp(16), dp(92));
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        if (tab == 0) home(content);
        else if (tab == 1) counter(content);
        else if (tab == 2) build(content);
        else champions(content);
        root.addView(bottomNav());
    }

    private void home(LinearLayout c) {
        header(c, "LOL: ASSISTANTS", thai ? "OFFLINE-FIRST • NATIVE ANDROID" : "OFFLINE-FIRST • NATIVE ANDROID");
        card(c, "PHASE 2", thai ? "Counter แบบทีม 5 คน + Role-aware filtering + Threat-aware item scoring" : "5-enemy team counter + role-aware filtering + threat-aware item scoring");
        add(c, section(c, thai ? "เครื่องมือหลัก" : "CORE TOOLS"));
        add(c, actionCard("COUNTER", thai ? "แก้ทางแชมป์ + ทีม + ไอเทม" : "Champion + team + item counters", 1));
        add(c, actionCard("BUILD", thai ? "แนะนำไอเทมตามภัยคุกคาม" : "Threat-aware item recommendations", 2));
        add(c, actionCard("CHAMPIONS", thai ? "ค้นหาและดูรายละเอียดแชมป์" : "Search and inspect champions", 3));
        add(c, section(c, thai ? "สถานะระบบ" : "SYSTEM"));
        TextView status = text("✓ Native UI\n✓ Offline data\n✓ Team counter 5 slots\n✓ Role-aware filtering\n✓ CounterEngine " + CounterEngine.VERSION + "\n✓ TH/EN switch\n✓ No WebView\n\nData: " + DATA_PATCH + "\nSources: " + DATA_SOURCE, 13, MUTED);
        cardView(c, status);
        Button lang = button(thai ? "ภาษา: ไทย  →  English" : "Language: English  →  ไทย");
        lang.setOnClickListener(v -> { thai = !thai; prefs.edit().putBoolean("thai", thai).apply(); showTab(0); });
        add(c, lang);
    }

    private void counter(LinearLayout c) {
        header(c, "COUNTER CENTER", thai ? "วิเคราะห์ทีมศัตรูครบ 5 ช่อง" : "Analyze the full 5-player enemy team");
        add(c, fieldLabel(thai ? "ตำแหน่งของเรา" : "YOUR ROLE"));
        Spinner role = spinner(roles.toArray(new String[0])); add(c, role);
        add(c, fieldLabel(thai ? "แชมป์ของเรา" : "YOUR CHAMPION"));
        Spinner me = spinner(championNamesForRole("MID")); add(c, me);
        role.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedRole = roles.get(position);
                setSpinnerValues(me, championNamesForRole(selectedRole));
            }
        });

        add(c, section(c, thai ? "ทีมศัตรู • 5 ช่อง • ห้ามซ้ำ" : "ENEMY TEAM • 5 SLOTS • NO DUPLICATES"));
        Spinner[] enemyRole = new Spinner[5];
        Spinner[] enemyPick = new Spinner[5];
        for (int i = 0; i < 5; i++) {
            add(c, fieldLabel((thai ? "ศัตรู " : "ENEMY ") + (i + 1) + " • ROLE"));
            enemyRole[i] = spinner(roles.toArray(new String[0]));
            enemyRole[i].setSelection(i < roles.size() ? i : 0);
            add(c, enemyRole[i]);
            add(c, fieldLabel(thai ? "แชมป์" : "CHAMPION"));
            enemyPick[i] = spinner(championNamesForRole(roles.get(enemyRole[i].getSelectedItemPosition())));
            add(c, enemyPick[i]);
            final int slot = i;
            enemyRole[i].setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                    setSpinnerValues(enemyPick[slot], championNamesForRole(roles.get(position)));
                    ensureNoDuplicate(enemyPick, slot);
                }
            });
            enemyPick[i].setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                    ensureNoDuplicate(enemyPick, slot);
                }
            });
        }

        add(c, fieldLabel(thai ? "ไอเทมศัตรูที่ต้องการแก้" : "ENEMY ITEM TO COUNTER"));
        Spinner enemyItem = spinner(itemNames(items)); add(c, enemyItem);
        add(c, fieldLabel(thai ? "ภัยคุกคามเพิ่มเติม" : "ADDITIONAL TEAM THREATS"));
        EditText threats = edit(thai ? "เช่น heal, shield, AP burst, tank, assassin, CC" : "e.g. heal, shield, AP burst, tank, assassin, CC"); add(c, threats);
        Button analyze = button(thai ? "วิเคราะห์ COUNTER BUILD" : "ANALYZE COUNTER BUILD"); add(c, analyze);
        TextView result = text("", 14, TEXT); cardView(c, result);

        analyze.setOnClickListener(v -> {
            Set<String> unique = new HashSet<>();
            List<Champion> enemies = new ArrayList<>();
            boolean duplicate = false;
            for (Spinner pick : enemyPick) {
                String n = String.valueOf(pick.getSelectedItem());
                if (!unique.add(n)) duplicate = true;
                Champion found = findChampion(n);
                if (found != null) enemies.add(found);
            }
            if (duplicate) {
                result.setText(thai ? "กรุณาเลือกแชมป์ศัตรูไม่ให้ซ้ำกันทั้ง 5 ช่อง" : "Please choose five different enemy champions.");
                return;
            }
            Champion our = findChampion(String.valueOf(me.getSelectedItem()));
            Item selectedEnemyItem = items.get(enemyItem.getSelectedItemPosition());
            CounterEngine.ThreatProfile profile = new CounterEngine.ThreatProfile();
            StringBuilder threatText = new StringBuilder(threats.getText().toString());
            for (Champion e : enemies) threatText.append(' ').append(e.tags).append(' ').append(e.damageType).append(' ').append(e.name);
            threatText.append(' ').append(selectedEnemyItem.tags);
            profile.absorb(threatText.toString());
            boolean mixed = false;
            for (Champion e : enemies) if (e.damageType.contains("MAGIC_PHYSICAL")) mixed = true;

            List<String[]> catalog = new ArrayList<>();
            for (Item item : items) catalog.add(new String[]{item.name, item.tags});
            List<CounterEngine.Candidate> ranked = CounterEngine.rank(catalog, profile, mixed);

            StringBuilder s = new StringBuilder();
            s.append(thai ? "ผลวิเคราะห์ทีม\n\n" : "TEAM ANALYSIS\n\n");
            s.append(thai ? "เรา: " : "US: ").append(our == null ? "-" : our.name).append("\n");
            s.append(thai ? "ศัตรู: " : "ENEMIES: ");
            for (int i = 0; i < enemies.size(); i++) {
                if (i > 0) s.append(" • ");
                s.append(enemies.get(i).name);
            }
            s.append("\n\n");
            s.append(thai ? "Threat Profile: " : "Threat Profile: ");
            s.append(threatFlags(profile)).append("\n\n");
            s.append(thai ? "ไอเทมที่เข้ากับภัยรวม:\n" : "ITEMS MATCHING TEAM THREATS:\n");
            for (int i = 0; i < Math.min(6, ranked.size()); i++) {
                CounterEngine.Candidate x = ranked.get(i);
                s.append(i + 1).append(". ").append(x.name).append(" • ").append(String.format(Locale.US, "%.1f", x.score)).append("\n");
                if (!x.reasons.isEmpty()) s.append("   ").append(String.join(" + ", x.reasons)).append("\n");
            }
            s.append("\n").append(thai ? "แผนการเล่น:\n" : "GAMEPLAY PLAN:\n");
            s.append(teamPlayTip(enemies));
            s.append("\n\n").append(thai ? "ข้อมูล: " : "DATA: ").append(DATA_PATCH).append(" • ").append(DATA_SOURCE);
            result.setText(s.toString());
        });
    }

    private void build(LinearLayout c) {
        header(c, "BUILD LAB", thai ? "เริ่มจากแชมป์ แล้วปรับตามภัยจริง" : "Start from your champion, then adapt to threats");
        add(c, fieldLabel(thai ? "แชมป์" : "CHAMPION"));
        Spinner me = spinner(names(champions)); add(c, me);
        add(c, fieldLabel(thai ? "ภัยหลัก" : "THREAT"));
        Spinner threat = spinner(new String[]{"AP Burst", "AD/Auto Attack", "Heavy Healing", "Armor Stack", "Shield", "Assassin"}); add(c, threat);
        TextView output = text("", 14, TEXT); cardView(c, output);
        Button b = button(thai ? "สร้าง BUILD แนะนำ" : "GENERATE BUILD"); add(c, b);
        b.setOnClickListener(v -> {
            String x = threat.getSelectedItem().toString();
            CounterEngine.ThreatProfile p = new CounterEngine.ThreatProfile();
            p.absorb(x);
            List<String[]> catalog = new ArrayList<>();
            for (Item item : items) catalog.add(new String[]{item.name, item.tags});
            List<CounterEngine.Candidate> ranked = CounterEngine.rank(catalog, p, false);
            StringBuilder s = new StringBuilder(thai ? "BUILD แนะนำ\n\n" : "RECOMMENDED BUILD\n\n");
            s.append(me.getSelectedItem()).append("\n\n");
            for (int i = 0; i < Math.min(4, ranked.size()); i++) {
                CounterEngine.Candidate r = ranked.get(i);
                s.append(i + 1).append(". ").append(r.name).append("\n");
            }
            s.append("\n").append(thai ? "หลักการ: ปรับไอเทมตามภัยของเกม ไม่ยึด build preset แบบตายตัว" : "Principle: adapt items to the game threat instead of blindly following a fixed preset.");
            output.setText(s.toString());
        });
    }

    private void champions(LinearLayout c) {
        header(c, thai ? "CHAMPION DATABASE" : "CHAMPION DATABASE", thai ? "ข้อมูลแชมป์ออฟไลน์ + Role" : "Offline champion data + roles");
        EditText search = edit(thai ? "ค้นหา Ahri, Kai'Sa, MID..." : "Search Ahri, Kai'Sa, MID..."); add(c, search);
        ListView list = new ListView(this); list.setDivider(null); list.setBackgroundColor(BG); add(c, list, new LinearLayout.LayoutParams(-1, dp(430)));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, championRows(champions));
        list.setAdapter(adapter);
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s,int st,int c,int a){}
            public void onTextChanged(CharSequence s,int st,int b,int c2){ adapter.getFilter().filter(s); }
            public void afterTextChanged(Editable e){}
        });
        list.setOnItemClickListener((p,v,pos,id) -> {
            String name = ((TextView)v).getText().toString().split(" • ")[0];
            Champion x = findChampion(name);
            if (x != null) championDetail(c, x);
        });
    }

    private void championDetail(LinearLayout c, Champion x) {
        add(c, section(c, x.name));
        TextView d = text(x.role + "  •  " + x.damageType + "\n" + x.description + "\n\nTags: " + x.tags + "\n\nData: " + x.patch + " • " + x.source, 14, TEXT);
        cardView(c, d);
        Button fav = button(favorites().contains(x.name) ? (thai ? "★ ลบจากรายการโปรด" : "★ Remove favorite") : (thai ? "☆ เพิ่มรายการโปรด" : "☆ Add favorite"));
        add(c, fav);
        fav.setOnClickListener(v -> { Set<String> f = new HashSet<>(favorites()); if (f.contains(x.name)) f.remove(x.name); else f.add(x.name); prefs.edit().putStringSet("favorites", f).apply(); showTab(3); });
    }

    private void ensureNoDuplicate(Spinner[] picks, int changed) {
        String selected = String.valueOf(picks[changed].getSelectedItem());
        for (int i = 0; i < picks.length; i++) {
            if (i == changed) continue;
            if (selected.equals(String.valueOf(picks[i].getSelectedItem()))) {
                for (int j = 0; j < picks[i].getCount(); j++) {
                    String candidate = String.valueOf(picks[i].getItemAtPosition(j));
                    if (!candidate.equals(selected) && !isSelectedByOther(picks, i, candidate)) {
                        picks[i].setSelection(j);
                        return;
                    }
                }
            }
        }
    }

    private boolean isSelectedByOther(Spinner[] picks, int skip, String candidate) {
        for (int i = 0; i < picks.length; i++) if (i != skip && candidate.equals(String.valueOf(picks[i].getSelectedItem()))) return true;
        return false;
    }

    private void setSpinnerValues(Spinner spinner, String[] values) {
        String previous = String.valueOf(spinner.getSelectedItem());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, values);
        spinner.setAdapter(adapter);
        for (int i = 0; i < values.length; i++) if (values[i].equals(previous)) { spinner.setSelection(i); return; }
        if (values.length > 0) spinner.setSelection(0);
    }

    private String threatFlags(CounterEngine.ThreatProfile p) {
        List<String> f = new ArrayList<>();
        if (p.healing) f.add("HEAL");
        if (p.shields) f.add("SHIELD");
        if (p.magicBurst) f.add("AP/MAGIC");
        if (p.physicalBurst) f.add("AD/PHYSICAL");
        if (p.armorStack) f.add("ARMOR");
        if (p.magicResist) f.add("MR");
        if (p.crowdControl) f.add("CC");
        if (p.assassin) f.add("ASSASSIN");
        if (p.frontline) f.add("FRONTLINE");
        if (p.autoAttack) f.add("AUTO");
        if (p.mixedDamage) f.add("MIXED");
        return f.isEmpty() ? "SITUATIONAL" : String.join(" • ", f);
    }

    private String teamPlayTip(List<Champion> enemies) {
        boolean assassin = false, tank = false, heal = false, cc = false;
        for (Champion e : enemies) {
            assassin |= e.tags.contains("ASSASSIN");
            tank |= e.tags.contains("TANK") || e.tags.contains("ARMOR");
            heal |= e.tags.contains("HEAL");
            cc |= e.tags.contains("CC");
        }
        List<String> tips = new ArrayList<>();
        if (assassin) tips.add(thai ? "รักษา Vision และเก็บ escape cooldown ไว้รับจังหวะล้วง" : "Maintain vision and hold escape cooldowns for assassin entry.");
        if (heal) tips.add(thai ? "ลดการฟื้นฟูก่อนการแลกยาว" : "Apply anti-heal before extended trades.");
        if (tank) tips.add(thai ? "อย่าเท cooldown ทั้งหมดใส่แนวหน้าโดยไม่มี priority target" : "Do not spend every cooldown on the frontline without a priority target.");
        if (cc) tips.add(thai ? "กระจายตัวและหลีกเลี่ยงการโดน CC หลักพร้อมกัน" : "Spread out and avoid taking the primary CC chain together.");
        if (tips.isEmpty()) tips.add(thai ? "เล่นตาม wave, vision และจังหวะ objective" : "Play around wave state, vision and objective timing.");
        return String.join("\n", tips);
    }

    private Champion findChampion(String name) {
        for (Champion x : champions) if (x.name.equals(name)) return x;
        return null;
    }

    private List<String> favorites() { return new ArrayList<>(prefs.getStringSet("favorites", new HashSet<String>())); }
    private List<String> championRows(List<Champion> list) { List<String> out=new ArrayList<>(); for(Champion x:list) out.add(x.name+" • "+x.role+" • "+x.damageType); return out; }
    private String[] names(List<Champion> list){String[] a=new String[list.size()];for(int i=0;i<list.size();i++)a[i]=list.get(i).name;return a;}
    private String[] itemNames(List<Item> list){String[] a=new String[list.size()];for(int i=0;i<list.size();i++)a[i]=list.get(i).name;return a;}
    private String[] championNamesForRole(String role) { List<Champion> filtered = new ArrayList<>(); for (Champion x : champions) if (x.role.equals(role)) filtered.add(x); return names(filtered); }

    private LinearLayout bottomNav(){LinearLayout n=new LinearLayout(this);n.setPadding(dp(8),dp(7),dp(8),dp(7));n.setBackgroundColor(Color.rgb(14,18,24));String[] labels=thai?new String[]{"หน้าหลัก","Counter","Build","Champions"}:new String[]{"Home","Counter","Build","Champions"};for(int i=0;i<4;i++){final int tab=i;Button b=button(labels[i]);b.setTextSize(11);b.setBackgroundColor(i==currentTab?Color.rgb(35,72,78):Color.TRANSPARENT);b.setOnClickListener(v->showTab(tab));n.addView(b,new LinearLayout.LayoutParams(0,dp(52),1));}return n;}
    private void header(LinearLayout c,String a,String b){TextView t=text(a,26,TEXT);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);add(c,t);TextView s=text(b,13,MUTED);add(c,s);add(c,space(10));}
    private TextView section(LinearLayout c,String s){TextView t=text(s,16,TEXT);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setPadding(0,dp(10),0,dp(8));return t;}
    private TextView fieldLabel(String s){return text(s,11,ACCENT);}
    private TextView text(String s,int size,int color){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setLineSpacing(0,1.12f);return t;}
    private TextView space(int h){TextView t=new TextView(this);t.setHeight(dp(h));return t;}
    private Button button(String s){Button b=new Button(this);b.setText(s);b.setTextColor(TEXT);b.setTextSize(13);b.setAllCaps(false);b.setBackground(round(PANEL_2,14));return b;}
    private EditText edit(String hint){EditText e=new EditText(this);e.setHint(hint);e.setHintTextColor(MUTED);e.setTextColor(TEXT);e.setSingleLine(true);e.setPadding(dp(14),0,dp(14),0);e.setBackground(round(PANEL_2,14));e.setMinHeight(dp(52));return e;}
    private Spinner spinner(String[] values){Spinner s=new Spinner(this);ArrayAdapter<String>a=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,values);s.setAdapter(a);s.setBackground(round(PANEL_2,12));s.setMinimumHeight(dp(50));return s;}
    private TextView actionCard(String title,String sub,int tab){TextView t=text(title+"\n"+sub,15,TEXT);t.setPadding(dp(16),dp(14),dp(16),dp(14));t.setBackground(round(PANEL,16));t.setOnClickListener(v->showTab(tab));return t;}
    private void card(LinearLayout c,String title,String body){TextView t=text(title+"\n\n"+body,14,TEXT);t.setPadding(dp(16),dp(16),dp(16),dp(16));cardView(c,t);}
    private void cardView(LinearLayout c,View v){v.setBackground(round(PANEL,16));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,dp(7),0,dp(7));c.addView(v,p);}
    private void add(LinearLayout c,View v){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,dp(4),0,dp(4));c.addView(v,p);}
    private GradientDrawable round(int color,int r){GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(dp(r));g.setStroke(dp(1),Color.rgb(38,47,58));return g;}
    private int dp(int v){return(int)(v*getResources().getDisplayMetrics().density+0.5f);}

    static class Champion{
        final String name,role,damageType,tags,description,patch,source;
        Champion(String n,String r,String d,String t,String de){name=n;role=r;damageType=d;tags=t;description=de;patch=DATA_PATCH;source=DATA_SOURCE;}
    }
    static class Item{final String name,tags,description;Item(String n,String t,String d){name=n;tags=t;description=d;}}
}
