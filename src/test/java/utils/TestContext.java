package utils;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    
    private static final ThreadLocal<Map<String, Object>> context = 
            ThreadLocal.withInitial(HashMap::new);
    
    public static void set(String key, Object value) {
        context.get().put(key, value);
        System.out.println("✓ TestContext.set('" + key + "', " + 
                         (value != null ? value.toString() : "null") + ")");
    }
    
    public static Object get(String key) {
        Object value = context.get().get(key);
        System.out.println("✓ TestContext.get('" + key + "') = " + 
                         (value != null ? value.toString() : "null"));
        return value;
    }
    
    public static String getString(String key) {
        Object value = get(key);
        return value != null ? value.toString() : null;
    }
    
    public static Integer getInteger(String key) {
        Object value = get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    public static Boolean getBoolean(String key) {
        Object value = get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return null;
    }
    
    public static boolean has(String key) {
        boolean exists = context.get().containsKey(key);
        System.out.println("✓ TestContext.has('" + key + "') = " + exists);
        return exists;
    }
    
    public static void remove(String key) {
        Object removed = context.get().remove(key);
        System.out.println("✓ TestContext.remove('" + key + "') = " + 
                         (removed != null ? "removed" : "not found"));
    }
    
    public static void clear() {
        int size = context.get().size();
        context.get().clear();
        System.out.println("✓ TestContext.clear() - Removed " + size + " entries");
    }
    
    public static void clearAll() {
        clear();
        context.remove();
        System.out.println("✓ TestContext.clearAll() - ThreadLocal removed");
    }
    
    public static Map<String, Object> getAll() {
        return new HashMap<>(context.get());
    }
    
    public static void printAll() {
        Map<String, Object> allData = getAll();
        System.out.println("\n========== TestContext - All Data ==========");
        if (allData.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            allData.forEach((key, value) -> 
                System.out.println("  " + key + " = " + value));
        }
        System.out.println("============================================\n");
    }
    
    public static int size() {
        return context.get().size();
    }
}
