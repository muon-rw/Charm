package svenhjol.charm_core.fabric;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import svenhjol.charm_core.helper.TextHelper;
import svenhjol.charm_core.iface.ILog;

public class Log implements ILog {
    private final Logger log;

    public Log(String namespace) {
        log = LogManager.getFormatterLogger(TextHelper.snakeToUpperCamel(namespace));
    }

    @Override
    public void info(String message, Object... args) {
        log.error(message, args);
    }

    @Override
    public void info(Class<?> source, String message, Object... args) {
        log.info(makeMessage(source, message), args);
    }

    @Override
    public void warn(String message, Object... args) {
        log.warn(message, args);
    }

    @Override
    public void warn(Class<?> source, String message, Object... args) {
        log.warn(makeMessage(source, message), args);
    }

    @Override
    public void error(String message, Object... args) {
        log.error(message, args);
    }

    @Override
    public void error(Class<?> source, String message, Object... args) {
        log.error(makeMessage(source, message), args);
    }
}