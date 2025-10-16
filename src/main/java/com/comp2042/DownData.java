package com.comp2042;

public final class DownData {

    // Also a Data Transfer Object

    // When a piece moves down to the bottom, then locks in place;
    // Piece needs to merge with the board
    // Game checks for completed rows
    // View needs to be updated;
    // This class packages the row clear results and the updated view state

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
