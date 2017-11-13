package com.example.qingchen.vrmr;

import dagger.Component;

/**
 * @author qingchen
 * @date 17-11-12
 */
@Component(modules = InfoModule.class)
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
