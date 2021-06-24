 package com.gitegg.platform.base.service;

 @FunctionalInterface
 public interface IAction<T> {
     void run(T param);
 }

