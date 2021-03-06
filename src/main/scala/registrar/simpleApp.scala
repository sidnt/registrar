package registrar

import caliban.GraphQL.graphQL
import caliban.RootResolver
import registrar.models.User
import registrar.userRepo.{accessUserRepoService, inMemUserRepo}
import zio.{App, ExitCode, URIO, console}


object simpleApp extends App {

  val z1 = for {
    urs         <-    accessUserRepoService
    u           =     User("1","Mr. User", "mruser@email.com")
    _           <-    console.putStrLn("creating user> " + u)
    _           <-    urs.createUser(u)
    queries     =     userGqlApi.queries(urs)
    gqlApi      =     graphQL(RootResolver(queries))
    interpreter <-    gqlApi.interpreter
    _           <-    console.putStrLn("querying user back next> ")
    results     <-    interpreter.execute(userQuery.query)
    _           <-    console.putStrLn(results.data.toString)
  } yield ()

  val z2 = z1.provideCustomLayer(inMemUserRepo)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = z2.exitCode

}
