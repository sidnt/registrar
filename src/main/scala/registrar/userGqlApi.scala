package registrar

import caliban.GraphQL.graphQL
import caliban.RootResolver
import registrar.models.{Fail, User, UserId}
import registrar.userGqlApi.Queries
import registrar.userRepo.{UserRepo, accessUserRepo, inMemUserRepo}
import zio._
import zio.console.Console

object userGqlApi {

  final case class GetUserArgs(userId: UserId)

  final case class Queries(
                          getUser: GetUserArgs => IO[Fail, User]
                          )

}

object apiRenderer extends App {

  val z1: ZIO[Console with UserRepo, Nothing, Unit] = for {
    ur        <-    accessUserRepo
    queries   =     Queries(gua => ur.getUser(gua.userId))
    gqlApi    =     graphQL(RootResolver(queries))
    _         <-    console.putStrLn(gqlApi.render)
  } yield ()

  val z2: ZIO[zio.ZEnv, Nothing, Unit] = z1.provideCustomLayer(inMemUserRepo)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = z2.exitCode

}
