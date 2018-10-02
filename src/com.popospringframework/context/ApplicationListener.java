package com.popospringframework.context;

public interface ApplicationListener<E> {

    void onApplicationEvent(E event);

}
