package com.comp2042.logic.clear;

import com.comp2042.view.ViewData;

/**
 * Data transfer object containing results when a brick is locked in place.
 * Packages the row clearing results and updated view state.
 */
public final class DownData {

    private final ClearRow clearRow;
    private final ViewData viewData;

    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }
}