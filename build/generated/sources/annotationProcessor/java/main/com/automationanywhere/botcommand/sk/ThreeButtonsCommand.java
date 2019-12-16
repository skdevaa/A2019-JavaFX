package com.automationanywhere.botcommand.sk;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ThreeButtonsCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(ThreeButtonsCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    ThreeButtons command = new ThreeButtons();
    if(parameters.get("title") == null || parameters.get("title").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","title"));
    }

    if(parameters.get("label") == null || parameters.get("label").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","label"));
    }

    if(parameters.get("button1") == null || parameters.get("button1").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","button1"));
    }

    if(parameters.get("button2") == null || parameters.get("button2").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","button2"));
    }

    if(parameters.get("button3") == null || parameters.get("button3").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","button3"));
    }

    if(parameters.get("title") != null && parameters.get("title").get() != null && !(parameters.get("title").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","title", "String", parameters.get("title").get().getClass().getSimpleName()));
    }
    if(parameters.get("label") != null && parameters.get("label").get() != null && !(parameters.get("label").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","label", "String", parameters.get("label").get().getClass().getSimpleName()));
    }
    if(parameters.get("button1") != null && parameters.get("button1").get() != null && !(parameters.get("button1").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","button1", "String", parameters.get("button1").get().getClass().getSimpleName()));
    }
    if(parameters.get("button2") != null && parameters.get("button2").get() != null && !(parameters.get("button2").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","button2", "String", parameters.get("button2").get().getClass().getSimpleName()));
    }
    if(parameters.get("button3") != null && parameters.get("button3").get() != null && !(parameters.get("button3").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","button3", "String", parameters.get("button3").get().getClass().getSimpleName()));
    }
    try {
      Optional<Value> result =  Optional.ofNullable(command.action(parameters.get("title") != null ? (String)parameters.get("title").get() : (String)null ,parameters.get("label") != null ? (String)parameters.get("label").get() : (String)null ,parameters.get("button1") != null ? (String)parameters.get("button1").get() : (String)null ,parameters.get("button2") != null ? (String)parameters.get("button2").get() : (String)null ,parameters.get("button3") != null ? (String)parameters.get("button3").get() : (String)null ));
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
