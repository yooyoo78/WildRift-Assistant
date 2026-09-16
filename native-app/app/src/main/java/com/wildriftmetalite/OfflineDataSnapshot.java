package com.wildriftmetalite;

import android.content.Context;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Immutable, offline-first snapshot used by decision features.
 * Keeps all datasets on the same patch snapshot and rejects silent mixing.
 */
public final class OfflineDataSnapshot {
    public final OfflineDataRepository.Metadata metadata;
    public final List<OfflineDataRepository.ChampionRecord> champions;
    public final List<OfflineDataRepository.ItemRecord> items;

    private OfflineDataSnapshot(OfflineDataRepository.Metadata metadata,
                                List<OfflineDataRepository.ChampionRecord> champions,
                                List<OfflineDataRepository.ItemRecord> items) {
        this.metadata = metadata;
        this.champions = champions;
        this.items = items;
    }

    public static OfflineDataSnapshot load(Context context) throws Exception {
        OfflineDataRepository.Metadata metadata = OfflineDataRepository.manifest(context);
        List<OfflineDataRepository.ChampionRecord> champions = OfflineDataRepository.champions(context);
        List<OfflineDataRepository.ItemRecord> items = OfflineDataRepository.items(context);
        validate(metadata, champions, items);
        return new OfflineDataSnapshot(metadata, champions, items);
    }

    private static void validate(OfflineDataRepository.Metadata metadata,
                                 List<OfflineDataRepository.ChampionRecord> champions,
                                 List<OfflineDataRepository.ItemRecord> items) throws Exception {
        if (metadata == null || metadata.patch == null || metadata.patch.equals("unknown"))
            throw new IllegalStateException("Offline data manifest is unavailable");
        if (champions == null || champions.isEmpty())
            throw new IllegalStateException("Champion dataset is empty");
        if (items == null || items.isEmpty())
            throw new IllegalStateException("Item dataset is empty");

        Set<String> championIds = new HashSet<>();
        for (OfflineDataRepository.ChampionRecord c : champions) {
            if (c.id == null || c.id.trim().isEmpty() || !championIds.add(c.id))
                throw new IllegalStateException("Duplicate/invalid champion id");
        }
        Set<String> itemIds = new HashSet<>();
        for (OfflineDataRepository.ItemRecord i : items) {
            if (i.id == null || i.id.trim().isEmpty() || !itemIds.add(i.id))
                throw new IllegalStateException("Duplicate/invalid item id");
        }
    }
}
