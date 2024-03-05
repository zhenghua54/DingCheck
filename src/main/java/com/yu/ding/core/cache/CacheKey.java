package com.yu.ding.core.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MrYu
 */
public class CacheKey {
    private static final int DEFAULT_MULTIPLIER = 7;
    private static final int DEFAULT_HASHCODE = 17;
    private final int multiplier;
    private int hashcode;
    /**
     * infect factories
     */
    private int count;
    private List<Object> updateList = new ArrayList<>();

    public CacheKey() {
        multiplier = DEFAULT_MULTIPLIER;
        hashcode = DEFAULT_HASHCODE;
    }

    public CacheKey(List<Object> updateList) {
        this();
        this.updateList = updateList;
    }

    public void update(Object o){
        int bashHashcode = o == null ? 1 : objectHashcode(o);
        count++;
        bashHashcode *= count;
        hashcode = multiplier * hashcode + bashHashcode;
        updateList.add(o);
    }
    public void updateAll(Object[] objects){
        for(Object o: objects){
            update(o);
        }
    }

    @Override
    public boolean equals(Object obj) {
        //address
        if(this == obj){
            return true;
        }
        if(!(obj instanceof CacheKey)){
            return false;
        }
        final CacheKey that = (CacheKey) obj;
        if(that.count != count){
            return false;
        }
        if(that.hashcode != hashcode){
            return false;
        }
        if(that.updateList.size() != this.updateList.size()){
            return false;
        }
        for(int i = 0; i < updateList.size(); i++){
            Object thisObject = updateList.get(i);
            Object thatObject = that.updateList.get(i);
            if(!objectEquals(thisObject, thatObject)){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
//        return Objects.hash(paramCount, sql, cursor, maxSize);\
        return hashcode;
    }
    private int objectHashcode(Object obj){
        final Class<?> clazz = obj.getClass();
        if(!clazz.isArray()){
            return obj.hashCode();
        }
        final Class<?> componentType = clazz.getComponentType();
        if (long.class.equals(componentType)) {
            return Arrays.hashCode((long[]) obj);
        } else if (int.class.equals(componentType)) {
            return Arrays.hashCode((int[]) obj);
        } else if (short.class.equals(componentType)) {
            return Arrays.hashCode((short[]) obj);
        } else if (char.class.equals(componentType)) {
            return Arrays.hashCode((char[]) obj);
        } else if (byte.class.equals(componentType)) {
            return Arrays.hashCode((byte[]) obj);
        } else if (boolean.class.equals(componentType)) {
            return Arrays.hashCode((boolean[]) obj);
        } else if (float.class.equals(componentType)) {
            return Arrays.hashCode((float[]) obj);
        } else if (double.class.equals(componentType)) {
            return Arrays.hashCode((double[]) obj);
        } else {
            return Arrays.hashCode((Object[]) obj);
        }
    }

    private boolean objectEquals(Object thisObj, Object thatObj) {
        if (thisObj == null) {
            return thatObj == null;
        } else if (thatObj == null) {
            return false;
        }
        final Class<?> clazz = thisObj.getClass();
        if (!clazz.equals(thatObj.getClass())) {
            return false;
        }
        if (!clazz.isArray()) {
            return thisObj.equals(thatObj);
        }
        final Class<?> componentType = clazz.getComponentType();
        if (long.class.equals(componentType)) {
            return Arrays.equals((long[]) thisObj, (long[]) thatObj);
        } else if (int.class.equals(componentType)) {
            return Arrays.equals((int[]) thisObj, (int[]) thatObj);
        } else if (short.class.equals(componentType)) {
            return Arrays.equals((short[]) thisObj, (short[]) thatObj);
        } else if (char.class.equals(componentType)) {
            return Arrays.equals((char[]) thisObj, (char[]) thatObj);
        } else if (byte.class.equals(componentType)) {
            return Arrays.equals((byte[]) thisObj, (byte[]) thatObj);
        } else if (boolean.class.equals(componentType)) {
            return Arrays.equals((boolean[]) thisObj, (boolean[]) thatObj);
        } else if (float.class.equals(componentType)) {
            return Arrays.equals((float[]) thisObj, (float[]) thatObj);
        } else if (double.class.equals(componentType)) {
            return Arrays.equals((double[]) thisObj, (double[]) thatObj);
        } else {
            return Arrays.equals((Object[]) thisObj, (Object[]) thatObj);
        }
    }
    public static void main(String[] args){
        CacheKey a = new CacheKey();
        CacheKey b = new CacheKey();
        a.update("ssfsf");
        a.update(54);
        b.update("ssfsf");
        System.out.println(a.equals(b));
    }
}
