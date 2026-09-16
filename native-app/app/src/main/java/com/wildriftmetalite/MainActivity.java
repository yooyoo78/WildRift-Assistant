package com.wildriftmetalite;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
import java.util.HashSet;
import java.util.List;

public class MainActivity extends Activity {
    private static final int BG=Color.rgb(10,14,22), PANEL=Color.rgb(20,27,39), TEXT=Color.rgb(235,240,248), MUTED=Color.rgb(155,168,188);
    private boolean thai=true;
    private LinearLayout root;
    private String dataPatch="unknown", dataCoverage="unavailable", dataUpdatedAt="unknown";
    private final ArrayList<Champion> champions=new ArrayList<>();
    private final ArrayList<Item> items=new ArrayList<>();

    static class Champion {
        final String id,name,role,damageType,tags,description;
        Champion(String id,String name,String role,String damageType,String tags,String description){this.id=id;this.name=name;this.role=role;this.damageType=damageType;this.tags=tags;this.description=description;}
    }
    static class Item {
        final String id,name,category,tags,description;
        Item(String id,String name,String category,String tags,String description){this.id=id;this.name=name;this.category=category;this.tags=tags;this.description=description;}
    }

    @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(BG);loadOfflineData();render();}

    private void loadOfflineData(){
        champions.clear(); items.clear();
        OfflineDataRepository.Metadata meta=OfflineDataRepository.manifest(this);
        dataPatch=meta.patch; dataCoverage=meta.coverage; dataUpdatedAt=meta.updatedAt;
        try {
            for(OfflineDataRepository.ChampionRecord r:OfflineDataRepository.champions(this))
                champions.add(new Champion(r.id,r.name,r.roles,r.damageType,r.tags,r.description));
            for(OfflineDataRepository.ItemRecord r:OfflineDataRepository.items(this))
                items.add(new Item(r.id,r.name,r.category,r.tags,r.description));
        } catch(Exception e) {
            dataCoverage="load-error";
        }
    }

    private void render(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(BG);show("HOME");setContentView(root);}

    private void show(String page){
        root.removeAllViews();
        LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(14),dp(10),dp(14),dp(10));
        ScrollView s=new ScrollView(this);s.addView(c);root.addView(s,new LinearLayout.LayoutParams(-1,0,1));nav(root);
        if(page.equals("HOME"))home(c);else if(page.equals("COUNTER"))counter(c);else if(page.equals("BUILD"))build(c);else champions(c);
    }

    private void nav(LinearLayout parent){
        LinearLayout bar=new LinearLayout(this);bar.setPadding(dp(10),dp(8),dp(10),dp(8));bar.setBackgroundColor(PANEL);
        String[] labels={"HOME","COUNTER","BUILD","CHAMPIONS"};
        for(String label:labels){Button b=button(label);b.setOnClickListener(v->show(label));bar.addView(b,new LinearLayout.LayoutParams(0,dp(46),1));}
        parent.addView(bar);
    }

    private void home(LinearLayout c){
        header(c,"LOL: ASSISTANTS",thai?"ผู้ช่วย Wild Rift แบบ Offline-first":"Offline-first Wild Rift assistant");
        card(c,"DATA SNAPSHOT",(thai?"Patch: ":"Patch: ")+dataPatch+"\n"+(thai?"Coverage: ":"Coverage: ")+dataCoverage+"\n"+(thai?"Updated: ":"Updated: ")+dataUpdatedAt+"\n"+(thai?"Champions: ":"Champions: ")+champions.size()+"\n"+(thai?"Items: ":"Items: ")+items.size());
        card(c,"ENGINE STATUS",thai?"Team Counter: 5 slots\nRole-aware filtering\nThreat-aware item recommendation\nCounterEngine + structured offline data":"Team Counter: 5 slots\nRole-aware filtering\nThreat-aware item recommendation\nCounterEngine + structured offline data");
        Button x=button(thai?"เปลี่ยนภาษา: THA / ENG":"Language: THA / ENG");x.setOnClickListener(v->{thai=!thai;show("HOME");});c.addView(x);
        if("load-error".equals(dataCoverage)) card(c,"DATA ERROR",thai?"โหลดข้อมูลออฟไลน์ไม่สำเร็จ — ระบบจะไม่ใช้ข้อมูล seed เก่าโดยอัตโนมัติ":"Offline data failed to load; legacy seed data is not used automatically.");
    }

    private void counter(LinearLayout c){
        header(c,"COUNTER CENTER",thai?"วิเคราะห์ทีมศัตรู 5 ช่อง + ภัยคุกคาม":"Analyze 5 enemy slots + threats");
        Spinner role=spinner(new String[]{"TOP","JUNGLE","MID","ADC","SUPPORT"});add(c,role);
        Spinner player=spinner(namesForRole("TOP"));add(c,player);
        role.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> p){}public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){player.setAdapter(new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,namesForRole((String)p.getItemAtPosition(pos))));}});
        ArrayList<Spinner> enemy=new ArrayList<>();
        for(int i=0;i<5;i++){Spinner e=spinner(enemyOptions(i,new HashSet<>()));enemy.add(e);add(c,e);final int idx=i;e.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> p){}public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){refreshEnemySpinners(enemy,idx);}});}
        Spinner ei=spinner(itemNames());add(c,ei);
        EditText threats=edit(thai?"ภัยเพิ่มเติม: healing, shield, armor, AP, CC...":"Extra threats: healing, shield, armor, AP, CC...");add(c,threats);
        Button run=button(thai?"วิเคราะห์ทีม":"ANALYZE TEAM");add(c,run);TextView out=text("");add(c,out);
        run.setOnClickListener(v->{
            CounterEngine.ThreatProfile p=new CounterEngine.ThreatProfile();
            for(Spinner e:enemy){Object selected=e.getSelectedItem();if(selected!=null){Champion ch=findChampion(selected.toString());if(ch!=null)p.absorb(ch.tags+" "+ch.damageType+" "+ch.description);}}
            Object selectedItem=ei.getSelectedItem();if(selectedItem!=null)p.absorb(selectedItem.toString());p.absorb(threats.getText().toString());
            List<CounterEngine.Candidate> r=CounterEngine.rank(p,itemsToEngine());
            StringBuilder z=new StringBuilder("TEAM THREAT PROFILE\n").append(profileSummary(p)).append("\n\nITEM COUNTER\n");
            for(int i=0;i<Math.min(6,r.size());i++)z.append(i+1).append(". ").append(r.get(i).name).append(" — ").append(r.get(i).score).append("\n");
            z.append("\n").append(gamePlan(p));out.setText(z.toString());
        });
    }

    private void build(LinearLayout c){
        header(c,"BUILD LAB",thai?"Build ที่ปรับตามภัยของทีมศัตรู":"Build adjusted to enemy team threats");
        Spinner ei=spinner(itemNames());add(c,ei);EditText t=edit(thai?"ใส่ภัยสำคัญของเกม":"Enter key game threats");add(c,t);Button b=button(thai?"สร้าง Build":"GENERATE BUILD");add(c,b);TextView out=text("");add(c,out);
        b.setOnClickListener(v->{CounterEngine.ThreatProfile p=new CounterEngine.ThreatProfile();Object selected=ei.getSelectedItem();if(selected!=null)p.absorb(selected.toString());p.absorb(t.getText().toString());List<CounterEngine.Candidate> r=CounterEngine.rank(p,itemsToEngine());StringBuilder z=new StringBuilder();for(int i=0;i<Math.min(5,r.size());i++)z.append(i+1).append(". ").append(r.get(i).name).append(" — ").append(r.get(i).score).append("\n");z.append("\n").append(thai?"หลักการ: ปรับไอเทมตามภัยของเกม ไม่ยึด build preset แบบตายตัว":"Principle: adapt items to the game threat instead of blindly following a fixed preset.");out.setText(z.toString());});
    }

    private void champions(LinearLayout c){
        header(c,"CHAMPION DATABASE",thai?"ข้อมูลแชมป์จาก offline snapshot + Role":"Champions from the offline snapshot + roles");
        EditText search=edit(thai?"ค้นหา Ahri, Kai'Sa, MID...":"Search Ahri, Kai'Sa, MID...");add(c,search);
        ListView list=new ListView(this);list.setDivider(null);list.setBackgroundColor(BG);c.addView(list,new LinearLayout.LayoutParams(-1,dp(430)));
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,championRows(champions));list.setAdapter(adapter);
        search.addTextChangedListener(new android.text.TextWatcher(){public void beforeTextChanged(CharSequence s,int st,int c,int a){}public void onTextChanged(CharSequence s,int st,int b,int c2){adapter.getFilter().filter(s);}public void afterTextChanged(android.text.Editable e){}});
        list.setOnItemClickListener((p,v,pos,id)->{Champion x=findChampion(((TextView)v).getText().toString().split(" • ")[0]);if(x!=null)championDetail(c,x);});
    }

    private void championDetail(LinearLayout c,Champion x){card(c,x.name,"Role: "+x.role+"\nDamage: "+x.damageType+"\nTags: "+x.tags+"\nPatch: "+dataPatch+"\nCoverage: "+dataCoverage+"\nUpdated: "+dataUpdatedAt+"\n\n"+x.description);}
    private String[] namesForRole(String role){ArrayList<String>a=new ArrayList<>();for(Champion x:champions)if(x.role.contains(role))a.add(x.name);return a.toArray(new String[0]);}
    private String[] enemyOptions(int index,HashSet<String> used){ArrayList<String>n=new ArrayList<>();n.add("— Enemy "+(index+1)+" —");for(Champion x:champions)if(!used.contains(x.name))n.add(x.name);return n.toArray(new String[0]);}
    private void refreshEnemySpinners(ArrayList<Spinner> all,int changed){HashSet<String>used=new HashSet<>();for(int i=0;i<all.size();i++)if(i!=changed&&all.get(i).getSelectedItem()!=null)used.add((String)all.get(i).getSelectedItem());for(int i=0;i<all.size();i++){if(i==changed)continue;ArrayList<String>n=new ArrayList<>();n.add("— Enemy "+(i+1)+" —");for(Champion x:champions)if(!used.contains(x.name))n.add(x.name);String cur=all.get(i).getSelectedItem()==null?null:(String)all.get(i).getSelectedItem();ArrayAdapter<String>ad=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,n);all.get(i).setAdapter(ad);int p=cur==null?-1:n.indexOf(cur);if(p>=0)all.get(i).setSelection(p);}}
    private String[] itemNames(){String[]a=new String[items.size()+1];a[0]="— Enemy item / threat —";for(int i=0;i<items.size();i++)a[i+1]=items.get(i).name;return a;}
    private List<CounterEngine.ItemData> itemsToEngine(){ArrayList<CounterEngine.ItemData>a=new ArrayList<>();for(Item x:items)a.add(new CounterEngine.ItemData(x.name,x.tags));return a;}
    private String profileSummary(CounterEngine.ThreatProfile p){return "healing="+p.healing+", shields="+p.shields+", magicBurst="+p.magicBurst+", physicalBurst="+p.physicalBurst+", armorStack="+p.armorStack+", magicResist="+p.magicResist+", CC="+p.crowdControl+", assassin="+p.assassin+", frontline="+p.frontline+", autoAttack="+p.autoAttack+", mixed="+p.mixedDamage;}
    private String gamePlan(CounterEngine.ThreatProfile p){StringBuilder s=new StringBuilder("\nGAMEPLAY PLAN\n");if(p.assassin)s.append("• Respect burst windows and preserve key defensive cooldowns.\n");if(p.healing)s.append("• Focus sustained targets and manage fight duration.\n");if(p.frontline)s.append("• Avoid wasting cooldowns on the frontline without purpose.\n");if(p.crowdControl)s.append("• Track major CC and choose entry timing accordingly.\n");return s.toString();}
    private Champion findChampion(String n){for(Champion x:champions)if(x.name.equals(n))return x;return null;}
    private void header(LinearLayout c,String a,String b){TextView t=text(a);t.setTextSize(24);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);c.addView(t);TextView s=text(b);s.setTextColor(MUTED);c.addView(s);}
    private void card(LinearLayout c,String title,String body){LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(dp(14),dp(14),dp(14),dp(14));GradientDrawable g=new GradientDrawable();g.setColor(PANEL);g.setCornerRadius(dp(14));box.setBackground(g);TextView h=text(title);h.setTypeface(Typeface.DEFAULT,Typeface.BOLD);box.addView(h);box.addView(text(body));c.addView(box,new LinearLayout.LayoutParams(-1,-2));}
    private TextView text(String s){TextView t=new TextView(this);t.setText(s);t.setTextColor(TEXT);t.setTextSize(15);t.setPadding(dp(6),dp(8),dp(6),dp(8));return t;}
    private Button button(String s){Button b=new Button(this);b.setText(s);b.setTextColor(TEXT);return b;}
    private EditText edit(String h){EditText e=new EditText(this);e.setHint(h);e.setHintTextColor(MUTED);e.setTextColor(TEXT);return e;}
    private Spinner spinner(String[]d){Spinner s=new Spinner(this);s.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,d));return s;}
    private void add(LinearLayout c,View v){c.addView(v,new LinearLayout.LayoutParams(-1,-2));}
    private int dp(int n){return(int)(n*getResources().getDisplayMetrics().density+0.5f);}
    private ArrayList<String> championRows(List<Champion>xs){ArrayList<String>a=new ArrayList<>();for(Champion x:xs)a.add(x.name+" • "+x.role+" • "+x.damageType);return a;}
}
