package com.getjenny.starchat

/**
  * Created by Angelo Leto <angelo@getjenny.com> on 27/06/16.
  */

import scala.concurrent.ExecutionContext
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route

import com.getjenny.starchat.resources._
import com.getjenny.starchat.services._

trait Resources extends KnowledgeBaseResource with DecisionTableResource
  with RootAPIResource with IndexManagementResource with LanguageGuesserResource
  with TermResource with TokenizersResource with AnalyzersPlaygroundResource
  with SpellcheckResource

trait RestInterface extends Resources {
  implicit def executionContext: ExecutionContext

  lazy val knowledgeBaseService = KnowledgeBaseService
  lazy val decisionTableService = DecisionTableService
  lazy val indexManagementService = IndexManagementService
  lazy val languageGuesserService = LanguageGuesserService
  lazy val termService = TermService
  lazy val responseService = ResponseService
  lazy val analyzerService = AnalyzerService
  lazy val spellcheckService = SpellcheckService
  lazy val cronJobService = new CronJobService
  lazy val systemService = SystemService

  val routes: Route = LoggingEntities.logRequestAndResult(rootAPIsRoutes) ~
    LoggingEntities.logRequestAndResult(knowledgeBaseRoutes) ~
    LoggingEntities.logRequestAndResultB64(knowledgeBaseSearchRoutes) ~
    LoggingEntities.logRequestAndResultB64(decisionTableRoutes) ~
    LoggingEntities.logRequestAndResultB64(decisionTableSearchRoutes) ~
    LoggingEntities.logRequestAndResultB64(decisionTableResponseRequestRoutes) ~
    LoggingEntities.logRequestAndResult(decisionTableAnalyzerRoutes) ~
    LoggingEntities.logRequestAndResult(indexManagementRoutes) ~
    LoggingEntities.logRequestAndResult(languageGuesserRoutes) ~
    LoggingEntities.logRequestAndResultReduced(termRoutes) ~
    LoggingEntities.logRequestAndResult(esTokenizersRoutes) ~
    LoggingEntities.logRequestAndResult(analyzersPlaygroundRoutes) ~
    LoggingEntities.logRequestAndResult(spellcheckRoutes)
}
