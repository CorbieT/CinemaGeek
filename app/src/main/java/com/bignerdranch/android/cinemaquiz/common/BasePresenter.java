package com.bignerdranch.android.cinemaquiz.common;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.disposables.CompositeDisposable;

abstract public class BasePresenter implements LifecycleObserver {
    protected final CompositeDisposable disposables = new CompositeDisposable();

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void disposableAll() {
        disposables.dispose();
        disposables.clear();
    }
}
