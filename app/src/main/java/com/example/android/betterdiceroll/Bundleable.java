package com.example.android.betterdiceroll;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Bundleable {
    @NonNull Bundle toBundle();

    void fromBundle(@Nullable Bundle bundle);
}
