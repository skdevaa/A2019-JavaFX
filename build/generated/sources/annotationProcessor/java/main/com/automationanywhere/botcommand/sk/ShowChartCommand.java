package com.automationanywhere.botcommand.sk;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.model.record.Record;
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

public final class ShowChartCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(ShowChartCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    ShowChart command = new ShowChart();
    if(parameters.get("record") == null || parameters.get("record").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","record"));
    }

    if(parameters.get("charttype") == null || parameters.get("charttype").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","charttype"));
    }
    if(parameters.get("charttype") != null && parameters.get("charttype").get() != null && !(parameters.get("charttype").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","charttype", "String", parameters.get("charttype").get().getClass().getSimpleName()));
    }
    if(parameters.get("charttype") != null) {
      switch((String)parameters.get("charttype").get()) {
        case "line" : {

        } break;
        case "pie" : {

        } break;
        case "bar" : {

        } break;
        default : throw new BotCommandException(MESSAGES_GENERIC.getString("generic.InvalidOption","charttype"));
      }
    }

    if(parameters.get("chartlabel") == null || parameters.get("chartlabel").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","chartlabel"));
    }

    if(parameters.get("xlabel") == null || parameters.get("xlabel").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","xlabel"));
    }

    if(parameters.get("ylabel") == null || parameters.get("ylabel").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","ylabel"));
    }

    if(parameters.get("width") == null || parameters.get("width").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","width"));
    }

    if(parameters.get("height") == null || parameters.get("height").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","height"));
    }

    if(parameters.get("record") != null && parameters.get("record").get() != null && !(parameters.get("record").get() instanceof Record)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","record", "Record", parameters.get("record").get().getClass().getSimpleName()));
    }
    if(parameters.get("charttype") != null && parameters.get("charttype").get() != null && !(parameters.get("charttype").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","charttype", "String", parameters.get("charttype").get().getClass().getSimpleName()));
    }
    if(parameters.get("chartlabel") != null && parameters.get("chartlabel").get() != null && !(parameters.get("chartlabel").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","chartlabel", "String", parameters.get("chartlabel").get().getClass().getSimpleName()));
    }
    if(parameters.get("xlabel") != null && parameters.get("xlabel").get() != null && !(parameters.get("xlabel").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","xlabel", "String", parameters.get("xlabel").get().getClass().getSimpleName()));
    }
    if(parameters.get("ylabel") != null && parameters.get("ylabel").get() != null && !(parameters.get("ylabel").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","ylabel", "String", parameters.get("ylabel").get().getClass().getSimpleName()));
    }
    if(parameters.get("width") != null && parameters.get("width").get() != null && !(parameters.get("width").get() instanceof Double)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","width", "Double", parameters.get("width").get().getClass().getSimpleName()));
    }
    if(parameters.get("height") != null && parameters.get("height").get() != null && !(parameters.get("height").get() instanceof Double)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","height", "Double", parameters.get("height").get().getClass().getSimpleName()));
    }
    try {
      command.action(parameters.get("record") != null ? (Record)parameters.get("record").get() : (Record)null ,parameters.get("charttype") != null ? (String)parameters.get("charttype").get() : (String)null ,parameters.get("chartlabel") != null ? (String)parameters.get("chartlabel").get() : (String)null ,parameters.get("xlabel") != null ? (String)parameters.get("xlabel").get() : (String)null ,parameters.get("ylabel") != null ? (String)parameters.get("ylabel").get() : (String)null ,parameters.get("width") != null ? (Double)parameters.get("width").get() : (Double)null ,parameters.get("height") != null ? (Double)parameters.get("height").get() : (Double)null );Optional<Value> result = Optional.empty();
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
