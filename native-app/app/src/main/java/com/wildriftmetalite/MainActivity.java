package com.wildriftmetalite;

import android.app.Activity;
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

public class MainActivity extends Activity {
    private static final int BG=Color.rgb(10,14,22),PANEL=Color.rgb(20,27,39),TEXT=Color.rgb(235,240,248),MUTED=Color.rgb(155,168,188);
    private static final String DATA_PATCH="7.2e-reference";
    private static final String DATA_SOURCE="Wild Rift Core / Wild Rift Build / Wild Rift Meta / Wild Rift Guides";
    private boolean thai=true;
    private LinearLayout root;
    private final java.util.ArrayList<Champion> champions=new java.util.ArrayList<>();
    private final java.util.ArrayList<Item> items=new java.util.ArrayList<>();
    static class Champion{final String name,role,damageType,tags,description,patch,source;Champion(String n,String r,String d,String t,String de){name=n;role=r;damageType=d;tags=t;description=de;patch=DATA_PATCH;source=DATA_SOURCE;}}
    static class Item{final String name,tags,description;Item(String n,String t,String d){name=n;tags=t;description=d;}}
    @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(BG);seed();render();}
    private void seed(){
        if(!champions.isEmpty())return;
        champions.add(new Champion("Ahri","MID","AP","BURST,CC","Mobile mage with charm and burst windows."));
        champions.add(new Champion("Yasuo","MID","AD","CRIT,DASH","Dueling skirmisher with high mobility."));
        champions.add(new Champion("Akali","MID","AP","ASSASSIN,BURST","Assassin with burst and backline access."));
        champions.add(new Champion("Orianna","MID","AP","CC,AOE","Control mage with zone control and teamfight utility."));
        champions.add(new Champion("Lux","MID,SUPPORT","AP","CC,BURST,SHIELD","Long-range control mage with shield and bind."));
        champions.add(new Champion("Morgana","MID,SUPPORT","AP","CC,SHIELD","Zone control with binding and magic shield."));
        champions.add(new Champion("Malphite","TOP","AP/ARMOR","ENGAGE,CC,FRONTLINE","Frontline engage tank with strong teamfight initiation."));
        champions.add(new Champion("Darius","TOP","AD","HEAL,FRONTLINE","Bruiser with sustained damage and healing."));
        champions.add(new Champion("Rammus","JUNGLE","ARMOR","CC,FRONTLINE,ANTI_AUTO","Armor-heavy initiator that punishes basic attacks."));
        champions.add(new Champion("Lee Sin","JUNGLE","AD","DASH,BURST,ENGAGE","Early pressure jungler with mobility and engage."));
        champions.add(new Champion("Kha'Zix","JUNGLE","AD","ASSASSIN,BURST","Isolation-focused assassin with high burst."));
        champions.add(new Champion("Kai'Sa","ADC","AD/AP","CRIT,BURST","Scaling marksman with mixed damage potential."));
        champions.add(new Champion("Jinx","ADC","AD","CRIT,AOE","Scaling marksman with high teamfight DPS."));
        champions.add(new Champion("Caitlyn","ADC","AD","POKE,CRIT","Long-range marksman with lane pressure."));
        champions.add(new Champion("Thresh","SUPPORT","AD","CC,ENGAGE,PEEL","Playmaking support with hook and lantern utility."));
        champions.add(new Champion("Leona","SUPPORT","TANK","CC,ENGAGE,FRONTLINE","Hard-engage tank support with layered CC."));
        champions.add(new Champion("Garen","TOP","AD","FRONTLINE,BRUISER","Durable fighter with execute threat."));
        champions.add(new Champion("Vayne","ADC,TOP","AD","TRUE_DAMAGE,DUEL","Scaling duelist that shreds durable targets."));
        champions.add(new Champion("Seraphine","MID,SUPPORT","AP","CC,SHIELD,AOE","Teamfight mage with CC and shielding."));
        champions.add(new Champion("Dr. Mundo","TOP,JUNGLE","AD","HEAL,FRONTLINE,HP","High-health frontline with strong regeneration."));
        items.add(new Item("Mortal Reminder","ANTI_HEAL,ARMOR_PEN","Anti-heal plus armor penetration."));
        items.add(new Item("Lord Dominik's Regards","ARMOR_PEN","Armor penetration for durable targets."));
        items.add(new Item("Maw of Malmortius","MR","Magic-damage defense with anti-burst value."));
        items.add(new Item("Guardian Angel","REVIVE","Revive effect for high-risk fights."));
        items.add(new Item("Black Cleaver","ARMOR_PEN,HP","Health plus armor reduction/penetration utility."));
        items.add(new Item("Serpent's Fang","ANTI_SHIELD","Shield counter item."));
        items.add(new Item("Mercury's Treads","CC_RESIST,MR","Magic resistance and crowd-control mitigation."));
        items.add(new Item("Plated Steelcaps","ARMOR,ANTI_AUTO","Armor and basic-attack mitigation."));
        items.add(new Item("Spirit Visage","MR,HP","Magic resistance and sustain-oriented defense."));
        items.add(new Item("Executioner's Calling","ANTI_HEAL","Early anti-heal component."));
        items.add(new Item("Serylda's Grudge","ARMOR_PEN,CC","Armor penetration with slow utility."));
    }
    private void render(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(BG);show("HOME");setContentView(root);}
    private void show(String page){root.removeAllViews();LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(14),dp(10),dp(14),dp(10));ScrollView s=new ScrollView(this);s.addView(c);root.addView(s,new LinearLayout.LayoutParams(-1,0,1));nav(root);if(page.equals("HOME"))home(c);else if(page.equals("COUNTER"))counter(c);else if(page.equals("BUILD"))build(c);else champions(c);}
    private void nav(LinearLayout parent){LinearLayout bar=new LinearLayout(this);bar.setPadding(dp(10),dp(8),dp(10),dp(8));bar.setBackgroundColor(PANEL);String[] labels={"HOME","COUNTER","BUILD","CHAMPIONS"};for(String label:labels){Button b=button(label);b.setOnClickListener(v->show(label));bar.addView(b,new LinearLayout.LayoutParams(0,dp(46),1));}parent.addView(bar);}
    private void home(LinearLayout c){header(c,"LOL: ASSISTANTS",thai?"ผู้ช่วย Wild Rift แบบ Offline-first":"Offline-first Wild Rift assistant");card(c,"STATUS",thai?"Team Counter: 5 slots\nRole-aware filtering\nThreat-aware item recommendation\nCounterEngine + offline data":"Team Counter: 5 slots\nRole-aware filtering\nThreat-aware item recommendation\nCounterEngine + offline data");Button x=button(thai?"เปลี่ยนภาษา: THA / ENG":"Language: THA / ENG");x.setOnClickListener(v->{thai=!thai;show("HOME");});c.addView(x);}
    private void counter(LinearLayout c){header(c,"COUNTER CENTER",thai?"วิเคราะห์ทีมศัตรู 5 ช่อง + ภัยคุกคาม":"Analyze 5 enemy slots + threats");Spinner role=spinner(new String[]{"TOP","JUNGLE","MID","ADC","SUPPORT"});add(c,role);Spinner player=spinner(namesForRole("TOP"));add(c,player);role.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> p){}public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){player.setAdapter(new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,namesForRole((String)p.getItemAtPosition(pos))));}});java.util.ArrayList<Spinner> enemy=new java.util.ArrayList<>();for(int i=0;i<5;i++){Spinner e=spinner(new String[]{"— Enemy "+(i+1)+" —"});enemy.add(e);add(c,e);final int idx=i;e.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> p){}public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){refreshEnemySpinners(enemy,idx);}});}Spinner ei=spinner(itemNames());add(c,ei);EditText threats=edit(thai?"ภัยเพิ่มเติม: healing, shield, armor, AP, CC...":"Extra threats: healing, shield, armor, AP, CC...");add(c,threats);Button run=button(thai?"วิเคราะห์ทีม":"ANALYZE TEAM");add(c,run);TextView out=text("");add(c,out);run.setOnClickListener(v->{CounterEngine.ThreatProfile p=new CounterEngine.ThreatProfile();for(Spinner e:enemy){Champion ch=findChampion((String)e.getSelectedItem());if(ch!=null)p.absorb(ch.tags+" "+ch.damageType+" "+ch.description);}p.absorb((String)ei.getSelectedItem());p.absorb(threats.getText().toString());java.util.List<CounterEngine.Candidate> r=CounterEngine.rank(p,itemsToEngine());StringBuilder z=new StringBuilder("TEAM THREAT PROFILE\n").append(profileSummary(p)).append("\n\nITEM COUNTER\n");for(int i=0;i<Math.min(6,r.size());i++)z.append(i+1).append(". ").append(r.get(i).name).append(" — ").append(r.get(i).score).append("\n");z.append("\n").append(gamePlan(p));out.setText(z.toString());});}
    private void build(LinearLayout c){header(c,"BUILD LAB",thai?"Build ที่ปรับตามภัยของทีมศัตรู":"Build adjusted to enemy team threats");Spinner ei=spinner(itemNames());add(c,ei);EditText t=edit(thai?"ใส่ภัยสำคัญของเกม":"Enter key game threats");add(c,t);Button b=button(thai?"สร้าง Build":"GENERATE BUILD");add(c,b);TextView out=text("");add(c,out);b.setOnClickListener(v->{CounterEngine.ThreatProfile p=new CounterEngine.ThreatProfile();p.absorb((String)ei.getSelectedItem());p.absorb(t.getText().toString());java.util.List<CounterEngine.Candidate> r=CounterEngine.rank(p,itemsToEngine());StringBuilder z=new StringBuilder();for(int i=0;i<Math.min(5,r.size());i++)z.append(i+1).append(". ").append(r.get(i).name).append(" — ").append(r.get(i).score).append("\n");z.append("\n").append(thai?"หลักการ: ปรับไอเทมตามภัยของเกม ไม่ยึด build preset แบบตายตัว":"Principle: adapt items to the game threat instead of blindly following a fixed preset.");out.setText(z.toString());});}
    private void champions(LinearLayout c){header(c,"CHAMPION DATABASE",thai?"ข้อมูลแชมป์ออฟไลน์ + Role":"Offline champion data + roles");EditText search=edit(thai?"ค้นหา Ahri, Kai'Sa, MID...":"Search Ahri, Kai'Sa, MID...");add(c,search);ListView list=new ListView(this);list.setDivider(null);list.setBackgroundColor(BG);c.addView(list,new LinearLayout.LayoutParams(-1,dp(430)));ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,championRows(champions));list.setAdapter(adapter);search.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int st,int c,int a){}public void onTextChanged(CharSequence s,int st,int b,int c2){adapter.getFilter().filter(s);}public void afterTextChanged(Editable e){}});list.setOnItemClickListener((p,v,pos,id)->{Champion x=findChampion(((TextView)v).getText().toString().split(" • ")[0]);if(x!=null)championDetail(c,x);});}
    private void championDetail(LinearLayout c,Champion x){card(c,x.name,"Role: "+x.role+"\nDamage: "+x.damageType+"\nTags: "+x.tags+"\nPatch: "+x.patch+"\nSource: "+x.source+"\n\n"+x.description);}
    private String[] namesForRole(String role){java.util.ArrayList<String>a=new java.util.ArrayList<>();for(Champion x:champions)if(x.role.contains(role))a.add(x.name);return a.toArray(new String[0]);}
    private void refreshEnemySpinners(java.util.ArrayList<Spinner> all,int changed){java.util.HashSet<String> used=new java.util.HashSet<>();for(int i=0;i<all.size();i++)if(i!=changed&&all.get(i).getSelectedItem()!=null)used.add((String)all.get(i).getSelectedItem());for(int i=0;i<all.size();i++){if(i==changed)continue;java.util.ArrayList<String> n=new java.util.ArrayList<>();n.add("— Enemy "+(i+1)+" —");for(Champion x:champions)if(!used.contains(x.name))n.add(x.name);String cur=(String)all.get(i).getSelectedItem();ArrayAdapter<String> ad=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,n);all.get(i).setAdapter(ad);int p=n.indexOf(cur);if(p>=0)all.get(i).setSelection(p);}}
    private String[] itemNames(){String[]a=new String[items.size()+1];a[0]="— Enemy item / threat —";for(int i=0;i<items.size();i++)a[i+1]=items.get(i).name;return a;}
    private java.util.List<CounterEngine.ItemData> itemsToEngine(){java.util.ArrayList<CounterEngine.ItemData>a=new java.util.ArrayList<>();for(Item x:items)a.add(new CounterEngine.ItemData(x.name,x.tags));return a;}
    private String profileSummary(CounterEngine.ThreatProfile p){return "healing="+p.healing+", shields="+p.shields+", magicBurst="+p.magicBurst+", physicalBurst="+p.physicalBurst+", armorStack="+p.armorStack+", magicResist="+p.magicResist+", CC="+p.crowdControl+", assassin="+p.assassin+", frontline="+p.frontline+", autoAttack="+p.autoAttack+", mixed="+p.mixedDamage;}
    private String gamePlan(CounterEngine.ThreatProfile p){StringBuilder s=new StringBuilder("\nGAMEPLAY PLAN\n");if(p.assassin)s.append("• Respect burst windows and preserve key defensive cooldowns.\n");if(p.healing)s.append("• Focus sustained targets and manage fight duration.\n");if(p.frontline)s.append("• Avoid wasting cooldowns on the frontline without purpose.\n");if(p.crowdControl)s.append("• Track major CC and choose entry timing accordingly.\n");return s.toString();}
    private void header(LinearLayout c,String a,String b){TextView t=text(a);t.setTextSize(24);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);c.addView(t);TextView s=text(b);s.setTextColor(MUTED);c.addView(s);}
    private void card(LinearLayout c,String title,String body){LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(dp(14),dp(14),dp(14),dp(14));GradientDrawable g=new GradientDrawable();g.setColor(PANEL);g.setCornerRadius(dp(14));box.setBackground(g);TextView h=text(title);h.setTypeface(Typeface.DEFAULT,Typeface.BOLD);box.addView(h);box.addView(text(body));c.addView(box,new LinearLayout.LayoutParams(-1,-2));}
    private TextView text(String s){TextView t=new TextView(this);t.setText(s);t.setTextColor(TEXT);t.setTextSize(15);t.setPadding(dp(6),dp(8),dp(6),dp(8));return t;}
    private Button button(String s){Button b=new Button(this);b.setText(s);b.setTextColor(TEXT);return b;}
    private EditText edit(String h){EditText e=new EditText(this);e.setHint(h);e.setHintTextColor(MUTED);e.setTextColor(TEXT);return e;}
    private Spinner spinner(String[]d){Spinner s=new Spinner(this);s.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,d));return s;}
    private void add(LinearLayout c,View v){c.addView(v,new LinearLayout.LayoutParams(-1,-2));}
    private int dp(int n){return(int)(n*getResources().getDisplayMetrics().density+0.5f);}
    private java.util.ArrayList<String> championRows(java.util.List<Champion>xs){java.util.ArrayList<String>a=new java.util.ArrayList<>();for(Champion x:xs)a.add(x.name+" • "+x.role+" • "+x.damageType);return a;}
    private Champion findChampion(String n){for(Champion x:champions)if(x.name.equals(n))return x;return null;}
}
