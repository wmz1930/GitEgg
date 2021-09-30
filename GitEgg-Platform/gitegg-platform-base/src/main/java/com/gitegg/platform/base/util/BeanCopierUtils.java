 package com.gitegg.platform.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.service.IAction;
import org.springframework.cglib.beans.BeanCopier;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用对象复制类
 * @author GitEgg
 * @date 2020/09/20
 */
@Slf4j
public class BeanCopierUtils {

    /**
     * 使用缓存提高效率
     */
    private static final ConcurrentHashMap<String, BeanCopier> mapCaches = new ConcurrentHashMap<>();

        /**
         * 复制到指定类型的对象
         * @param <O>
         * @param <T>
         * @param source
         * @param target
         * @return
         */
        public static <O, T> T copyByClass(O source, Class<T> target) {
            T instance = baseMapper(source, target);
            return instance;
        }

        /**
         * 复制到指定类型的对象 lambda
         * @param <O>
         * @param <T>
         * @param source
         * @param target
         * @param action
         * @return
         */
        public static <O, T> T copyByClass(O source, Class<T> target, IAction<T> action) {
            T instance = baseMapper(source, target);
            action.run(instance);
            return instance;
        }

        /**
         * 复制到已存在的对象
         * @param <O>
         * @param <T>
         * @param source
         * @param target
         * @return
         */
        public static <O, T> T copyByObject(O source, T target) {
            String baseKey = generateKey(source.getClass(), target.getClass());
            BeanCopier copier;
            if (!mapCaches.containsKey(baseKey)) {
                copier = BeanCopier.create(source.getClass(), target.getClass(), false);
                mapCaches.put(baseKey, copier);
            } else {
                copier = mapCaches.get(baseKey);
            }
            copier.copy(source, target, null);
            return target;
        }

        /**
         *  复制到已存在的对象 lambda
         * @param <O>
         * @param <T>
         * @param source
         * @param target
         * @param action
         * @return
         */
        public static <O, T> T copyByObject(O source, T target, IAction<T> action) {
            String baseKey = generateKey(source.getClass(), target.getClass());
            BeanCopier copier;
            if (!mapCaches.containsKey(baseKey)) {
                copier = BeanCopier.create(source.getClass(), target.getClass(), false);
                mapCaches.put(baseKey, copier);
            } else {
                copier = mapCaches.get(baseKey);
            }
            copier.copy(source, target, null);
            action.run(target);
            return target;
        }

        /**
         * 根据传进来的对象类型生成新的对象并复制
         * @param <O>
         * @param <T>
         * @param source
         * @param target
         * @return
         */
        private static <O, T> T baseMapper(O source, Class<T> target) {
            String baseKey = generateKey(source.getClass(), target);
            BeanCopier copier;
            if (!mapCaches.containsKey(baseKey)) {
                copier = BeanCopier.create(source.getClass(), target, false);
                mapCaches.put(baseKey, copier);
            } else {
                copier = mapCaches.get(baseKey);
            }
            T instance = null;
            try {
                instance = target.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.error("mapper 创建对象异常" + e.getMessage());
            }
            copier.copy(source, instance, null);
            return instance;
        }

        /**
         * 生成key
         * @param class1
         * @param class2
         * @return
         */
        private static String generateKey(Class<?> class1, Class<?> class2) {
            return class1.toString() + class2.toString();
        }

        /**
         * 对集合进行深拷贝
         * 注意需要岁泛型类进行序列化（实现serializable）
         *
         * @param src
         * @param <T>
         * @return
         * @throws ClassNotFoundException
         */
        public static <T> List<T> deepCopyList(List<T> src) {
            try {
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream outputStream = new ObjectOutputStream(byteOut);
                outputStream.writeObject(src);
                ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                ObjectInputStream inputStream = new ObjectInputStream(byteIn);
                return (List<T>) inputStream.readObject();
            } catch (Exception e) {
                throw new BusinessException(e);
            }
        }
}
