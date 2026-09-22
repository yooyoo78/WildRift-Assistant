package com.wildriftmetalite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** Offline decision engine for team-aware counter item recommendations. */
public final class CounterEngine {
    public static final String VERSION = "0.2.1";

    public static final class ThreatProfile {
        public boolean healing;
        public boolean shields;
        public boolean magicBurst;
        public boolean physicalBurst;
        public boolean armorStack;
        public boolean magicResist;
        public boolean crowdControl;
        public boolean assassin;
        public boolean frontline;
        public boolean autoAttack;
        public boolean mixedDamage;

        public void absorb(String text) {
            if (text == null) return;
            String s = text.toLowerCase(Locale.ROOT);
            healing |= has(s, "heal", "healing", "ฮีล", "ฟื้นฟู", "mundo", "darius");
            shields |= has(s, "shield", "โล่", "เกราะเวท");
            magicBurst |= has(s, "ap", "magic", "เวท", "mage", "burst");
            physicalBurst |= has(s, "ad", "physical", "กายภาพ", "crit");
            armorStack |= has(s, "armor", "เกราะ", "tank", "แท็งค์");
            magicResist |= has(s, "mr", "magic resist", "ต้านเวท");
            crowdControl |= has(s, "cc", "stun", "root", "knock", "slow", "ควบคุม");
            assassin |= has(s, "assassin", "นักฆ่า", "ล้วง");
            frontline |= has(s, "tank", "frontline", "แนวหน้า");
            autoAttack |= has(s, "auto", "attack", "basic", "marksman", "โจมตีปกติ");
            mixedDamage |= has(s, "mixed", "ad/ap", "ap/ad", "ผสม");
        }

        private static boolean has(String s, String... keys) {
            for (String k : keys) if (s.contains(k)) return true;
            return false;
        }
    }

    /** Small adapter model used by the native UI item catalog. */
    public static final class ItemData {
        public final String name;
        public final String tags;
        public final String category;

        public ItemData(String name, String tags, String category) {
            this.name = name; this.tags = tags == null ? "" : tags; this.category = category == null ? "" : category.toUpperCase(Locale.ROOT);
        }
        public ItemData(String name, String tags, String category) {
            this.name = name;
            this.tags = tags == null ? "" : tags;
            this.category = category == null ? "" : category.toUpperCase(Locale.ROOT);
        }

        public ItemData(String name, String tags) {
            this(name, tags, "");
        }
    }

    public static final class Candidate {
        public final String name;
        public final String tags;
        public double score;
        public final List<String> reasons = new ArrayList<>();
        Candidate(String n, String t) { name = n; tags = t == null ? "" : t.toUpperCase(Locale.ROOT); }
        void add(double points, String reason) { score += points; if (reason != null) reasons.add(reason); }
    }

    private CounterEngine() {}

    /** Native UI overload: scores ItemData records against the complete threat profile. */
    public static List<Candidate> rank(ThreatProfile profile, List<ItemData> itemCatalog) {
        if (profile == null || itemCatalog == null) return Collections.emptyList();
        List<String[]> rows = new ArrayList<>();
        for (ItemData item : itemCatalog) {
            if (item == null) continue;
            rows.add(new String[]{item.name, item.tags, item.category});
        }
        return rank(rows, profile, profile.mixedDamage);
    }

    /** Scores item tags against the complete enemy threat profile. */
    public static List<Candidate> rank(List<String[]> itemCatalog, ThreatProfile profile, boolean mixedDamage) {
        if (itemCatalog == null || profile == null) return Collections.emptyList();
        profile.mixedDamage |= mixedDamage;
        List<Candidate> result = new ArrayList<>();
        for (String[] row : itemCatalog) {
            if (row == null || row.length < 2) continue;
            Candidate c = new Candidate(row[0], row[1]);
            String category = row.length > 2 ? row[2].toUpperCase(Locale.ROOT) : "";
            if (profile.healing && c.tags.contains("ANTI_HEAL")) c.add(10, "ANTI_HEAL");
            if (profile.shields && c.tags.contains("ANTI_SHIELD")) c.add(10, "ANTI_SHIELD");
            if (profile.magicBurst && c.tags.contains("MR")) c.add(8, "MR");
            if (profile.physicalBurst && c.tags.contains("ARMOR")) c.add(8, "ARMOR");
            if (profile.armorStack && c.tags.contains("ARMOR_PEN")) c.add(8, "ARMOR_PEN");
            if (profile.magicResist && c.tags.contains("MAGIC_PEN")) c.add(7, "MAGIC_PEN");
            if (profile.crowdControl && c.tags.contains("CC_RESIST")) c.add(6, "CC_RESIST");
            if (profile.assassin && c.tags.contains("REVIVE")) c.add(5, "REVIVE");
            if (profile.autoAttack && c.tags.contains("ARMOR")) c.add(4, "ANTI_AUTO");
            if (profile.frontline && c.tags.contains("ARMOR_PEN")) c.add(3, "FRONTLINE");
            if (profile.mixedDamage && c.tags.contains("HP")) c.add(2, "MIXED_DAMAGE");
            if ("PHYSICAL".equals(category) && profile.magicBurst && !profile.physicalBurst) c.add(-1.5, "DAMAGE_PROFILE_MISMATCH");
            if ("PHYSICAL".equals(category) && profile.physicalBurst) c.add(1.0, "PHYSICAL_PROFILE");
            if (c.score == 0) c.add(0.5, "SITUATIONAL");
            result.add(c);
        }
        result.sort(Comparator.comparingDouble((Candidate x) -> x.score).reversed());
        return result;
    }
}
