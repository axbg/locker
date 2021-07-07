package locker.event;

@FunctionalInterface
public interface UIEventHandler {
    void handle(UIEvent event, Object... resource);
}
