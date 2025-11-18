package com.comp2042.view.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


class PauseGameTest {

    private GuiController guiController;

    @BeforeEach
    void setUp() {
        guiController = new GuiController();
    }

    @Test
    @DisplayName("Should have pause property initialized")
    void testPausePropertyExists() throws Exception {
        Field isPauseField = GuiController.class.getDeclaredField("isPause");
        isPauseField.setAccessible(true);

        assertNotNull(isPauseField.get(guiController),
                "Pause property should be initialized");
    }

    @Test
    @DisplayName("Should start in unpaused state")
    void testInitialPauseState() throws Exception {
        Field isPauseField = GuiController.class.getDeclaredField("isPause");
        isPauseField.setAccessible(true);

        Object isPause = isPauseField.get(guiController);

        // Use reflection to get the value
        Method getValueMethod = isPause.getClass().getMethod("getValue");
        Boolean pauseState = (Boolean) getValueMethod.invoke(isPause);

        assertFalse(pauseState, "Game should start unpaused");
    }

    @Test
    @DisplayName("Pause toggle method exists and is accessible")
    void testPauseToggleMethodExists() {
        try {
            Method togglePauseMethod = GuiController.class.getDeclaredMethod("togglePause");
            assertNotNull(togglePauseMethod, "togglePause method should exist");
        } catch (NoSuchMethodException e) {
            fail("togglePause method should exist in GuiController");
        }
    }

    @Test
    @DisplayName("Should have pause overlay field")
    void testPauseOverlayExists() throws Exception {
        Field pauseOverlayField = GuiController.class.getDeclaredField("pauseOverlay");
        pauseOverlayField.setAccessible(true);

        // Field exists (might be null if not initialized via FXML)
        assertNotNull(pauseOverlayField, "pauseOverlay field should exist");
    }

    @Test
    @DisplayName("Should have showPauseOverlay method")
    void testShowPauseOverlayMethodExists() {
        try {
            Method showPauseMethod = GuiController.class.getDeclaredMethod("showPauseOverlay");
            assertNotNull(showPauseMethod, "showPauseOverlay method should exist");
        } catch (NoSuchMethodException e) {
            fail("showPauseOverlay method should exist");
        }
    }

    @Test
    @DisplayName("Should have hidePauseOverlay method")
    void testHidePauseOverlayMethodExists() {
        try {
            Method hidePauseMethod = GuiController.class.getDeclaredMethod("hidePauseOverlay");
            assertNotNull(hidePauseMethod, "hidePauseOverlay method should exist");
        } catch (NoSuchMethodException e) {
            fail("hidePauseOverlay method should exist");
        }
    }
}