package com.automationanywhere.botcommand.sk;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Double;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ShowTableCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(ShowTableCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    ShowTable command = new ShowTable();
    if(parameters.get("table") == null || parameters.get("table").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","table"));
    }

    if(parameters.get("label") == null || parameters.get("label").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","label"));
    }

    if(parameters.get("width") == null || parameters.get("width").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","width"));
    }

    if(parameters.get("height") == null || parameters.get("height").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","height"));
    }

    if(parameters.get("table") != null && parameters.get("table").get() != null && !(parameters.get("table").get() instanceof Table)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","table", "Table", parameters.get("table").get().getClass().getSimpleName()));
    }
    if(parameters.get("label") != null && parameters.get("label").get() != null && !(parameters.get("label").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","label", "String", parameters.get("label").get().getClass().getSimpleName()));
    }
    if(parameters.get("width") != null && parameters.get("width").get() != null && !(parameters.get("width").get() instanceof Double)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","width", "Double", parameters.get("width").get().getClass().getSimpleName()));
    }
    if(parameters.get("height") != null && parameters.get("height").get() != null && !(parameters.get("height").get() instanceof Double)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","height", "Double", parameters.get("height").get().getClass().getSimpleName()));
    }
    try {
      command.action(parameters.get("table") != null ? (Table)parameters.get("table").get() : (Table)null ,parameters.get("label") != null ? (String)parameters.get("label").get() : (String)null ,parameters.get("width") != null ? (Double)parameters.get("width").get() : (Double)null ,parameters.get("height") != null ? (Double)parameters.get("height").get() : (Double)null );Optional<Value> result = Optional.empty();
      logger.traceExit(result);
      return result;
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","action"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
