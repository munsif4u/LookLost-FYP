package com.example.looklost;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

public abstract class FirestoreRecyclerAdapter<T, T1> {
    public <FirestoreRecyclerOptions> FirestoreRecyclerAdapter(FirestoreRecyclerOptions options) {
    }

    protected abstract void onBindViewHolder(@NonNull AdapterToShowMyItems.ItemViewHolder holder, int position, @NonNull ItemModel model);

    @NonNull
    public abstract AdapterToShowMyItems.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);
}
