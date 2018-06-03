package com.esgi.mypunch.navbar;

public class NavContentPresenterImpl implements  NavContentPresenter {

    private NavContentView navContentView;

    NavContentPresenterImpl(NavContentView navContentView) {
        this.navContentView = navContentView;
    }

    @Override
    public void onDestroy() {
        navContentView = null;
    }
}
