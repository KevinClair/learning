package com.github.kevin.learning.learning_19;

public class SessionUserLocal {

    private static final ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static User getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static void setUser(User user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}
