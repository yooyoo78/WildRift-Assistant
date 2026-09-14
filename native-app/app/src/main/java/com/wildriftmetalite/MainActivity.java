package com.wildriftmetalite;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Set;

public class MainActivity extends Activity {
    private static final int BG = Color.rgb(9, 12, 16);
    private static final int PANEL = Color.rgb(18, 23, 30);
    private static final int PANEL_2 = Color.rgb(23, 29, 37);
    private static final int ACCENT = Color.rgb(72, 207, 220);
    private static final int TEXT = Color.rgb(245, 248, 250);
    private static final int MUTED = Color.rgb(155, 166, 178);

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
        items.add(new Item("Amumu's Counterweight", "ARMOR,HP", "ไอเทมตัวอย่างสำหรับแนวหน้า"));
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
        header(c, "WILD RIFT", "ASSISTANT");
        card(c, "Offline-first • Native Android", thai ? "ศูนย์ช่วยตัดสินใจสำหรับเกมจริง ใช้ข้อมูลในเครื่อง ไม่ต้องเปิด WebView" : "A native offline-first companion for real match decisions.");
        TextView h = section(c, thai ? "เครื่องมือหลัก" : "CORE TOOLS");
        add(c, h);
        add(c, actionCard("COUNTER", thai ? "แก้ทางแชมป์ + ทีม + ไอเทม" : "Champion + team + item counters", 1));
        add(c, actionCard("BUILD", thai ? "แนะนำไอเทมตามภัยคุกคาม" : "Threat-aware item recommendations", 2));
        add(c, actionCard("CHAMPIONS", thai ? "ค้นหาและดูรายละเอียดแชมป์" : "Search and inspect champions", 3));
        add(c, section(c, thai ? "สถานะระบบ" : "SYSTEM"));
        TextView status = text((thai ? "✓ Native UI\n✓ Offline data\n✓ Counter engine\n✓ TH/EN switch\n✓ No WebView" : "✓ Native UI\n✓ Offline data\n✓ Counter engine\n✓ TH/EN switch\n✓ No WebView"), 14, MUTED);
        cardView(c, status);
        Button lang = button(thai ? "ภาษา: ไทย  →  English" : "Language: English  →  ไทย");
        lang.setOnClickListener(v -> { thai = !thai; prefs.edit().putBoolean("thai", thai).apply(); showTab(0); });
        add(c, lang);
    }

    private void counter(LinearLayout c) {
        header(c, thai ? "COUNTER CENTER" : "COUNTER CENTER", thai ? "แก้ทางแบบครบระบบ" : "Full counter system");
        add(c, fieldLabel(thai ? "ตำแหน่ง" : "ROLE"));
        Spinner role = spinner(roles.toArray(new String[0])); add(c, role);
        add(c, fieldLabel(thai ? "แชมป์ของเรา" : "YOUR CHAMPION"));
        Spinner me = spinner(names(champions)); add(c, me);
        add(c, fieldLabel(thai ? "แชมป์ศัตรูหลัก" : "MAIN ENEMY"));
        Spinner enemy = spinner(names(champions)); add(c, enemy);
        add(c, fieldLabel(thai ? "ไอเทมศัตรูที่ต้องการแก้" : "ENEMY ITEM"));
        Spinner enemyItem = spinner(itemNames(items)); add(c, enemyItem);
        add(c, section(c, thai ? "ภัยคุกคามในทีมศัตรู" : "ENEMY TEAM THREATS"));
        EditText threats = edit(thai ? "เช่น heal, shield, AP burst, tank, assassin" : "e.g. heal, shield, AP burst, tank, assassin"); add(c, threats);
        Button analyze = button(thai ? "วิเคราะห์ COUNTER BUILD" : "ANALYZE COUNTER BUILD"); add(c, analyze);
        TextView result = text("", 14, TEXT); cardView(c, result);
        analyze.setOnClickListener(v -> {
            Champion a = champions.get(me.getSelectedItemPosition());
            Champion e = champions.get(enemy.getSelectedItemPosition());
            Item ei = items.get(enemyItem.getSelectedItemPosition());
            String t = threats.getText().toString().toLowerCase();
            List<ItemScore> scores = recommend(a, e, ei, t);
            StringBuilder s = new StringBuilder();
            s.append(thai ? "ผลวิเคราะห์\n\n" : "ANALYSIS\n\n");
            s.append(thai ? "คู่เลน: " : "MATCHUP: ").append(a.name).append(" vs ").append(e.name).append("\n");
            s.append(thai ? "ภัยหลัก: " : "MAIN THREAT: ").append(e.damageType).append(" • ").append(e.tags).append("\n\n");
            for (int i = 0; i < Math.min(5, scores.size()); i++) {
                ItemScore x = scores.get(i);
                s.append(i + 1).append(". ").append(x.item.name).append("  •  ").append(x.score).append("\n");
                s.append("   ").append(x.reason).append("\n");
            }
            s.append("\n").append(thai ? "วิธีเล่น: " : "PLAY TIP: ");
            s.append(playTip(a, e));
            result.setText(s.toString());
        });
    }

    private void build(LinearLayout c) {
        header(c, thai ? "BUILD LAB" : "BUILD LAB", thai ? "เริ่มจากแชมป์ แล้วปรับตามศัตรู" : "Start from your champion, then adapt to threats");
        add(c, fieldLabel(thai ? "แชมป์" : "CHAMPION"));
        Spinner me = spinner(names(champions)); add(c, me);
        add(c, fieldLabel(thai ? "ภัยหลัก" : "THREAT"));
        Spinner threat = spinner(new String[]{"AP Burst", "AD/Auto Attack", "Heavy Healing", "Armor Stack", "Shield", "Assassin"}); add(c, threat);
        TextView output = text("", 14, TEXT); cardView(c, output);
        Button b = button(thai ? "สร้าง BUILD แนะนำ" : "GENERATE BUILD"); add(c, b);
        b.setOnClickListener(v -> {
            String x = threat.getSelectedItem().toString();
            StringBuilder s = new StringBuilder(thai ? "BUILD แนะนำ\n\n" : "RECOMMENDED BUILD\n\n");
            s.append(me.getSelectedItem()).append("\n\n");
            if (x.contains("AP")) s.append("1. Maw of Malmortius / Mercury's Treads\n2. Guardian Angel\n3. Core damage item\n\n");
            else if (x.contains("AD")) s.append("1. Plated Steelcaps\n2. Guardian Angel\n3. Core damage item\n\n");
            else if (x.contains("Healing")) s.append("1. Mortal Reminder / Executioner's Calling\n2. Core damage item\n3. Defensive slot\n\n");
            else if (x.contains("Armor")) s.append("1. Lord Dominik's Regards / Serylda's Grudge\n2. Black Cleaver\n3. Core damage item\n\n");
            else if (x.contains("Shield")) s.append("1. Serpent's Fang\n2. Core damage item\n3. Guardian Angel\n\n");
            else s.append("1. Guardian Angel\n2. Mercury's Treads or defensive slot\n3. Core damage item\n\n");
            s.append(thai ? "หมายเหตุ: นี่คือระบบตัดสินใจออฟไลน์ ไม่ใช่ meta สด" : "Note: this is offline decision support, not live meta data.");
            output.setText(s.toString());
        });
    }

    private void champions(LinearLayout c) {
        header(c, thai ? "CHAMPION DATABASE" : "CHAMPION DATABASE", thai ? "ค้นหาแชมป์จากข้อมูลออฟไลน์" : "Search the offline champion dataset");
        EditText search = edit(thai ? "ค้นหา Ahri, Kai'Sa, MID..." : "Search Ahri, Kai'Sa, MID..."); add(c, search);
        ListView list = new ListView(this); list.setDivider(null); list.setBackgroundColor(BG); add(c, list, new LinearLayout.LayoutParams(-1, dp(430)));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, championRows(champions));
        list.setAdapter(adapter);
        search.addTextChangedListener(new TextWatcher() { public void beforeTextChanged(CharSequence s,int st,int c,int a){} public void onTextChanged(CharSequence s,int st,int b,int c2){ adapter.getFilter().filter(s); } public void afterTextChanged(Editable e){} });
        list.setOnItemClickListener((p,v,pos,id) -> {
            String name = ((TextView)v).getText().toString().split(" • ")[0];
            for (Champion x : champions) if (x.name.equals(name)) { championDetail(c, x); break; }
        });
    }

    private void championDetail(LinearLayout c, Champion x) {
        add(c, section(c, x.name));
        TextView d = text(x.role + "  •  " + x.damageType + "\n" + x.description + "\n\nTags: " + x.tags, 14, TEXT); cardView(c, d);
        Button fav = button(favorites().contains(x.name) ? (thai ? "★ ลบจากรายการโปรด" : "★ Remove favorite") : (thai ? "☆ เพิ่มรายการโปรด" : "☆ Add favorite"));
        add(c, fav);
        fav.setOnClickListener(v -> { Set<String> f = new HashSet<>(favorites()); if (f.contains(x.name)) f.remove(x.name); else f.add(x.name); prefs.edit().putStringSet("favorites", f).apply(); showTab(3); });
    }

    private List<String> favorites() { return new ArrayList<>(prefs.getStringSet("favorites", new HashSet<String>())); }
    private List<String> championRows(List<Champion> list) { List<String> out=new ArrayList<>(); for(Champion x:list) out.add(x.name+" • "+x.role+" • "+x.damageType); return out; }
    private String[] names(List<Champion> list){String[] a=new String[list.size()];for(int i=0;i<list.size();i++)a[i]=list.get(i).name;return a;}
    private String[] itemNames(List<Item> list){String[] a=new String[list.size()];for(int i=0;i<list.size();i++)a[i]=list.get(i).name;return a;}

    private List<ItemScore> recommend(Champion me, Champion enemy, Item enemyItem, String threats) {
        List<ItemScore> out=new ArrayList<>();
        for(Item x:items){double score=0;List<String> why=new ArrayList<>();String all=(enemy.tags+" "+threats+" "+enemyItem.tags).toUpperCase();
            if(all.contains("HEAL")&&x.tags.contains("ANTI_HEAL")){score+=8;why.add(thai?"ลดการฟื้นฟู":"anti-heal");}
            if((all.contains("ARMOR")||enemy.tags.contains("TANK"))&&x.tags.contains("ARMOR_PEN")){score+=7;why.add(thai?"รับมือเกราะ/แนวหน้า":"armor penetration");}
            if((all.contains("AP")||enemy.damageType.equals("MAGIC"))&&x.tags.contains("MR")){score+=7;why.add(thai?"ต้านเวท":"magic resistance");}
            if(all.contains("SHIELD")&&x.tags.contains("ANTI_SHIELD")){score+=8;why.add(thai?"ตัด Shield":"anti-shield");}
            if((all.contains("ASSASSIN")||all.contains("BURST"))&&x.tags.contains("REVIVE")){score+=5;why.add(thai?"กันการโดนลบเร็ว":"survivability");}
            if(all.contains("AD")&&x.tags.contains("ARMOR")){score+=6;why.add(thai?"ลดแรงโจมตีกายภาพ":"armor");}
            if(all.contains("CC")&&x.tags.contains("CC_RESIST")){score+=6;why.add(thai?"ลดผล CC":"CC resistance");}
            if(me.tags.contains("DPS")&&x.tags.contains("ARMOR_PEN"))score+=1;
            if(why.isEmpty()){score=1;why.add(thai?"ตัวเลือกสถานการณ์":"situational");}
            out.add(new ItemScore(x,score,String.join(" • ",why)));
        }
        Collections.sort(out, (a,b)->Double.compare(b.score,a.score)); return out;
    }

    private String playTip(Champion me, Champion e){
        if(e.tags.contains("ASSASSIN")) return thai?"เก็บ Flash/escape ไว้รับจังหวะเข้าถึงแนวหลัง และอย่าเดินลึกโดยไม่มี Vision":"Hold your escape for the assassin's entry and avoid deep paths without vision.";
        if(e.tags.contains("HEAL")) return thai?"ลดการฮีลก่อนการแลกยาว และหลีกเลี่ยงการให้ศัตรูได้ไฟต์ฟรี":"Apply anti-heal before extended trades and deny free resets.";
        if(e.tags.contains("TANK")||e.tags.contains("ARMOR")) return thai?"อย่าเสีย cooldown ทั้งหมดใส่แนวหน้าโดยไม่มีเป้าหมายหลัก":"Do not spend every cooldown on the frontline without a priority target.";
        return thai?"เล่นตามจังหวะ wave และเก็บ cooldown สำคัญไว้ตอบโต้ engage":"Play around wave timing and reserve key cooldowns for the enemy engage.";
    }

    private LinearLayout bottomNav(){LinearLayout n=new LinearLayout(this);n.setPadding(dp(8),dp(7),dp(8),dp(7));n.setBackgroundColor(Color.rgb(14,18,24));String[] labels=thai?new String[]{"หน้าหลัก","Counter","Build","Champions"}:new String[]{"Home","Counter","Build","Champions"};for(int i=0;i<4;i++){final int tab=i;Button b=button(labels[i]);b.setTextSize(11);b.setBackgroundColor(i==currentTab?Color.rgb(35,72,78):Color.TRANSPARENT);b.setOnClickListener(v->showTab(tab));n.addView(b,new LinearLayout.LayoutParams(0,dp(52),1));}return n;}
    private void header(LinearLayout c,String a,String b){TextView t=text(a,26,TEXT);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);add(c,t);TextView s=text(b,13,MUTED);add(c,s);add(c,space(10));}
    private TextView section(LinearLayout c,String s){TextView t=text(s,16,TEXT);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setPadding(0,dp(10),0,dp(8));return t;}
    private TextView fieldLabel(String s){return text(s,11,ACCENT);}
    private TextView text(String s,int size,int color){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setLineSpacing(0,1.12f);return t;}
    private TextView space(int h){TextView t=new TextView(this);t.setHeight(dp(h));return t;}
    private Button button(String s){Button b=new Button(this);b.setText(s);b.setTextColor(TEXT);b.setTextSize(13);b.setAllCaps(false);b.setBackground(round(PANEL_2,14));return b;}
    private EditText edit(String hint){EditText e=new EditText(this);e.setHint(hint);e.setHintTextColor(MUTED);e.setTextColor(TEXT);e.setSingleLine(true);e.setPadding(dp(14),0,dp(14),0);e.setBackground(round(PANEL_2,14));e.setMinHeight(dp(52));return e;}
    private Spinner spinner(String[] values){Spinner s=new Spinner(this);ArrayAdapter<String>a=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,values);s.setAdapter(a);s.setBackground(round(PANEL_2,12));s.setMinimumHeight(dp(50));return s;}
    private TextView actionCard(String title,String sub,int tab){TextView t=text(title+"\n"+sub,15,TEXT);t.setPadding(dp(16),dp(14),dp(16),dp(14));t.setBackground(round(PANEL,16));t.setOnClickListener(v->showTab(tab));return t;}
    private void card(LinearLayout c,String title,String body){TextView t=text(title+"\n\n"+body,14,TEXT);t.setPadding(dp(16),dp(16),dp(16),dp(16));cardView(c,t);}
    private void cardView(LinearLayout c,View v){v.setBackground(round(PANEL,16));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,dp(7),0,dp(7));c.addView(v,p);}
    private void add(LinearLayout c,View v){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,dp(4),0,dp(4));c.addView(v,p);}
    private void add(LinearLayout c,View v,LinearLayout.LayoutParams p){p.setMargins(0,dp(4),0,dp(4));c.addView(v,p);}
    private GradientDrawable round(int color,int r){GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(dp(r));g.setStroke(dp(1),Color.rgb(38,47,58));return g;}
    private int dp(int v){return(int)(v*getResources().getDisplayMetrics().density+0.5f);}

    static class Champion{final String name,role,damageType,tags,description;Champion(String n,String r,String d,String t,String de){name=n;role=r;damageType=d;tags=t;description=de;}}
    static class Item{final String name,tags,description;Item(String n,String t,String d){name=n;tags=t;description=d;}}
    static class ItemScore{final Item item;final double score;final String reason;ItemScore(Item i,double s,String r){item=i;score=s;reason=r;}}
}
