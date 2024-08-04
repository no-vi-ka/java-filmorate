package ru.yandex.practicum.filmorate.helpers;

import java.time.LocalDate;

public class EqualityHelper {
    public static boolean equality(String s1, String s2) {
        boolean equivalent = false;
        if (s1.equals(s2)) {
            equivalent = true;
        }
        return equivalent;
    }

    public static boolean equality(LocalDate s1, LocalDate s2) {
        boolean equivalent = false;
        if (s1.equals(s2)) {
            equivalent = true;
        }
        return equivalent;
    }

    public static boolean equality(int s1, int s2) {
        boolean equivalent = false;
        if (s1 == s2) {
            equivalent = true;
        }
        return equivalent;
    }
}
