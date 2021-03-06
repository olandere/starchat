package com.getjenny.starchat.resources

/**
  * Created by angelo on 07/04/17.
  */

import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.server.Route
import com.getjenny.starchat.entities._
import com.getjenny.starchat.routing._
import com.getjenny.starchat.services.AnalyzerService
import akka.http.scaladsl.model.StatusCodes
import akka.pattern.CircuitBreaker
import com.getjenny.starchat.SCActorSystem

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

trait AnalyzersPlaygroundResource extends MyResource {

  def analyzersPlaygroundRoutes: Route = pathPrefix("analyzers_playground") {
    pathEnd {
      post {
        entity(as[AnalyzerEvaluateRequest]) { request =>
          val analyzerService = AnalyzerService
          val breaker: CircuitBreaker = StarChatCircuitBreaker.getCircuitBreaker()
          onCompleteWithBreaker(breaker)(analyzerService.evaluateAnalyzer(request)) {
            case Success(value) =>
              completeResponse(StatusCodes.OK, StatusCodes.BadRequest, value)
            case Failure(e) =>
              log.error("route=analyzersPlaygroundRoutes method=POST: " + e.getMessage)
              completeResponse(StatusCodes.BadRequest,
                Option{ReturnMessageData(code = 100, message = e.getMessage)})
          }
        }
      }
    }
  }
}



