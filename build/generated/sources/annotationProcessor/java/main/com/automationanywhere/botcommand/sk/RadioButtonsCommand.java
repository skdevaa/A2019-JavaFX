package com.automationanywhere.botcommand.sk;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Double;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class RadioButtonsCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(RadioButtonsCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    RadioButtons command = new RadioButtons();
    if(parameters.get("options") == null || parameters.get("options").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","options"));
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

    if(parameters.get("options") != null && parameters.get("options").get() != null && !(parameters.get("options").get() instanceof List)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","options", "List<Value>", parameters.get("options").get().getClass().getSimpleName()));
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
      Optional<Value> result =  Optional.ofNullable(command.action(parameters.get("options") != null ? (List<Value>)parameters.get("options").get() : (List<Value>)null ,parameters.get("label") != null ? (String)parameters.get("label").get() : (String)null ,parameters.get("width") != null ? (Double)parameters.get("width").get() : (Double)null ,parameters.get("height") != null ? (Double)parameters.get("height").get() : (Double)null ));
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
