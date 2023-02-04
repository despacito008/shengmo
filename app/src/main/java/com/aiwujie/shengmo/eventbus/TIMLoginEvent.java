package com.aiwujie.shengmo.eventbus;

public class TIMLoginEvent {
    boolean loginState;
    public TIMLoginEvent(boolean loginState) {
        this.loginState = loginState;
    }
}
