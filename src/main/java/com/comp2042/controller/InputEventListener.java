package com.comp2042.controller;

import com.comp2042.logic.clear.DownData;
import com.comp2042.logic.movement.MoveEvent;
import com.comp2042.view.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}
