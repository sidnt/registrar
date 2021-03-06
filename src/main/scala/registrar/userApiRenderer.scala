package registrar

import caliban.GraphQL.graphQL
import caliban.RootResolver
import registrar.userRepo.{accessUserRepoService, inMemUserRepo}
import zio._

object userApiRenderer extends App {

  val z1 = for {
    urs         <-    accessUserRepoService
    queries     =     userGqlApi.queries(urs)
    gqlApi      =     graphQL(RootResolver(queries))
    _           <-    console.putStrLn(gqlApi.render)
  } yield ()

  val z2 = z1.provideCustomLayer(inMemUserRepo)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = z2.exitCode

}
