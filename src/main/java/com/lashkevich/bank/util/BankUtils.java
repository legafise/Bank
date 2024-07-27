package com.lashkevich.bank.util;

import java.io.Closeable;
import java.io.IOException;

public final class BankUtils {

    private BankUtils() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

}
