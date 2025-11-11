package com.comp2042.logic.movement;

import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;

/**
 * Represents a movement event in the game.
 * Contains the type of movement and the source that triggered it.
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Creates a new MoveEvent.
     *
     * @param eventType defines the type of movement event
     * @param eventSource is the source that triggered the event
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    public EventSource getEventSource() {
        return eventSource;
    }
}